package mebn.test;
 
import java.util.ArrayList;
import java.util.List;

import mebn_ln.probabilistic_rule.ProbabilisticRules;
import mebn_rm.MEBN.MFrag.MFrag;
import mebn_rm.MEBN.MNode.MCNode;
import mebn_rm.MEBN.MNode.MDNode;
import mebn_rm.MEBN.MNode.MNode;
import mebn_rm.MEBN.MTheory.MTheory;
import mebn_rm.util.StringUtil;
import util.ListMgr;

public class Rules_posco_mill extends ProbabilisticRules {
	String childMNode = "";
	List<String> parentMNodes = new ArrayList<String>(); 
	
	public void initDynamicNodes_IdealModel(MTheory m, String child, String parent){
		parentMNodes.clear(); 
	    parentMNodes.add(parent); 
        childMNode = child; 
        m.addParents(childMNode, parentMNodes);
	}

    public List<String> initDynamicNodeList_IdealModel(MTheory m, int sizeRMPass, int sizeFMPass) { 
//    	List<String> nodeList = initStaticNodeList();
    	List<String> nodeList = initDynamicNodeList(sizeRMPass, sizeFMPass);    	
	 
//	    	nodeList.add("rm_pass.RPS_SET_ROLLGAP_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_REAL_ROLLGAP_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_DIFF_ROLLGAP_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_RM_PRE_WK_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_RM_ACT_WK_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_RM_PRE_WM_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_RM_ACT_WM_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_RM_PRE_THICK_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_RM_Delivery_THICK_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_RM_PRE_WIDTH_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_RM_REC_WIDTH_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_RM_PRE_LENGTH_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_RM_REC_LENGTH_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_RM_PRE_TEMP_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_SET_EDGGAP_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_REAL_EDGGAP_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_DIFF_EDEGAP_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_RM_IND_SPEED_"+sizeRMPass);
//	    	nodeList.add("rm_pass.RPS_RM_ACT_SPEED_"+sizeRMPass); 
	  
		for (int i = 2; i < sizeRMPass + 1; i++) {
			int j = i - 1;
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_SET_ROLLGAP_"+i, "rm_pass.RPS_SET_ROLLGAP_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_REAL_ROLLGAP_"+i, "rm_pass.RPS_REAL_ROLLGAP_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_DIFF_ROLLGAP_"+i, "rm_pass.RPS_DIFF_ROLLGAP_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_RM_PRE_WK_"+i, "rm_pass.RPS_RM_PRE_WK_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_RM_ACT_WK_"+i, "rm_pass.RPS_RM_ACT_WK_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_RM_PRE_WM_"+i, "rm_pass.RPS_RM_PRE_WM_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_RM_ACT_WM_"+i, "rm_pass.RPS_RM_ACT_WM_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_RM_PRE_THICK_"+i, "rm_pass.RPS_RM_PRE_THICK_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_RM_Delivery_THICK_"+i, "rm_pass.RPS_RM_Delivery_THICK_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_RM_PRE_WIDTH_"+i, "rm_pass.RPS_RM_PRE_WIDTH_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_RM_REC_WIDTH_"+i, "rm_pass.RPS_RM_REC_WIDTH_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_RM_PRE_LENGTH_"+i, "rm_pass.RPS_RM_PRE_LENGTH_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_RM_REC_LENGTH_"+i, "rm_pass.RPS_RM_REC_LENGTH_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_RM_PRE_TEMP_"+i, "rm_pass.RPS_RM_PRE_TEMP_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_SET_EDGGAP_"+i, "rm_pass.RPS_SET_EDGGAP_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_REAL_EDGGAP_"+i, "rm_pass.RPS_REAL_EDGGAP_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_DIFF_EDEGAP_"+i, "rm_pass.RPS_DIFF_EDEGAP_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_RM_IND_SPEED_"+i, "rm_pass.RPS_RM_IND_SPEED_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_RM_ACT_SPEED_"+i, "rm_pass.RPS_RM_ACT_SPEED_"+j);
			initDynamicNodes_IdealModel(m, "rm_pass.RPS_SET_EDGGAP_"+i, "rm_pass.RPS_SET_EDGGAP_"+j); 
    	}  
     
//    		nodeList.add("fm_pass.FPS_SET_ROLLGAP_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_REAL_ROLLGAP_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_DIFF_ROLLGAP_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_FM_PRE_WK_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_FM_ACT_WK_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_FM_PRE_WM_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_FM_ACT_WM_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_FM_PRE_MAH_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_FM_REC_MAH_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_FM_PRE_THICK_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_FM_Delivery_THICK_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_FM_PRE_WIDTH_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_FM_REC_WIDTH_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_FM_PRE_LENGTH_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_FM_REC_LENGTH_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_FM_IND_SPEED_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_FM_ACT_SPEED_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_FM_PRE_TEMP_"+sizeFMPass);
//	    	nodeList.add("fm_pass.FPS_FM_REC_TEMP_"+sizeFMPass); 
	    	
     	for (int i = 2; i < sizeFMPass + 1; i++) {
     		int j = i - 1;
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_SET_ROLLGAP_"+i, "fm_pass.FPS_SET_ROLLGAP_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_REAL_ROLLGAP_"+i, "fm_pass.FPS_REAL_ROLLGAP_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_DIFF_ROLLGAP_"+i, "fm_pass.FPS_DIFF_ROLLGAP_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_FM_PRE_WK_"+i, "fm_pass.FPS_FM_PRE_WK_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_FM_ACT_WK_"+i, "fm_pass.FPS_FM_ACT_WK_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_FM_PRE_WM_"+i, "fm_pass.FPS_FM_PRE_WM_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_FM_ACT_WM_"+i, "fm_pass.FPS_FM_ACT_WM_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_FM_PRE_MAH_"+i, "fm_pass.FPS_FM_PRE_MAH_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_FM_REC_MAH_"+i, "fm_pass.FPS_FM_REC_MAH_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_FM_PRE_THICK_"+i, "fm_pass.FPS_FM_PRE_THICK_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_FM_Delivery_THICK_"+i, "fm_pass.FPS_FM_Delivery_THICK_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_FM_PRE_WIDTH_"+i, "fm_pass.FPS_FM_PRE_WIDTH_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_FM_REC_WIDTH_"+i, "fm_pass.FPS_FM_REC_WIDTH_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_FM_PRE_LENGTH_"+i, "fm_pass.FPS_FM_PRE_LENGTH_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_FM_REC_LENGTH_"+i, "fm_pass.FPS_FM_REC_LENGTH_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_FM_IND_SPEED_"+i, "fm_pass.FPS_FM_IND_SPEED_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_FM_ACT_SPEED_"+i, "fm_pass.FPS_FM_ACT_SPEED_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_FM_PRE_TEMP_"+i, "fm_pass.FPS_FM_PRE_TEMP_"+j);
     		initDynamicNodes_IdealModel(m, "fm_pass.FPS_FM_REC_TEMP_"+i, "fm_pass.FPS_FM_REC_TEMP_"+j); 
    	} 
  
    	return nodeList;
    }
    
