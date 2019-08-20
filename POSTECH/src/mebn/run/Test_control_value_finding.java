package mebn.run;
  
import hmlp.rs.cps.CPSCompilerMain; 
import mebn_rm.util.TextFile;
 
public class Test_control_value_finding {

	public Test_control_value_finding() { 
	}
 
	public static void main(String[] args) { 
		 
		String sbn = "projects//ROOT_MTheory_1//Output//ROOT_MTheory_1_V_model_Step[4]_Target[1]_ssbn.txt";
        String net = new TextFile().load(sbn);
        
        System.out.println(net); 
         
        //-0.000042508190327861314 * P_SLAB_THICKNESS + 0.000016048825587212807 * P_SLAB_WIDTH + -0.00009731574274505774 * P_SLAB_LENGTH + -0.00009731574274505774 * P_SLAB_WEIGHT + -0.00009731574274505774 * P_PLATE_ORD_THICKNESS + -0.00009731574274505774 * P_PLATE_ORD_WIDTH + -0.00009731574274505774 * P_PLATE_ORD_LENGTH + -0.00009731574274505774 * P_PLATE_OBJ_THICKNESS + -0.00009731574274505774 * P_PLATE_OBJ_WIDTH + -0.00009731574274505774 * P_C + -0.00009731574274505774 * P_MN + -0.00007085637800440931 * F_FURNACE_NO + -0.0002815366890817376 * F_FURNACE_COL + -0.00009818177645917646 * F_FURNACE_TIME + 0.0006443121129473984 * TRE_FUR_IN + 0.0003930124344781086 * TRE_FUR_OUT + -0.00009731574274505774 * TRE_FUR_OBJ + -0.00009731574274505774 * TRE_FUR_UNI1 + -0.00009731574274505774 * TRE_FUR_UNI2 + -0.0021585412683626 * T_TIME_FUR_IN + 0.0004533175046159114 * T_TIME_FUR_OUT + -0.00009731574274505774 * RSH_PRE_EDGE_REDUCTION + -0.000747266142126233 * RSH_CAL_EDGE_REDUCTION + -0.0005205651878356138 * RSH_PRE_EDGE_LOAD + -0.0025091751128091264 * RSH_REAL_EDGE_LOAD + 0.00032590551997157706 * RSH_OBJ_SET + -0.0011705155872167893 * RSH_OBJ_REAL + 0.001496421107188365 * RSH_REAL_SET + -0.00009731574274505774 * RSB_BAR_OBJ_THICKNESS + 0.000016048825587211496 * RSB_BAR_REAL_THICKNESS + 0.00035239306708614745 * RSB_BAR_REAL_WIDTH + -0.008127766287862206 * RSB_BAR_REAL_LENGTH + -0.00009731574274505774 * RSB_UPPER_ROLL_DIA + -0.00009731574274505774 * RSB_UNDER_ROLL_DIA + 0.0007312454989129424 * TRE_RM_IN + 0.0005470245525762625 * TRE_RM_OUT + -0.0037980651756486248 * T_TIME_RM_IN + 0.0030038089242875924 * T_TIME_RM_OUT + -0.0000014171275600881922 * FSB_OUTER_THICKNESS_DIFF_3P + -0.000003136343402561631 * FSB_OUTER_THICKNESS_DIFF_CEN + -0.000005290869040531499 * FSB_CROWN + 0.0000009527016430149582 * FSB_INNER_THICKNESS_DIFF + -0.0000982889001725075 * FSB_UPPER_ROLL_DIA + -0.0000982889001725075 * FSB_UNDER_ROLL_DIA + -0.00009731574274505774 * TRE_FM_IN + -0.00007937809422194535 * TRE_FM_OUT + -0.00009731574274505774 * TRE_FM_IN_OBJ + -0.00009731574274505774 * TRE_FM_OUT_OBJ + -0.001639523907286031 * T_TIME_FM_IN + -0.001639523907286031 * T_TIME_FM_OUT + NormalDist( 130.08498246157131, 0.000000000000013627987627273797);}
//        List<String> inputs = new ArrayList<String>();
//        inputs.add("P_SLAB_THICKNESS"); 
//		inputs.add("P_SLAB_WIDTH"); 
//        inputs.add("P_SLAB_LENGTH"); 
//		inputs.add("P_SLAB_WEIGHT"); 
//		inputs.add("P_PLATE_ORD_THICKNESS"); 
//		inputs.add("P_PLATE_ORD_WIDTH"); 
//		inputs.add("P_PLATE_ORD_LENGTH"); 
//		inputs.add("P_PLATE_OBJ_THICKNESS"); 
//		inputs.add("P_PLATE_OBJ_WIDTH"); 
//		inputs.add("P_C"); 
//		inputs.add("P_MN"); 
//		inputs.add("F_FURNACE_NO"); 
//		inputs.add("F_FURNACE_COL");
//		inputs.add("F_FURNACE_TIME"); 
//		inputs.add("TRE_FUR_IN"); 
//		inputs.add("TRE_FUR_OUT"); 
//		inputs.add("TRE_FUR_OBJ"); 
//		inputs.add("TRE_FUR_UNI1"); 
//		inputs.add("TRE_FUR_UNI2"); 
//		inputs.add("T_TIME_FUR_IN"); 
//		inputs.add("T_TIME_FUR_OUT"); 
//		inputs.add("RSH_PRE_EDGE_REDUCTION"); 
//		inputs.add("RSH_CAL_EDGE_REDUCTION"); 
//		inputs.add("RSH_PRE_EDGE_LOAD"); 
//		inputs.add("RSH_REAL_EDGE_LOAD"); 
//		inputs.add("RSH_OBJ_SET"); 
//		inputs.add("RSH_OBJ_REAL"); 
//		inputs.add("RSH_REAL_SET"); 
//		inputs.add("RSB_BAR_OBJ_THICKNESS"); 
//		inputs.add("RSB_BAR_REAL_THICKNESS"); 
//		inputs.add("RSB_BAR_REAL_WIDTH"); 
//		inputs.add("RSB_BAR_REAL_LENGTH"); 
//		inputs.add("RSB_UPPER_ROLL_DIA"); 
//		inputs.add("RSB_UNDER_ROLL_DIA"); 
//		inputs.add("TRE_RM_IN"); 
//		inputs.add("TRE_RM_OUT"); 
//		inputs.add("T_TIME_RM_IN"); 
//		inputs.add("T_TIME_RM_OUT"); 
//		inputs.add("FSB_OUTER_THICKNESS_DIFF_3P"); 
//		inputs.add("FSB_OUTER_THICKNESS_DIFF_CEN"); 
//		inputs.add("FSB_CROWN"); 
//		inputs.add("FSB_INNER_THICKNESS_DIFF"); 
//		inputs.add("FSB_UPPER_ROLL_DIA"); 
//		inputs.add("FSB_UNDER_ROLL_DIA"); 
//		inputs.add("TRE_FM_IN"); 
//		inputs.add("TRE_FM_OUT"); 
//		inputs.add("TRE_FM_IN_OBJ"); 
//		inputs.add("TRE_FM_OUT_OBJ"); 
//		inputs.add("T_TIME_FM_IN"); 
//		inputs.add("T_TIME_FM_OUT");    
         
		String evidence = "";
		evidence += "defineEvidence( " + "P_SLAB_THICKNESS" + " , " + 300 + "); \n";
		evidence += "defineEvidence( " + "P_SLAB_WIDTH" + " , " + 2400 + "); \n";
		evidence += "defineEvidence( " + "P_SLAB_LENGTH" + " , " + 4620 + "); \n";
		evidence += "defineEvidence( " + "P_SLAB_WEIGHT" + " , " + 3 + "); \n";
		evidence += "defineEvidence( " + "P_PLATE_ORD_THICKNESS" + " , " + 32 + "); \n";
		evidence += "defineEvidence( " + "P_PLATE_ORD_WIDTH" + " , " + 4126 + "); \n";
		evidence += "defineEvidence( " + "P_PLATE_ORD_LENGTH" + " , " + 25189 + "); \n";
		evidence += "defineEvidence( " + "P_PLATE_OBJ_THICKNESS" + " , " + 32 + "); \n";
		evidence += "defineEvidence( " + "P_PLATE_OBJ_WIDTH" + " , " + 4136 + "); \n";
		evidence += "defineEvidence( " + "P_C" + " , " + 2 + "); \n";
		evidence += "defineEvidence( " + "P_MN" + " , " + 3 + "); \n";
		evidence += "defineEvidence( " + "F_FURNACE_NO" + " , " + 2 + "); \n";
		evidence += "defineEvidence( " + "F_FURNACE_COL" + " , " + 3 + "); \n";
		
		evidence += "defineEvidence( " + "QR_RESULT_COL_1" + " , " + 0 + "); \n";
		  
        CPSCompilerMain cpsCompiler = new CPSCompilerMain();
        cpsCompiler.InitCompiler(); 
        cpsCompiler.compile(net + evidence + " run(DMP);");
        		
		// print the ranking
//		for (int i = 0; i < ranking.size(); i++) {
//			System.out.println("Rank" + (i+1) + ": Node = " + ranking.get(i).getKey() + ", score = " + ranking.get(i).getValue());
//		}
	} 
}
