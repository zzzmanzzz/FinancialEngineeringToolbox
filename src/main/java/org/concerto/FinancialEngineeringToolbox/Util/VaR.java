package org.concerto.FinancialEngineeringToolbox.Util;

import org.apache.commons.math3.stat.descriptive.rank.Percentile;

public class VaR {
    private static Percentile p = new Percentile();

    public static double[] getValueAtRisk(double[] data, double[] confidenceLevel) {
        double[] ret = new double[confidenceLevel.length];

        for(int i = 0 ; i < ret.length ; i++) {
            double left =  100 - confidenceLevel[i];
            ret[i] = p.evaluate(data, left);
        }
        return  ret;
    }
}
