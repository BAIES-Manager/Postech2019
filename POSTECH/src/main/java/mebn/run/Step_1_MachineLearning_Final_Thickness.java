package mebn.run;
    
import java.awt.Toolkit;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import hmlp_tool.OpenMEBNeditor;
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

public class Step_1_MachineLearning_Final_Thickness { 
    SortableValueMap<String, List<Double>> mapResults = new SortableValueMap<String, List<Double>>();
    SortableValueMap<String, Sensitivity_Specificity_score> mapAUC = new SortableValueMap<String, Sensitivity_Specificity_score>();
    SortableValueMap<Integer, SortableValueMap<String, List<Double>>> mapSBN_CRPSs = new SortableValueMap<Integer, SortableValueMap<String, List<Double>>> ();
	     
    String schema = "Postech2019_Final";
    // System Information 
    Long operatingTime;
    Long operatingMemory;
    Long timeStart;
    Long memoryStart;
    Runtime runtime;
    	 
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// System Resource Checker 
	//
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
    	
    	//////////////////////////////////////////////////////////////////
    	// 1. Access a relational database (e.g., "postech2019_pass_2")
         
        RDB.This().connect("localhost", "root", "jesus"); 
        RDB.This().init(schema);

    	//////////////////////////////////////////////////////////////////
    	// 2. Perform MEBN-RM mapping 
        MTheory m = new RM_To_MEBN(RDB.This()).run();
        
        System.out.println("*** After MEBN-RM *************************************************");
        System.out.println(m.toString(new String[]{"MFrag", "MNode"}));
        
    	//////////////////////////////////////////////////////////////////
    	// 3. Set parent-child relationships (i.e., causal relationships)    
  		String childMNode = "";
  		List<String> parentMNodes = null; 
  				 
  		childMNode = "pass.Y_InP";
  		parentMNodes = new ArrayList<String>();
  		parentMNodes.add("pass.X1_InP");
  		parentMNodes.add("pass.X2_InP"); 
  		parentMNodes.add("pass.X3_InP"); 
  		parentMNodes.add("pass.X4_InP"); 
  		parentMNodes.add("pass.X5_InP"); 
  		parentMNodes.add("pass.X6_InP"); 
  		parentMNodes.add("pass.X7_InP"); 
  		parentMNodes.add("pass.X8_InP"); 
  		parentMNodes.add("pass.X9_InP"); 
  		parentMNodes.add("pass.X10_InP"); 
  		parentMNodes.add("pass.X11_InP"); 
  		parentMNodes.add("pass.X12_InP"); 
  		parentMNodes.add("pass.X13_InP"); 
  		parentMNodes.add("pass.X14_InP"); 
  		m.addParents(childMNode, parentMNodes);

        System.out.println("*** After using expert knowledge *************************************************");
        System.out.println(m.toString(new String[]{"MFrag", "MNode"}));

    	//////////////////////////////////////////////////////////////////
    	// 4. update default contexts  
		m.updateContexts();
		
    	//////////////////////////////////////////////////////////////////
    	// 5. Update default class local distributions (CLDs)
		m.updateCLDs();
		
		System.out.println("*** Update context nodes and CLDs  *************************************************");
        
    	//////////////////////////////////////////////////////////////////
        // 6. Perform MEBN learning 
		MRoot mroot = new MRoot();
        mroot.setMTheories(new MTheory[]{m});
        new MTheory_Learning().run(mroot);
        System.out.println(m.toString(new String[]{"MFrag", "MNode", "CLD"}));
        System.out.println("Learning Completed!");
        
        resourceCheckEnd();
         
    	//////////////////////////////////////////////////////////////////
        // 7. Convert from m to a MEBN in UnBBayes
        // To save a MEBN file, cps, continuous, MEBN_LM, MEBN_RM, UnBBayes, MEBN are required
        // and HML_UnBBayes.Jar should be removed in the project setting. 
        
        // new OpenMEBNeditor().run1(m, "projects//ROOT_MTheory_1//Output//learnedMEBN.ubf");
        
    	//////////////////////////////////////////////////////////////////
        // construct a script BN from a learned MEBN model 
        String file = new ConvertFromMTheoryToSBN().save(m , "_Target_"+schema);
        System.out.println("************************************************************************************");
        System.out.println(file);
        System.out.println("************************************************************************************");
        
    	//////////////////////////////////////////////////////////////////
        // 8. Save SSBN MEBN learning
        saveLearningResults();
        
