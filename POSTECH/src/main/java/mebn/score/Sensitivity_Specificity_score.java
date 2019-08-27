/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  hmlp.rs.cps.datastructure.EDBUnit
 */
package mebn.score;
 
import java.io.PrintStream;

import edu.gmu.seor.prognos.unbbayesplugin.cps.datastructure.EDBUnit;

public class Sensitivity_Specificity_score {
    public Double TruePositive = 0.0;
    public Double FalseNegative = 0.0;
    public Double FalsePositive = 0.0;
    public Double TrueNegative = 0.0;

    public void run(EDBUnit predictiveBel, Double threshold, String target) {
        EDBUnit belNext = predictiveBel.getNext();
        int i = 0;
        while (i < predictiveBel.getNextSize()) {
            String state = belNext.getName();
            Double prob = belNext.getDataByDouble();
            if (state.equalsIgnoreCase(target) && prob >= threshold) {
                this.TruePositive = this.TruePositive + 1.0;
            } else if (!state.equalsIgnoreCase(target) && prob < threshold) {
                this.TrueNegative = this.TrueNegative + 1.0;
            } else if (state.equalsIgnoreCase(target) && prob < threshold) {
                this.FalseNegative = this.FalseNegative + 1.0;
            } else if (!state.equalsIgnoreCase(target) && prob >= threshold) {
                this.FalsePositive = this.FalsePositive + 1.0;
            }
            belNext = belNext.getNext();
            ++i;
        }
        System.out.println("TruePositive: " + this.TruePositive);
        System.out.println("FalseNegative: " + this.FalseNegative);
        System.out.println("FalsePositive: " + this.FalsePositive);
        System.out.println("TrueNegative: " + this.TrueNegative);
    }

    public String toString() {
        String s = "";
        s = String.valueOf(s) + "TruePositive: " + this.TruePositive + "\n";
        s = String.valueOf(s) + "FalseNegative: " + this.FalseNegative + "\n";
        s = String.valueOf(s) + "FalsePositive: " + this.FalsePositive + "\n";
        s = String.valueOf(s) + "TrueNegative: " + this.TrueNegative + "\n";
        return s;
    }

    public Double getSensitivity() {
        return this.TruePositive / (this.TruePositive + this.FalseNegative);
    }

    public Double getSpecificity() {
        return this.TrueNegative / (this.TrueNegative + this.FalsePositive);
    }

    public static void main(String[] args) {
        Sensitivity_Specificity_score t = new Sensitivity_Specificity_score();
        EDBUnit e = new EDBUnit();
        EDBUnit a1 = e.createNext("a1");
        a1.setData("0.3");
        EDBUnit a2 = a1.createNext("a2");
        a2.setData("0.2");
        EDBUnit a3 = a1.createNext("a3");
        a3.setData("0.5");
        t.run(e, 0.5, "a2");
    }
}