    public List<String> initDynamicNodeList(int sizeRMPass, int sizeFMPass) { 
    	List<String> nodeList = initStaticNodeList();
	
		for (int i = 1; i < sizeRMPass + 1; i++) {
	    	nodeList.add("rm_pass.RPS_SET_ROLLGAP_"+i);
	    	nodeList.add("rm_pass.RPS_REAL_ROLLGAP_"+i);
	    	nodeList.add("rm_pass.RPS_DIFF_ROLLGAP_"+i);
	    	nodeList.add("rm_pass.RPS_RM_PRE_WK_"+i);
	    	nodeList.add("rm_pass.RPS_RM_ACT_WK_"+i);
	    	nodeList.add("rm_pass.RPS_RM_PRE_WM_"+i);
	    	nodeList.add("rm_pass.RPS_RM_ACT_WM_"+i);
	    	nodeList.add("rm_pass.RPS_RM_PRE_THICK_"+i);
	    	nodeList.add("rm_pass.RPS_RM_Delivery_THICK_"+i);
	    	nodeList.add("rm_pass.RPS_RM_PRE_WIDTH_"+i);
	    	nodeList.add("rm_pass.RPS_RM_REC_WIDTH_"+i);
	    	nodeList.add("rm_pass.RPS_RM_PRE_LENGTH_"+i);
	    	nodeList.add("rm_pass.RPS_RM_REC_LENGTH_"+i);
	    	nodeList.add("rm_pass.RPS_RM_PRE_TEMP_"+i);
	    	nodeList.add("rm_pass.RPS_SET_EDGGAP_"+i);
	    	nodeList.add("rm_pass.RPS_REAL_EDGGAP_"+i);
	    	nodeList.add("rm_pass.RPS_DIFF_EDEGAP_"+i);
	    	nodeList.add("rm_pass.RPS_RM_IND_SPEED_"+i);
	    	nodeList.add("rm_pass.RPS_RM_ACT_SPEED_"+i); 
    	} 

    	for (int i = 1; i < sizeFMPass + 1; i++) {
	    	nodeList.add("fm_pass.FPS_SET_ROLLGAP_"+i);
	    	nodeList.add("fm_pass.FPS_REAL_ROLLGAP_"+i);
	    	nodeList.add("fm_pass.FPS_DIFF_ROLLGAP_"+i);
	    	nodeList.add("fm_pass.FPS_FM_PRE_WK_"+i);
	    	nodeList.add("fm_pass.FPS_FM_ACT_WK_"+i);
	    	nodeList.add("fm_pass.FPS_FM_PRE_WM_"+i);
	    	nodeList.add("fm_pass.FPS_FM_ACT_WM_"+i);
	    	nodeList.add("fm_pass.FPS_FM_PRE_MAH_"+i);
	    	nodeList.add("fm_pass.FPS_FM_REC_MAH_"+i);
	    	nodeList.add("fm_pass.FPS_FM_PRE_THICK_"+i);
	    	nodeList.add("fm_pass.FPS_FM_Delivery_THICK_"+i);
	    	nodeList.add("fm_pass.FPS_FM_PRE_WIDTH_"+i);
	    	nodeList.add("fm_pass.FPS_FM_REC_WIDTH_"+i);
	    	nodeList.add("fm_pass.FPS_FM_PRE_LENGTH_"+i);
	    	nodeList.add("fm_pass.FPS_FM_REC_LENGTH_"+i);
	    	nodeList.add("fm_pass.FPS_FM_IND_SPEED_"+i);
	    	nodeList.add("fm_pass.FPS_FM_ACT_SPEED_"+i);
	    	nodeList.add("fm_pass.FPS_FM_PRE_TEMP_"+i);
	    	nodeList.add("fm_pass.FPS_FM_REC_TEMP_"+i); 
	    	} 
 
    	return nodeList;
    }
    
