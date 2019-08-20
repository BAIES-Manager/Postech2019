package mebn.test;

import java.util.ArrayList;
  
public class Evidence_posco_mill {

	public Evidence_posco_mill() { 
	}
	
	public void get_case5(ArrayList<String> targets, ArrayList<String> evidence) { 
    	targets.clear();
    	evidence.clear();
    	evidence.add("RPS_RM_PRE_WK");
        targets.add("QR_RESULT_COL_1");
	}
	
    public void get_static_case(ArrayList<String> targets, ArrayList<String> evidence, int step, int target_quality) { 
    	targets.clear();
    	evidence.clear();
    	
        targets.add("QR_RESULT_COL_" + target_quality);
        
        // Input
        if (step == 1 || step == 2 || step == 3 || step == 4) {
        	evidence.add("P_ORDER_TYPE");
	 		evidence.add("P_SLAB_THICKNESS");
	 		evidence.add("P_SLAB_WIDTH");
	 		evidence.add("P_SLAB_LENGTH");
	 		evidence.add("P_SLAB_WEIGHT");
	 		evidence.add("P_PLATE_ORD_THICKNESS");
	 		evidence.add("P_PLATE_ORD_WIDTH");
	 		evidence.add("P_PLATE_ORD_LENGTH");
	 		evidence.add("P_PLATE_OBJ_THICKNESS");
	 		evidence.add("P_PLATE_OBJ_WIDTH");
	 		evidence.add("P_C");
	 		evidence.add("P_MN");
        }

         // Furnace
        if (step == 2 || step == 3 || step == 4) {
	 		evidence.add("F_FURNACE_NO");
	 		evidence.add("F_FURNACE_COL");
	 		evidence.add("F_FURNACE_TIME");
	 		evidence.add("T_TIME_FUR_IN");
	 		evidence.add("T_TIME_FUR_OUT");
	 		evidence.add("TRE_FUR_IN");
	 		evidence.add("TRE_FUR_OUT");
	 		evidence.add("TRE_FUR_OBJ");
	 		evidence.add("TRE_FUR_UNI1");
	 		evidence.add("TRE_FUR_UNI2");
        }
 		
        // RM
        if (step == 3 || step == 4) {
	 		evidence.add("RSH_PRE_EDGE_REDUCTION");
	 		evidence.add("RSH_CAL_EDGE_REDUCTION");
	 		evidence.add("RSH_PRE_EDGE_LOAD");
	 		evidence.add("RSH_REAL_EDGE_LOAD");
	 		evidence.add("RSH_OBJ_SET");
	 		evidence.add("RSH_OBJ_REAL");
	 		evidence.add("RSH_REAL_SET");
	 		evidence.add("RSB_BAR_OBJ_THICKNESS");
	 		evidence.add("RSB_BAR_REAL_THICKNESS");
	 		evidence.add("RSB_BAR_REAL_WIDTH");
	 		evidence.add("RSB_BAR_REAL_LENGTH");
	 		evidence.add("RSB_UPPER_ROLL_DIA");
	 		evidence.add("RSB_UNDER_ROLL_DIA");
	 		evidence.add("TRE_RM_IN");
	 		evidence.add("TRE_RM_OUT");
	 		evidence.add("T_TIME_RM_IN");
	 		evidence.add("T_TIME_RM_OUT");
        }
        
 		// FM
        if (step == 4) {
	 		evidence.add("FSB_OUTER_THICKNESS_DIFF_3P");
	 		evidence.add("FSB_OUTER_THICKNESS_DIFF_CEN");
	 		evidence.add("FSB_CROWN");
	 		evidence.add("FSB_INNER_THICKNESS_DIFF");
	 		evidence.add("FSB_UPPER_ROLL_DIA");
	 		evidence.add("FSB_UNDER_ROLL_DIA");  
	 		evidence.add("TRE_FM_IN");
	 		evidence.add("TRE_FM_OUT");
	 		evidence.add("TRE_FM_IN_OBJ");
	 		evidence.add("TRE_FM_OUT_OBJ");
	 		evidence.add("T_TIME_FM_IN");
	 		evidence.add("T_TIME_FM_OUT");
        }
    }
    
    public void get_dynamic_case(ArrayList<String> targets, ArrayList<String> evidence, int sizeRMPass, int sizeFMPass, int step, int target_quality) { 
        
    	get_static_case(targets, evidence, step, target_quality);
        
    	if (step == 2 || step == 3 || step == 4) {
    		for (int i = 1; i < sizeRMPass + 1; i++) {
    			evidence.add("RPS_SET_ROLLGAP_"+i);
    			evidence.add("RPS_REAL_ROLLGAP_"+i);
		    	evidence.add("RPS_DIFF_ROLLGAP_"+i);
		    	evidence.add("RPS_RM_PRE_WK_"+i);
		    	evidence.add("RPS_RM_ACT_WK_"+i);
		    	evidence.add("RPS_RM_PRE_WM_"+i);
		    	evidence.add("RPS_RM_ACT_WM_"+i);
		    	evidence.add("RPS_RM_PRE_THICK_"+i);
		    	evidence.add("RPS_RM_Delivery_THICK_"+i);
		    	evidence.add("RPS_RM_PRE_WIDTH_"+i);
		    	evidence.add("RPS_RM_REC_WIDTH_"+i);
		    	evidence.add("RPS_RM_PRE_LENGTH_"+i);
		    	evidence.add("RPS_RM_REC_LENGTH_"+i);
		    	evidence.add("RPS_RM_PRE_TEMP_"+i);
		    	evidence.add("RPS_SET_EDGGAP_"+i);
		    	evidence.add("RPS_REAL_EDGGAP_"+i);
		    	evidence.add("RPS_DIFF_EDEGAP_"+i);
		    	evidence.add("RPS_RM_IND_SPEED_"+i);
		    	evidence.add("RPS_RM_ACT_SPEED_"+i); 
	    	} 
    	}
    	
    	if (step == 3 || step == 4) {
	    	for (int i = 1; i < sizeFMPass + 1; i++) {
	    		evidence.add("FPS_SET_ROLLGAP_"+i);
	    		evidence.add("FPS_REAL_ROLLGAP_"+i);
	    		evidence.add("FPS_DIFF_ROLLGAP_"+i);
	    		evidence.add("FPS_FM_PRE_WK_"+i);
	    		evidence.add("FPS_FM_ACT_WK_"+i);
	    		evidence.add("FPS_FM_PRE_WM_"+i);
	    		evidence.add("FPS_FM_ACT_WM_"+i);
	    		evidence.add("FPS_FM_PRE_MAH_"+i);
	    		evidence.add("FPS_FM_REC_MAH_"+i);
	    		evidence.add("FPS_FM_PRE_THICK_"+i);
	    		evidence.add("FPS_FM_Delivery_THICK_"+i);
	    		evidence.add("FPS_FM_PRE_WIDTH_"+i);
	    		evidence.add("FPS_FM_REC_WIDTH_"+i);
	    		evidence.add("FPS_FM_PRE_LENGTH_"+i);
	    		evidence.add("FPS_FM_REC_LENGTH_"+i);
	    		evidence.add("FPS_FM_IND_SPEED_"+i);
	    		evidence.add("FPS_FM_ACT_SPEED_"+i);
	    		evidence.add("FPS_FM_PRE_TEMP_"+i);
	    		evidence.add("FPS_FM_REC_TEMP_"+i); 
	    	} 
    	} 
    }
}
