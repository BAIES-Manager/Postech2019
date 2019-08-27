/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  mebn_rm.util.NormalDistribution
 */
package mebn.score;
 
import mebn_rm.util.NormalDistribution;

public class Continuous_ranked_probability_score {
    public static double crps = 0.0;

    public static final double run(Double mean, Double variance, Double y) {
        double prev = 0.0;
        double cur = 0.0;
        double tic = 0.01;
        double gap = Math.abs(mean-y);
//        double negative = (- Math.sqrt(variance)) * 0.6;
//        double positive = Math.sqrt(variance) * 0.6;
        double negative = - 1 * gap;
        double positive = 1 * gap;
        
        crps = 0.0;
        NormalDistribution norm = new NormalDistribution(mean.doubleValue(), variance.doubleValue());
       // System.out.println("mean: " + mean + " var: " + variance + " truth value: " + y);
        double t = negative + mean;
        while (t < positive + mean) {
            double H = 0.0;
            H = t < y ? 0.0 : 1.0;
            double f = norm.getCDF(t);
            cur = (f - H) * (f - H);
            crps += (prev + cur) * tic * 0.5; // Integral
            prev = cur;
            t += tic;  
        }	
        System.out.println("mean: " + mean + " var: " + variance + " truth value: " + y + " crps:	" + crps);
        return crps;
    }

    public String toString() {
        String s = "";
        s = String.valueOf(s) + " crps: " + crps + "\n";
        return s;
    }

    public static void main(String[] args) {
//        Continuous_ranked_probability_score t = new Continuous_ranked_probability_score();
//        Double mean = 0.0;
//        Double STD = 0.0;
//        Double y = 0.0;
//        mean = 20.0;
//        STD = 0.1;
//        y = 0.0;
//        
//        for (Double i = 0.0; i < 40; i++){
//        	Continuous_ranked_probability_score.run(mean, STD, y + i);
//        } 
        
//        Continuous_ranked_probability_score t = new Continuous_ranked_probability_score();
//        Double mean = 0.0;
//        Double STD = 0.0;
//        Double y = 0.0;
//        mean = 0.0;
//        STD = 100.0;
//        y = 20.0;
//        
//        for (Double i = 0.0; i < 40; i++){
//        	Continuous_ranked_probability_score.run(mean + i, STD, y);
//        } 
    	
    	Continuous_ranked_probability_score t = new Continuous_ranked_probability_score();
    	Continuous_ranked_probability_score.run(71142.0, 2520.0, 71209.0);
    	    	
    }
}

