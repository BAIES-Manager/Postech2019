package mebn.run;
  
import java.awt.Toolkit;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import mebn.reasoner.Reasoner;
import mebn.reasoner.SensitivityAnalysisDriver;
import mebn.reasoner.SensitivityAnalysisDriver_for_Values;
import mebn.score.Sensitivity_Specificity_score;
import mebn_ln.converter.ConvertFromMTheoryToSBN;
import mebn_ln.core.MTheory_Learning; 
import mebn_rm.MEBN.MTheory.MRoot;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.RDB.RDB;
import mebn_rm.core.RM_To_MEBN;
import mebn_rm.util.ExcelCSV; 
import mebn_rm.util.TextFile;
import util.ListMgr;
import util.SortableValueMap; 

public class Step_1_MachineLearning { 
    SortableValueMap<String, List<Double>> mapResults = new SortableValueMap<String, List<Double>>();
    SortableValueMap<String, Sensitivity_Specificity_score> mapAUC = new SortableValueMap<String, Sensitivity_Specificity_score>();
     SortableValueMap<Integer, SortableValueMap<String, List<Double>>> mapSBN_CRPSs = new SortableValueMap<Integer, SortableValueMap<String, List<Double>>> ();
     
    enum mode {V_model, Ideal_model}; // Naive_model
    mode cur_mode = mode.V_model;
    Integer model_step = 1;
    Integer target_quality = 1; 
    int sizeRMPass = 16;
    int sizeFMPass = 22;    
        
    Long operatingTime;
    Long operatingMemory;
    Long timeStart;
    Long memoryStart;
    Runtime runtime;
    
	Evidence_posco_mill evidenceMgr = new Evidence_posco_mill();
	Rules_posco_mill ruleMgr = new Rules_posco_mill();
     
    public void resourceCheckStart(){
    	System.gc();
    	timeStart = System.nanoTime();
    	runtime = Runtime.getRuntime();
        runtime.gc();
        memoryStart = (runtime.totalMemory() - runtime.freeMemory())/(1024L * 1024L); 
    }
    
