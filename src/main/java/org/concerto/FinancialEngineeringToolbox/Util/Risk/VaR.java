package org.concerto.FinancialEngineeringToolbox.Util.Risk;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

import java.util.Arrays;

public class VaR {

    public static double[] getValueAtRisk(double[] data, double[] confidenceLevel) {
        double[] ret = new double[confidenceLevel.length];
        Percentile p = new Percentile();

        for(int i = 0 ; i < ret.length ; i++) {
            double left =  100 - confidenceLevel[i];
            ret[i] = p.evaluate(data, left);
        }
        return  ret;
    }

    public static double[] getConditionalValueAtRisk(double[] data, double[] confidenceLevel) {
        double[] ret = new double[confidenceLevel.length];
        double[] tmp = getValueAtRisk(data, confidenceLevel);
        DescriptiveStatistics ds = new DescriptiveStatistics();
        Arrays.sort(data);

        for(int i = 0 ; i < tmp.length ; i++) {
            for(double j : data) {
                if(j < tmp[i]) {
                    ds.addValue(j);
                } else {
                    break;
                }
            }
            ret[i] = ds.getMean();
            ds.clear();
        }
        return  ret;
    }
}