    public List<String> initStaticNodeList() { 
    	List<String> nodeList = new ArrayList<String>();
     
    	nodeList.add("product.P_ORDER_TYPE");
      	nodeList.add("product.P_SLAB_THICKNESS");
      	nodeList.add("product.P_SLAB_WIDTH");
      	nodeList.add("product.P_SLAB_LENGTH");
      	nodeList.add("product.P_SLAB_WEIGHT");
      	nodeList.add("product.P_PLATE_ORD_THICKNESS");
      	nodeList.add("product.P_PLATE_ORD_WIDTH");
      	nodeList.add("product.P_PLATE_ORD_LENGTH");
      	nodeList.add("product.P_PLATE_OBJ_THICKNESS");
      	nodeList.add("product.P_PLATE_OBJ_WIDTH");
      	nodeList.add("product.P_C");
      	nodeList.add("product.P_MN");	
 
    	nodeList.add("furnace.F_FURNACE_NO");
      	nodeList.add("furnace.F_FURNACE_COL");
      	nodeList.add("furnace.F_FURNACE_TIME");	
      	 
    	nodeList.add("temperature.TRE_FUR_IN");
      	nodeList.add("temperature.TRE_FUR_OUT");
      	nodeList.add("temperature.TRE_FUR_OBJ");
      	nodeList.add("temperature.TRE_FUR_UNI1");
      	nodeList.add("temperature.TRE_FUR_UNI2");
      	 
      	nodeList.add("time.T_TIME_FUR_IN");
      	nodeList.add("time.T_TIME_FUR_OUT");
  
    		nodeList.add("rm_finish_width.RSH_PRE_EDGE_REDUCTION");
      	nodeList.add("rm_finish_width.RSH_CAL_EDGE_REDUCTION");
      	nodeList.add("rm_finish_width.RSH_PRE_EDGE_LOAD");
      	nodeList.add("rm_finish_width.RSH_REAL_EDGE_LOAD");
      	nodeList.add("rm_finish_width.RSH_OBJ_SET");
      	nodeList.add("rm_finish_width.RSH_OBJ_REAL");
      	nodeList.add("rm_finish_width.RSH_REAL_SET");
      	
      	nodeList.add("rm_slab.RSB_BAR_OBJ_THICKNESS");
      	nodeList.add("rm_slab.RSB_BAR_REAL_THICKNESS");
      	nodeList.add("rm_slab.RSB_BAR_REAL_WIDTH");
      	nodeList.add("rm_slab.RSB_BAR_REAL_LENGTH");
      	nodeList.add("rm_slab.RSB_UPPER_ROLL_DIA");
      	nodeList.add("rm_slab.RSB_UNDER_ROLL_DIA"); 
      	 
      	nodeList.add("temperature.TRE_RM_IN");
      	nodeList.add("temperature.TRE_RM_OUT"); 
      	
      	nodeList.add("time.T_TIME_RM_IN");
      	nodeList.add("time.T_TIME_RM_OUT");
 
          	nodeList.add("fm_slab.FSB_OUTER_THICKNESS_DIFF_3P");
      	nodeList.add("fm_slab.FSB_OUTER_THICKNESS_DIFF_CEN");
      	nodeList.add("fm_slab.FSB_CROWN");
      	nodeList.add("fm_slab.FSB_INNER_THICKNESS_DIFF");
      	nodeList.add("fm_slab.FSB_UPPER_ROLL_DIA");
      	nodeList.add("fm_slab.FSB_UNDER_ROLL_DIA");  
      	 
      	nodeList.add("temperature.TRE_FM_IN");
      	nodeList.add("temperature.TRE_FM_OUT");
      	nodeList.add("temperature.TRE_FM_IN_OBJ");
      	nodeList.add("temperature.TRE_FM_OUT_OBJ");
      	
      	nodeList.add("time.T_TIME_FM_IN");
      	nodeList.add("time.T_TIME_FM_OUT"); 

 
    	return nodeList;
    }
    