    public void resourceCheckEnd(){
    	operatingTime = (System.nanoTime() - timeStart) / 1000000; // milliseconds 
        operatingMemory = ((runtime.totalMemory() - runtime.freeMemory())/(1024L * 1024L)) - memoryStart; // megabyte
    }
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // MEBN Learning Operation 
    //
    public String learning() throws SQLException {
    	resourceCheckStart();
    	
    	// 1. access a relational database (e.g., "posco")
        String schema = "posco"; 
        RDB.This().connect("localhost", "root", "jesus"); 
        RDB.This().init(schema);

    	// 2. perform MEBN-RM mapping 
        MTheory m = new RM_To_MEBN(RDB.This()).run();
        
        System.out.println("*** After MEBN-RM *************************************************");
        System.out.println(m.toString(new String[]{"MFrag", "MNode"}));
        
    	// 3. set rules (i.e., causal relationships)    
        if (cur_mode == mode.V_model) {
        	ruleMgr.setRules_V_Model_heavy(m, sizeRMPass, sizeFMPass, target_quality); 
//        	ruleMgr.setRules_V_Model_light(m, target_quality); 
        } else if (cur_mode == mode.Ideal_model) {
        	ruleMgr.setRules_Ideal_Model(m, sizeRMPass, sizeFMPass, target_quality);
        } 
//        else if (cur_mode == mode.Naive_model) {
//        	ruleMgr.setRules_naive_heavy(m, sizeRMPass, sizeFMPass, model_step, target_quality);
//        } 
         
        System.out.println("*** After expert knowledge *************************************************");
        System.out.println(m.toString(new String[]{"MFrag", "MNode"}));

    	// 4. update default contexts  
		m.updateContexts();
		
    	// 5. update default class local distributions (CLDs)
		m.updateCLDs();
		
		System.out.println("*** Update context nodes and CLDs  *************************************************");
        
        // 6. perform MEBN learning 
		MRoot mroot = new MRoot();
        mroot.setMTheories(new MTheory[]{m});
        new MTheory_Learning().run(mroot);
        System.out.println(m.toString(new String[]{"MFrag", "MNode", "CLD"}));
        System.out.println("Learning Completed!");

        // construct a script BN from a learned MEBN model 
        String file = new ConvertFromMTheoryToSBN().save(m , "_" + cur_mode + "_Target[" + target_quality +"]");
        System.out.println("************************************************************************************");
        System.out.println(file);
        System.out.println("************************************************************************************");
        
        resourceCheckEnd();
        
        // 7. save SSBN MEBN learning
        saveLearningResults();
        
        return file;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Bayesian Network Reasoning Operation 
    //
    public void reasoning(String sbn) throws SQLException {
    	
        for (int step = 1; step < model_step+1; step++) {
        	ArrayList<String> targets = new ArrayList<String>();
            ArrayList<String> evidence = new ArrayList<String>();
            
        	if (cur_mode == mode.V_model) { 
		        evidenceMgr.get_dynamic_case(targets, evidence, sizeRMPass, sizeFMPass, step, target_quality);
//		        evidenceMgr.get_static_case(targets, evidence, step, target_quality);
        	} else if (cur_mode == mode.Ideal_model) { 
        		evidenceMgr.get_dynamic_case(targets, evidence, sizeRMPass, sizeFMPass, step, target_quality); 
        	} 
//        	else if (cur_mode == mode.Naive_model) { 
//        		evidenceMgr.get_dynamic_case(targets, evidence, sizeRMPass, sizeFMPass, step, target_quality); 
//        	}
        	
        	reasoning(sbn, targets, evidence); 
        }  
    }
    
    public void reasoning(String sbn, ArrayList<String> targets, ArrayList<String> evidence) throws SQLException {
    	resourceCheckStart();
    	
        String ssbn = new TextFile().load(sbn);
      
    	mapResults.clear();
    	mapAUC.clear();
    	
        String fileCSV = "";
        fileCSV = "projects//ROOT_MTheory_1//test//test.csv";
        
        String reasonerType = "DMP";
                
        new Reasoner().reasoningUsingCSV(reasonerType, ssbn, mapResults, mapAUC, fileCSV, targets, evidence, 1);
        
        System.out.println("**************************************************");
        System.out.println("Reasoning Completed!");
        System.out.println("**************************************************");
        
        resourceCheckEnd();
        
        saveReasoningResults(sbn);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Save BN Reasoning Results 
    //
    public void saveReasoningResults(String sbn) throws SQLException {
    	String type = "[Type: " + cur_mode.toString() + "]";
    	String step = "[Step: " + model_step.toString() + "]";
    	String target = "[Target: " + target_quality.toString() + "]";
    	DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss"); 
    	String date = "[Date: " + df.format(new Date()) + "]";
    	 
    	// Step 1. save the reasoning results
    	String fileCSVResult = "projects//ROOT_MTheory_1//result_csv//Result_reasoning"; 
    	
    	ExcelCSV.createCSV((String)fileCSVResult);
    	// write net information
    	String[] net_info = new String[4];
    	net_info[0] = type;
    	net_info[1] = step;
    	net_info[2] = target;
    	net_info[3] = date;
    	ExcelCSV.writeCSV((String)fileCSVResult, (String[])net_info);
    	
    	// write data
        String[] heads = new String[mapResults.size()];
        String strTemp = "";
        int i = 0;
        for (String k : mapResults.keySet()) {
            heads[i++] = k;
            strTemp = k;
        }
        ExcelCSV.writeCSV((String)fileCSVResult, (String[])heads);
        List<Double> results = (List<Double>)mapResults.get((Object)strTemp);
        int j = 0;
        while (j < results.size()) {
            i = 0;
            for (String k2 : mapResults.keySet()) {
                results = (List<Double>)mapResults.get((Object)k2);
                heads[i++] = ((Double)results.get(j)).toString();
            }
            ExcelCSV.writeCSV((String)fileCSVResult, (String[])heads);
            ++j;
        }
        
        // Step 2. save the reasoning summary
        String fileCSVResult_summary = "projects//ROOT_MTheory_1//result_csv//Result_reasoning_summary"; 
     	ExcelCSV.createCSV((String)fileCSVResult_summary);
        
    	String[] summary = new String[7];
    	summary[0] = type;
    	summary[1] = step;
    	summary[2] = target;
    	summary[3] = date;
    	summary[4] = operatingTime.toString();// + " Milliseconds";
    	summary[5] = operatingMemory.toString();// + " MB";
    	List<Double> CRPSs = mapResults.get("QR_RESULT_COL_" + target_quality + "_CRPS");
    	Double avg = new ListMgr().getAverage(CRPSs);
    	summary[6] = avg.toString();
    	ExcelCSV.writeCSV((String)fileCSVResult_summary, (String[])summary);
    	
    	// Save average crps for this SBN to find a best model for each target variable
     	if (mapSBN_CRPSs.containsKey(target_quality)){
    		SortableValueMap<String, List<Double>> mapsbn_crps = mapSBN_CRPSs.get(target_quality);
    		if (mapsbn_crps.containsKey(sbn)){
        		List<Double> avgCRPSs = mapsbn_crps.get(sbn);
        		avgCRPSs.add(avg);
        	}  
    	} else {
    		SortableValueMap<String, List<Double>> mapsbn_crps = new SortableValueMap<String, List<Double>>(); 
    		List<Double> avgCRPSs = new ArrayList<Double>();
    		avgCRPSs.add(avg);
    		mapsbn_crps.put(sbn, avgCRPSs);
    		mapSBN_CRPSs.put(target_quality, mapsbn_crps);
    	}
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Save information about MEBN learning  
    //
    public void saveLearningResults() throws SQLException {
    	String type = "[Type: " + cur_mode.toString() + "]";
    	String step = "[Step: " + model_step.toString() + "]";
    	String target = "[Target: " + target_quality.toString() + "]";
    	DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss"); 
    	String date = "[Date: " + df.format(new Date()) + "]";
    	
    	// Step 1. save the learning results
    	String fileCSVResult = "projects//ROOT_MTheory_1//result_csv//Result_learning_summary";
    	ExcelCSV.createCSV((String)fileCSVResult);

    	// write information
    	String[] info = new String[6];
    	info[0] = type;
    	info[1] = step;
    	info[2] = target;
    	info[3] = date;
    	info[4] = operatingTime.toString();// + " Milliseconds";
    	info[5] = operatingMemory.toString();//  + " MB";
    	ExcelCSV.writeCSV((String)fileCSVResult, (String[])info); 
    }
     
    public SortableValueMap<Integer, String> findBestModel(){
    	SortableValueMap<Integer, String> bestModels = new SortableValueMap<Integer, String>();
    	
    	for (Integer k : mapSBN_CRPSs.keySet()) {
    		SortableValueMap<String, List<Double>> mapsbn_crps = mapSBN_CRPSs.get(k); 
    		
    		String bestModel = "";
	    	Double min_crps = 1000000.0;
	    	for (String key : mapsbn_crps.keySet()){
	    		List<Double> avgCRPSs = mapsbn_crps.get(key);
	    		Double avg = new ListMgr().getAverage(avgCRPSs); 
	    		if (avg < min_crps) {
	    			bestModel = key;
	    			min_crps = avg;
	    		}
	    	}
	    	
	    	System.out.println("Best model is " + bestModel + " for " + k + "'s quality" );
	    	bestModels.put(k, bestModel);
    	}
     
    	return bestModels;
    }
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Sensitivity Analysis For Variables 
    //
    public void performSensitivityAnalysisForVariables(SortableValueMap<Integer, String> models) { 
    	resourceCheckStart();
    	
    	// Step 1. save the learning results
    	String fileCSVResult = "projects//ROOT_MTheory_1//result_csv//Result_SensitivityAnalysisForVariables";
    	ExcelCSV.createCSV((String)fileCSVResult);
    	
    	for (Integer num : models.keySet()) {
    		String sbn = models.get(num);
			String net = new TextFile().load(sbn);
	        
	        System.out.println(net);  
	         
	        ArrayList<String> targets = new ArrayList<String>();
            ArrayList<String> evidence = new ArrayList<String>();
             
            int step = 4;
            evidenceMgr.get_static_case(targets, evidence, step, target_quality); 
//            evidenceMgr.get_dynamic_case(targets, evidence, sizeRMPass, sizeFMPass, step, num); 
		    
			// prepare calculator of ranking (with score of sensitivity)
			SensitivityAnalysisDriver driver = new SensitivityAnalysisDriver();
			
			// looks like 4 samples is enough for this net and config. Even numbers [2,4,6,8,...] are suggested
			List<Entry<String, Float>> ranking = driver.calculateSensitivity(net, "QR_RESULT_COL_" + num, evidence, 0.1f, 10); 
			
			// print the ranking
			String s = "*** Ranks for QR_RESULT_COL_" + num + " *******************";
			System.out.println(s);
			
			// write information
	    	String[] info = new String[1];
	    	info[0] = s; 
	    	ExcelCSV.writeCSV((String)fileCSVResult, (String[])info); 
	    	
			for (int i = 0; i < ranking.size(); i++) {
				s = "Rank" + (i+1) + ": Node = " + ranking.get(i).getKey() + ", score = " + ranking.get(i).getValue();
				System.out.println(s);
				info[0] = s; 
				ExcelCSV.writeCSV((String)fileCSVResult, (String[])info); 
			} 
    	}
	}
    
    public void performSensitivityAnalysisForValuesGivenTargetValues(SortableValueMap<Integer, String> models) { 
    	resourceCheckStart();
    
    	for (Integer num : models.keySet()) {
    		String sbn = models.get(num);
			String net = new TextFile().load(sbn);
	         
			// prepare calculator of ranking (with score of sensitivity)
            SensitivityAnalysisDriver_for_Values driver = new SensitivityAnalysisDriver_for_Values();
            String evidence1 = "0";
            String evidence2 = "1";
            driver.run(net, "QR_RESULT_COL_" + num, evidence1, evidence2); 
    	}
	}
     

    //////////////////////////////////////////////////////////////////////////////////////////////////
    //2. The simple V model is used for MEBN learning  
    public void run_for_V_Model() throws SQLException{
    	for (int k = 1; k < 7; k++) 
    	{
    		target_quality = k; 
    		cur_mode = mode.V_model;  
    		  
    		// Step 1: learning a MEBN model & generate a SBN
	        String sbn = learning();  
	        
		    for (int i = 0; i < 4; i++) 
	    	{ 
			    model_step = i+1;
//	    		model_step = 4;
	    		 
		        // Step 2: reasoning the SBN
//		        String sbn = "projects//ROOT_MTheory_1//Output//ROOT_MTheory_1_dynamic_model_4_ssbn.txt";
 		        reasoning(sbn);
 		        System.gc();
	    	}
	    	 
    	}
    	
    	// Step 3: Find a best model
    	SortableValueMap<Integer, String> bestModels = findBestModel();
    	
    	Toolkit.getDefaultToolkit().beep();
    	
    	// Step 4: Perform sensitivity analysis for variables
    	performSensitivityAnalysisForVariables(bestModels);
    	
    	// Step 5: Perform sensitivity analysis for values given target values
    	performSensitivityAnalysisForValuesGivenTargetValues(bestModels);
    }
    
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // 1. The ideal model which was made by Prof. Lee is used for MEBN learning
    public void run_for_Ideal_Model() throws SQLException{
    	for (int k = 1; k < 7; k++) 
    	{
    		target_quality = k; 
    		cur_mode = mode.Ideal_model;   
    		  
    		// Step 1: learning a MEBN model & generate a SBN
	        String sbn = learning();  
	        
		  	for (int i = 0; i < 4; i++) 
	    	{ 
			    model_step = i+1;
//	    		model_step = 4;
	    		 
		        // Step 2: reasoning the SBN
//		        String sbn = "projects//ROOT_MTheory_1//Output//ROOT_MTheory_1_dynamic_model_4_ssbn.txt";
 		        reasoning(sbn);
 		        System.gc();
	    	} 
    	}
    	
    	// Step 3: Find a best model
    	SortableValueMap<Integer, String> bestModels = findBestModel();
    	
    	Toolkit.getDefaultToolkit().beep();
    	
    	// Step 4: Perform sensitivity analysis for variables
    	performSensitivityAnalysisForVariables(bestModels);
    	
    	// Step 5: Perform sensitivity analysis for values given target values
    	performSensitivityAnalysisForValuesGivenTargetValues(bestModels);
    }
    
    public static void main(String[] args) throws SQLException {   
    	Step_1_MachineLearning t = new Step_1_MachineLearning();
    	
    	//1. The ideal model which was made by Prof. Lee is used for MEBN learning
    	t.run_for_Ideal_Model();
    	
    	//2. The simple V model is used for MEBN learning   
    	t.run_for_V_Model(); 
    }
}

