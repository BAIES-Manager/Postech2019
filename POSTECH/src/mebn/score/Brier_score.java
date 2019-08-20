/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  hmlp.rs.cps.datastructure.EDBUnit
 */
package mebn.score;

import hmlp.rs.cps.datastructure.EDBUnit;
import java.io.PrintStream;
import java.util.ArrayList;

public class Brier_score {
    public Double BrierScore = 0.0;

    public Double run(EDBUnit predictiveBel, Double threshold, String truth) {
        EDBUnit belNext = predictiveBel.getNext();
        ArrayList<Double> exactProbs = new ArrayList<Double>();
        int i = 0;
        while (i < predictiveBel.getNextSize()) {
            double BrierCons = 0.0;
            String state = belNext.getName();
            Double prob = belNext.getDataByDouble();
            exactProbs.add(prob);
            BrierCons = truth.equalsIgnoreCase(state) ? 1.0 : 0.0;
            this.BrierScore = this.BrierScore + (BrierCons - prob) * (BrierCons - prob);
            belNext = belNext.getNext();
            ++i;
        }
        System.out.println("BrierScore: " + this.BrierScore);
        return this.BrierScore;
    }

    public String toString() {
        String s = "";
        s = String.valueOf(s) + " BrierScore: " + this.BrierScore + "\n";
        return s;
    }

    public static void main(String[] args) {
        Brier_score t = new Brier_score();
        EDBUnit e = new EDBUnit();
        EDBUnit a1 = e.createNext("Others");
        a1.setData("0.10377964006708103");
        EDBUnit a2 = a1.createNext("Attack");
        a2.setData("0.896220359932919");
        t.run(e, 0.5, "Attack");
    }
}

