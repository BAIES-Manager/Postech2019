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

import au.com.bytecode.opencsv.CSVReader;
import edu.gmu.seor.prognos.unbbayesplugin.cps.CPSCompilerMain;
import edu.gmu.seor.prognos.unbbayesplugin.cps.datastructure.EDB;
import edu.gmu.seor.prognos.unbbayesplugin.cps.datastructure.EDBUnit;
 
 

public class Reasoner {
	TempMathFunctions tMath = new TempMathFunctions();
	
    public void run(String ssbn, String reasonerType, String evidence) {
        CPSCompilerMain cpsCompiler = new CPSCompilerMain();
        cpsCompiler.InitCompiler();
        if (reasonerType.equalsIgnoreCase("DMP_1gmr")) {
            cpsCompiler.compile(String.valueOf(ssbn) + evidence + " run(DMP, maxTime(100000), maxIter(50), maxGMR(1));");
        } else {
            cpsCompiler.compile(String.valueOf(ssbn) + evidence + " run(" + reasonerType + ");");
        }
    }

    public void reasoningUsingCSV(String reasonerType, String ssbn, SortableValueMap<String, List<Double>> mapResults, 
    		SortableValueMap<String, Sensitivity_Specificity_score> mapAUC, String excelFile, 
    		List<String> targets, List<String> evs, int time) {
        CSVReader reader = null;
        FileReader fr = null;
        try {
            fr = new FileReader(excelFile);
            reader = new CSVReader((Reader)fr);
        }
        catch (FileNotFoundException e1) {
            e1.printStackTrace(); 
        }
        HashMap<Integer, String> mapColumn = new HashMap<Integer, String>();
        HashMap<Integer, String> mapData = new HashMap<Integer, String>();
        try {
            int i = 0;
            List<String[]> list = reader.readAll();
            for (String[] nextLine : list) {
            	String evidence = "";
                if (i == 0) {
                    int j = 0;
                    while (j < nextLine.length) {
                        String str = nextLine[j];
                        mapColumn.put(j, str);
                        ++j;
                    }
                } else if (i != 0) { 
                    int j = 0;
                    while (j < nextLine.length) {
                        String data = nextLine[j];
                        data = tMath.getSafeNumberString2((String)data);
                        mapData.put(j, data);
                        evidence += getEvidence(targets, evs, mapColumn, j, data);
                        ++j;
                    } 
                    
                    run(ssbn, reasonerType, evidence);
                    checkTargets(reasonerType, mapResults, mapAUC, targets, evs, mapColumn, mapData); 
                }
                ++i;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getEvidence(List<String> targets, List<String> evs, Map<Integer, String> mapColumn, Integer i, String data) {
        String column = mapColumn.get(i);
        if (evs.isEmpty() ? !targets.contains(column) : !evs.isEmpty() && evs.contains(column) && !targets.contains(column)) {
        	if (data == null)
        		return "";
        	
        	if (data.equalsIgnoreCase("null"))
        		return "";
        	
            return "defineEvidence( " + column + " , " + data + "); \n";
        }
        return "";
    }
    
    public String getEvidence(List<String> targets, List<String> evs, Map<Integer, String> mapColumn, Integer i, String data, int curTime) {
        String column = mapColumn.get(i);
        if (evs.isEmpty() ? !targets.contains(column) : !evs.isEmpty() && evs.contains(column) && !targets.contains(column)) {
            return "defineEvidence( " + column + "_t" + curTime + " , " + data + "); \n";
        }
        return "";
    }

    public String getTrueData(String target, Map<Integer, String> mapColumn, Map<Integer, String> data) {
        for (Integer c : mapColumn.keySet()) {
            String column = mapColumn.get(c);
            if (!target.equalsIgnoreCase(column)) continue;
            return data.get(c);
        }
        return "";
    }

    public String checkTargets(String typeReasoner, SortableValueMap<String, List<Double>> mapResults, SortableValueMap<String, Sensitivity_Specificity_score> mapAUC, List<String> targetRVs, List<String> evs,  Map<Integer, String> mapColumn, Map<Integer, String> mapData) {
        Double threshold = 0.3;
        String groundTruth = "";
        if (typeReasoner.equalsIgnoreCase("DMP_1gmr")) {
            typeReasoner = "DMP";
        }
        EDBUnit exactNodes = EDB.This().get("ROOT.ENGINES." + typeReasoner + ".NODES");
        EDBUnit exactISA = exactNodes.getRel("ISA");
        Brier_score brier = new Brier_score();
                
        for (String str : exactISA.getMap().keySet()) {
            EDBUnit exactNode = (EDBUnit)exactISA.map.get((Object)str);
            EDBUnit type = exactNode.get("INFO.TYPE");
            EDBUnit evidence = exactNode.get("EVIDENCE");
            EDBUnit predictiveBel = exactNode.get("BEL"); 
            
            System.out.println(str);
            predictiveBel.print("         ");
            
//            if (evidence != null || predictiveBel == null) continue;
//            predictiveBel.print("         ");
//            System.out.println("[---------------------------------------------]");
//            System.out.println(str);
//            
            if (type.getData().equalsIgnoreCase("Discrete")) {
                if (targetRVs.contains(str)) {
                    Sensitivity_Specificity_score auc;
                    double BrierScore;
                    List scores;
                    String truth = getTrueData(str, mapColumn, mapData);
                    System.out.println("Truth: " + truth);
                    groundTruth = String.valueOf(groundTruth) + str + ": " + truth + "\n";
                    if (mapResults.containsKey((Object)str)) {
                        scores = (List)mapResults.get((Object)str);
                        BrierScore = brier.run(predictiveBel, threshold, truth);
                        scores.add(BrierScore);
                    } else {
                        scores = new ArrayList();
                        mapResults.put(str, scores);
                        BrierScore = brier.run(predictiveBel, threshold, truth);
                        scores.add(BrierScore);
                    }
                    if (mapAUC.containsKey((Object)str)) {
                        auc = (Sensitivity_Specificity_score)mapAUC.get((Object)str);
                        auc.run(predictiveBel, threshold, truth);
                    } else {
                        auc = new Sensitivity_Specificity_score();
                        mapAUC.put(str, auc);
                        auc.run(predictiveBel, threshold, truth);
                    }
                }
            } else if (type.getData().equalsIgnoreCase("Continuous") ) {
            	if (targetRVs.contains(str)) {
	                
	                Double mu = predictiveBel.get("MU").getMatrixData();
	                Double var = predictiveBel.get("SIGMA").getMatrixData();
	                Double actual = Double.valueOf(getTrueData(str, mapColumn, mapData)); 
	                Double crps = Continuous_ranked_probability_score.run(mu, var, actual);
	                
	                // Set an actual value
	                List actuals;
	                String key_actual = str + "_Actual";
	                if (mapResults.containsKey((Object)key_actual)) {
	                	actuals = (List)mapResults.get((Object)key_actual);
	                	actuals.add(actual);
	                } else {
	                	actuals = new ArrayList();
	                    mapResults.put(key_actual, actuals);
	                    actuals.add(actual);
	                }
	                
	                // Set MU
	                List mus;
	                String key_mu = str + "_MU";
	                if (mapResults.containsKey((Object)key_mu)) {
	                	mus = (List)mapResults.get((Object)key_mu);
	                	mus.add(mu);
	                } else {
	                	mus = new ArrayList();
	                    mapResults.put(key_mu, mus);
	                    mus.add(mu);
	                }
	                
	                // Set VAR
	                List vars;
	                String key_var = str + "_VAR";
	                if (mapResults.containsKey((Object)key_var)) {
	                	vars = (List)mapResults.get((Object)key_var);
	                	vars.add(var);
	                } else {
	                	vars = new ArrayList();
	                    mapResults.put(key_var, vars);
	                    vars.add(var);
	                }
	                
	                // Set CRPS
	                List scores;
	                String key_crps = str + "_CRPS";
	                if (mapResults.containsKey((Object)key_crps)) {
	                    scores = (List)mapResults.get((Object)key_crps);
	                    scores.add(crps);
	                } else {
	                    scores = new ArrayList();
	                    mapResults.put(key_crps, scores);
	                    scores.add(crps);
	                }
            	} else if (evs.contains(str)) {
            		evidence.print("");
            		Double ev = evidence.getDataByDouble();
            		
            		List values;            		
            		if (mapResults.containsKey((Object)str)) {
            			values = (List)mapResults.get((Object)str);
            			values.add(ev);
	                } else {
	                	values = new ArrayList();
	                    mapResults.put(str, values);
	                    values.add(ev);
	                } 
            	}
            }
            System.out.println("[---------------------------------------------]");
        }
        System.out.println(mapResults);
        return groundTruth;
    }
}