    public MTheory setRules_test(MTheory m) {  
    	parentMNodes.clear();		 
    	childMNode = "quality_result.QR_RESULT_COL_1"; 
    	parentMNodes.add("fm_slab.FSB_OUTER_THICKNESS_DIFF_3P");  
		m.addParents(childMNode, parentMNodes);
		
        return m;
    } 
    
    /*
     *  1. Ideal Model
     * 
     * 	[Proc1] -o [Proc2]	-o	[Proc3]	 
     *    \           |           / 
     *     o          o          o 
     *      [     Result Y      ]
     * 
     */    
    public MTheory setRules_Ideal_Model(MTheory m, int sizeRMPass, int sizeFMPass, int target_quality) {  
    	        
    	setChildWindowSize(m, "rm_pass.pass_no", sizeRMPass);
    	setChildWindowSize(m, "fm_pass.pass_no", sizeFMPass);
        	
        System.out.println(m.toString(new String[]{"MFrag", "MNode"}));
        
        List<String> childList = initDynamicNodeList_IdealModel(m, sizeRMPass, sizeFMPass); 
        
        //  [Proc2]
        //     |
        //     o
        // [Result Y]
    	parentMNodes.clear();
        for (String n : childList) { 
	    	parentMNodes.add(n); 
        }         
        
        childMNode = "quality_result.QR_RESULT_COL_" + target_quality; 
        m.addParents(childMNode, parentMNodes);
                
        // To do: [Proc1] -o [Proc2]	-o	[Proc3]	 
         
        return m;
    }
    
    /*
     * 2. V Model 
     * 
     * 	[Proc1]		[Proc2]		[Proc3]	
     *    \           |           /
     *     o          o          o 
     *      [     Result Y      ]
     * 
     */
    public MTheory setRules_V_Model_heavy(MTheory m, int sizeRMPass, int sizeFMPass, int target_quality) { 
        
    	setChildWindowSize(m, "rm_pass.pass_no", sizeRMPass);
    	setChildWindowSize(m, "fm_pass.pass_no", sizeFMPass);
        	
        System.out.println(m.toString(new String[]{"MFrag", "MNode"}));
        
        List<String> nodeList = initDynamicNodeList(sizeRMPass, sizeFMPass); 
        
    	parentMNodes.clear();
        for (String n : nodeList) { 
	    	parentMNodes.add(n); 
        } 
        
        childMNode = "quality_result.QR_RESULT_COL_" + target_quality; 
        m.addParents(childMNode, parentMNodes);
    	 		 
        return m;
    } 
    
