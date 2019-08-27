/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  au.com.bytecode.opencsv.CSVReader
 *  hmlp.rs.cps.CPSCompilerMain
 *  hmlp.rs.cps.datastructure.EDB
 *  hmlp.rs.cps.datastructure.EDBUnit
 *  hmlp.rs.cps.datastructure.SortableValueMap
 *  hmlp.util.SortableValueMap
 *  mebn_rm.util.TempMathFunctions
 */
package mebn.reasoner;
 
import mebn.score.Brier_score;
import mebn.score.Continuous_ranked_probability_score;
import mebn.score.Sensitivity_Specificity_score;
import mebn_rm.util.ExcelCSV;
import util.SortableValueMap;
import util.TempMathFunctions;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException; 
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gmu.seor.prognos.unbbayesplugin.cps.CPSCompilerMain;
import edu.gmu.seor.prognos.unbbayesplugin.cps.datastructure.EDB;
import edu.gmu.seor.prognos.unbbayesplugin.cps.datastructure.EDBUnit; 
 

public class SensitivityAnalysisDriver_for_Values {
	class MyNormalDist{
		Double mean;
		Double var;
	}
	
    public void run(String ssbn, String RV, String evidence1, String evidence2) {
    	
    	CPSCompilerMain cpsCompiler = new CPSCompilerMain();
        
    	String evidence = "defineEvidence( " + RV + " , " + evidence1 + "); \n";
    	cpsCompiler.InitCompiler(); 
        cpsCompiler.compile(String.valueOf(ssbn) + evidence + " run(DMP);"); 
         
        SortableValueMap<String, MyNormalDist> mapResults = new SortableValueMap<String, MyNormalDist>();
    	checkTargets(mapResults);
        
        evidence = "defineEvidence( " + RV + " , " + evidence2 + "); \n";
    	cpsCompiler.InitCompiler(); 
        cpsCompiler.compile(String.valueOf(ssbn) + evidence + " run(DMP);"); 
        
        SortableValueMap<String, MyNormalDist> mapResults2 = new SortableValueMap<String, MyNormalDist>();
    	checkTargets(mapResults2);
    	
    	comparison(mapResults, mapResults2, RV, evidence1, evidence2);
        
    }
  
    public void comparison(SortableValueMap<String, MyNormalDist> mapResults, SortableValueMap<String, MyNormalDist> mapResults2, String RV, String evidence1, String evidence2) {
    	String fileCSVResult = "projects//ROOT_MTheory_1//result_csv//Result_SensitivityAnalysisForValues";
    	ExcelCSV.createCSV((String)fileCSVResult);
    	
    	// write information
    	String[] info = new String[6];
    	info[0] = "Node Name"; 
    	info[1] = "Mean"; 
    	info[2] = "Var"; 
    	info[3] = "Mean"; 
    	info[4] = "Var"; 
     	info[5] = "Difference"; 
    	ExcelCSV.writeCSV((String)fileCSVResult, (String[])info); 
    	
    	System.out.println("Node Name	" + "Mean" + "	" + "Var" + "	" + "Mean" + "	" + "Var");
    	System.out.println(RV + "Node Name	" + evidence1 + "	" + "" + "	" + evidence2 + "	" );
    	
    	info[0] = RV; 
    	info[1] = evidence1; 
    	info[2] = ""; 
    	info[3] = evidence2; 
    	info[4] = ""; 
    	info[5] = ""; 
    	ExcelCSV.writeCSV((String)fileCSVResult, (String[])info); 
    	
    	for (String node : mapResults.keySet()) {
    		MyNormalDist n1 = mapResults.get(node);
    		MyNormalDist n2 = mapResults2.get(node);
    			
    		info[0] = node; 
        	info[1] = n1.mean.toString(); 
        	info[2] = n1.var.toString(); 
        	info[3] = n2.mean.toString(); 
        	info[4] = n2.var.toString();
        	Double dif = n1.mean - n2.mean;
        	info[5] = dif.toString();
        	ExcelCSV.writeCSV((String)fileCSVResult, (String[])info); 
        	
    		System.out.println(node + "	" + n1.mean + "	" + n1.var + "	" + n2.mean + "	" + n2.var+ "	" + dif);
    	} 
    }
    
    public String getEvidence(List<String> targets, List<String> evs, Map<Integer, String> mapColumn, Integer i, String data, int curTime) {
        String column = mapColumn.get(i);
        if (evs.isEmpty() ? !targets.contains(column) : !evs.isEmpty() && evs.contains(column) && !targets.contains(column)) {
            return "defineEvidence( " + column + "_t" + curTime + " , " + data + "); \n";
        }
        return "";
    }
  
    public void checkTargets(SortableValueMap<String, MyNormalDist> mapResults) { 
        EDBUnit exactNodes = EDB.This().get("ROOT.ENGINES.DMP.NODES");
        EDBUnit exactISA = exactNodes.getRel("ISA"); 
                
        for (String str : exactISA.getMap().keySet()) {
            EDBUnit exactNode = (EDBUnit)exactISA.map.get((Object)str); 
            EDBUnit evidence = exactNode.get("EVIDENCE");
            EDBUnit predictiveBel = exactNode.get("BEL"); 
            
            System.out.println(str);
            predictiveBel.print("         ");
              
        	if (evidence == null) { 
                Double mu = predictiveBel.get("MU").getMatrixData();
                Double var = predictiveBel.get("SIGMA").getMatrixData(); 
                MyNormalDist n = new MyNormalDist();
                n.mean = mu;
                n.var = var;
                
                mapResults.put(str, n); 
            }
            System.out.println("[---------------------------------------------]");
        }  
    }
}