        return file;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Bayesian Network Reasoning Operation 
    //
    public void reasoning(String sbn) throws SQLException { 
    	ArrayList<String> targets = new ArrayList<String>();
        ArrayList<String> evidence = new ArrayList<String>();
          
    	evidence.add("X1_InP");
    	evidence.add("X2_InP");
    	evidence.add("X3_InP");
    	evidence.add("X4_InP");
    	evidence.add("X5_InP");
    	evidence.add("X6_InP");
    	evidence.add("X7_InP");
    	evidence.add("X8_InP");
    	evidence.add("X9_InP");
    	evidence.add("X10_InP");
    	evidence.add("X11_InP");
    	evidence.add("X12_InP");
    	evidence.add("X13_InP");
    	evidence.add("X14_InP");
    	 
        targets.add("Y_InP");
        
    	reasoning(sbn, targets, evidence); 
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
        
        saveReasoningResults(sbn, targets);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Save BN Reasoning Results 
    //
    public void saveReasoningResults(String sbn, ArrayList<String> targets) throws SQLException {
    	String type = "[Type: " + schema.toString() + "]";
    	DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss"); 
    	String date = "[Date: " + df.format(new Date()) + "]";
    	 
    	// Step 1. save the reasoning results
    	String fileCSVResult = "projects//ROOT_MTheory_1//result_csv//Result_reasoning"; 
    	
    	ExcelCSV.createCSV((String)fileCSVResult);
    	// write net information
    	String[] net_info = new String[2];
    	net_info[0] = type;
    	net_info[1] = date;
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
        
    	String[] summary = new String[5];
    	summary[0] = type; 
    	summary[1] = date;
    	summary[2] = operatingTime.toString();// + " Milliseconds";
    	summary[3] = operatingMemory.toString();// + " MB";
    	List<Double> CRPSs = mapResults.get(targets.get(0) + "_CRPS");
    	Double avg = new ListMgr().getAverage(CRPSs);
    	summary[4] = avg.toString();
    	ExcelCSV.writeCSV((String)fileCSVResult_summary, (String[])summary);
    	
    	// Save average crps for this SBN to find a best model for each target variable
     	if (mapSBN_CRPSs.containsKey(1)){
    		SortableValueMap<String, List<Double>> mapsbn_crps = mapSBN_CRPSs.get(1);
    		if (mapsbn_crps.containsKey(sbn)){
        		List<Double> avgCRPSs = mapsbn_crps.get(sbn);
        		avgCRPSs.add(avg);
        	}  
    	} else {
    		SortableValueMap<String, List<Double>> mapsbn_crps = new SortableValueMap<String, List<Double>>(); 
    		List<Double> avgCRPSs = new ArrayList<Double>();
    		avgCRPSs.add(avg);
    		mapsbn_crps.put(sbn, avgCRPSs);
    		mapSBN_CRPSs.put(1, mapsbn_crps);
    	}
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Save information about MEBN learning  
    //
    public void saveLearningResults() throws SQLException {
    	String type = "[Type: " + schema.toString() + "]";
    	DateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss"); 
    	String date = "[Date: " + df.format(new Date()) + "]";
    	
    	// Step 1. save the learning results
    	String fileCSVResult = "projects//ROOT_MTheory_1//result_csv//Result_learning_summary";
    	ExcelCSV.createCSV((String)fileCSVResult);

    	// write information
    	String[] info = new String[4];
    	info[0] = type;
    	info[1] = date;
    	info[2] = operatingTime.toString();// + " Milliseconds";
    	info[3] = operatingMemory.toString();//  + " MB";
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
      
    //////////////////////////////////////////////////////////////////////////////////////////////////
    // 1. The ideal model which was made by Prof. Lee is used for MEBN learning
    public void run_for_Ideal_Model() throws SQLException{ 
    	
    	//////////////////////////////////////////////////////
    	// Step 1: learning a MEBN model & generate a SBN
    	String sbn = learning();  
                
		////////////////////////////////////////////////////
		// Step 2: reasoning the SBN
        // e.g.,) String sbn = "projects//ROOT_MTheory_1//Output//ROOT_MTheory_1_Target_Postech2019_Final_ssbn.txt";
    	
//      String sbn = "projects//ROOT_MTheory_1//Output//ROOT_MTheory_1_Target_Postech2019_Final_ssbn.txt";
//    	reasoning(sbn);
    	
        Toolkit.getDefaultToolkit().beep();
         
    }
    
    public static void main(String[] args) throws SQLException {   
    	
    	//1. The ideal model which was made by Prof. Lee is used for MEBN learning
    	new Step_1_MachineLearning_Final_Thickness().run_for_Ideal_Model();
    	 
    }
}