    public MTheory setRules_V_Model_light(MTheory m, int target_quality) {  
        List<String> nodeList = initStaticNodeList(); 
        
    	parentMNodes.clear();
        for (String n : nodeList) { 
	    	parentMNodes.add(n); 
        } 
        
        childMNode = "quality_result.QR_RESULT_COL_" + target_quality; 
        m.addParents(childMNode, parentMNodes); 
        
        return m;
    } 
     
    /*
     *  3. Naive Model
     *   
     *      [     Result Y      ]
     *      /         |         \
     *     o          o          o 
     * 	[Proc1]		[Proc2]		[Proc3]	
     *      
     */
    public MTheory setRules_naive_heavy(MTheory m, int sizeRMPass, int sizeFMPass, int target_quality) {  
        
    	setChildWindowSize(m, "rm_pass.pass_no", sizeRMPass);
    	setChildWindowSize(m, "fm_pass.pass_no", sizeFMPass);
        
        List<String> childList = initDynamicNodeList(sizeRMPass, sizeFMPass); 
        
        for (String n : childList) {
	    	parentMNodes.clear();		
	    	parentMNodes.add("quality_result.QR_RESULT_COL_" + target_quality); 
	    	childMNode = n; 
	    	m.addParents(childMNode, parentMNodes);
        } 
				
        return m;
    }
    
    public MTheory setRules_naive_light(MTheory m) {   
        List<String> childList = initStaticNodeList(); 
        
        for (String n : childList) {
	    	parentMNodes.clear();		
	    	parentMNodes.add("quality_result.QR_RESULT_COL_1");
	    	childMNode = n; 
	    	m.addParents(childMNode, parentMNodes);
        } 
				
        return m;
    }
    
    /*
     *  4. Tree Augmented Naive Model
     *   
     *      [     Result Y      ]
     *      /         |         \
     *     o          o          o 
     * 	[Proc1] -o [Proc2]	-o [Proc3]	
     *      
     */
    public MTheory setRules_tree_augmented_naive_heavy(MTheory m, int sizeRMPass, int sizeFMPass, int target_quality) {  
        
    	setChildWindowSize(m, "rm_pass.pass_no", sizeRMPass);
    	setChildWindowSize(m, "fm_pass.pass_no", sizeFMPass);
        
        List<String> childList = initDynamicNodeList(sizeRMPass, sizeFMPass); 
        
        for (String n : childList) {
	    	parentMNodes.clear();		
	    	parentMNodes.add("quality_result.QR_RESULT_COL_1");
	    	childMNode = n; 
	    	m.addParents(childMNode, parentMNodes);
        } 
				
        return m;
    }
    

    // If this MFrag is a timed MFrag, a special timed table is created and it is used for joining 
    /*
     * 	select 
     * 		SLAB_NO, 
	 *  	sum(A) as A,
	 * 		sum(B) as B,
	 * 		sum(C) as C,  
	 *      sum(A1) as A1,
	 *     	sum(B1) as B1,
	 *      sum(C1) as C1 
	 *	from 
	 *		( select
	 * 			t2.*,
	 *			case when mod(row_num, 3) = 1 then RM_PRE_WK end as A, 
	 *			case when mod(row_num, 3) = 2 then RM_PRE_WK end as B,
	 *			case when mod(row_num, 3) = 0 then RM_PRE_WK end as C, 
	 *
	 *          case when mod(row_num, 3) = 1 then RM_ACT_WK end as A1, 
	 *        	case when mod(row_num, 3) = 2 then RM_ACT_WK end as B1,
	 *          case when mod(row_num, 3) = 0 then RM_ACT_WK end as C1, 
	 *          
	 *			case when row_num / 3 > 1     then FLOOR ((row_num -1) / 3) end as flag_for_windows  
	 * 		from (
	 *	 		select t.*, 
	 *		  		( case SLAB_NO when @curtype then @curRow := @curRow + 1 
	 *											 else @curRow := 1 and @curType := SLAB_NO end
	 *		  		) + 1 as row_num
	 *			from rm_pass t,
	 *		  		(select @curRow := 0, @curType := '') r
	 *			order BY SLAB_NO asc  
	 *	  		) t2
	 *		) t1 
 	 *		group by flag_for_windows, SLAB_NO 
     */ 
    public void createTimedTable(MFrag f) { 
	    if (!f.isTimedMFrag()) {
	    	return;
	    }
	    
	    int cw = f.childWindowSizeForTimedMFrag;
	    String sqlTimed = "";
	    sqlTimed += "select \r\n";
	    
	    ListMgr l = new ListMgr();
	    List<String> keyList = f.getKeysExceptX(f.timedPrimaryKey);
	    String keys = l.getListComma(keyList);
	    // To do: this version works only a single keys (e.g., [SLAB_NO]),
	    // but this version doens't work for a composite keys (e.g., [SLAB_NO, MACHINE_NO]).
	    // So, change the sqlTimed.
	    
	    sqlTimed += keys + ", \r\n";
	    
	    for (MNode n : f.arrayResidentNodes){
	    	for (int k = 1; k < (cw+1); k++) {
	    		sqlTimed += "sum(" + n.name +"_"+ k + " ) as " + n.name +"_"+ k + ", \r\n";
	    	}
	    } 
	    sqlTimed =  sqlTimed.substring(0, sqlTimed.length()-4);
 
	    sqlTimed += "\r\n from \r\n";
	    sqlTimed += "	( select \r\n";
	    sqlTimed += "	t2.*, \r\n";
	    
	    for (MNode n : f.arrayResidentNodes){
	    	int i = 0; 
	    	for (int k = 1; k < (cw+1); k++) {
	    		i++;
		    	if (i == cw) {
		    		i = 0;
		    	}
	    		sqlTimed += "case when mod(row_num, " + cw + " ) = " + i + " then " + n.attribute + " end as " + n.name +"_"+ k + ", \r\n";
	    	}
	    }
	    
	    sqlTimed += "case when row_num / " + cw + " > 1 then FLOOR ((row_num -1) / " + cw + " ) end as flag_for_windows \r\n";
	    sqlTimed += "from ( \r\n";
	    sqlTimed += "select t.*, \r\n";
	    sqlTimed += "( case " + keys + " when @curtype then @curRow := @curRow + 1 \r\n";
	    sqlTimed += "else @curRow := 1 and @curType := " + keys + " end \r\n";
	    sqlTimed += ") + 1 as row_num \r\n";
	    sqlTimed += "from " + f.name + " t, \r\n";
	    sqlTimed += "(select @curRow := 0, @curType := '') r \r\n";
	    sqlTimed += "order BY " + keys + " asc \r\n";
	    sqlTimed += ") t2 \r\n";
	    sqlTimed += ") t1 \r\n";
	    sqlTimed += "group by flag_for_windows, " + keys + " \r\n"; 
//	    System.out.println(sqlTimed);
	    f.joiningSQL = sqlTimed;
	  
    }
    public void setChildWindowSize(MTheory m, String timedPK, int childWindowSize) {
    	String mFrag = new StringUtil().getLeft(timedPK);
        String mPK = new StringUtil().getRight(timedPK);
        MFrag f = m.getMFrag(mFrag);
         
        if (childWindowSize <= 1 ){
    		return;
    	}  
        
        f.setTimedMFrag(childWindowSize, mPK);  
        
        createTimedTable(f);
         
        List<MNode> newList = new ArrayList<MNode>(f.arrayResidentNodes);
        
        for (MNode mn : newList) { 
        	// create new MNodes according to childWindowSize
        	for (int i = 1; i < childWindowSize; i++) {
        		MNode newMN = null;
                if (mn.isContinuous()) {
                	newMN = new MCNode(f, mn.name + "_" + (i+1), mn.ovs);
                } else if (mn.isDiscrete()) {
                	newMN = new MDNode(f, mn.name + "_" + (i+1), mn.ovs);
                }
                
                newMN.setAttributeName(mn.attribute);
                
                f.setMNode(newMN);
        	}
        	
        	mn.name += "_" + 1;
        }
        
    }
    
}

