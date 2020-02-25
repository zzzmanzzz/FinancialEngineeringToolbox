package org.concerto.FinancialEngineeringToolbox.Util.Sequence;

import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;

public class Profile {
    private double[] sequence;
    private double mean;
    private double stdDev;
    private long size;
    private double skew;
    private double kurtosis;
    private double min;
    private double max;

    public double getMean() {
        return mean;
    }

    public double getStdDev() {
        return stdDev;
    }

    public long getSize() {
        return size;
    }

    public double getSkew() {
        return skew;
    }

    public double getKurtosis() {
        return kurtosis;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }


    public Profile(double[] sequence) {
        this.sequence = sequence;
        init();
    }

    public double[] getSequence() {
        return sequence;
    }

    public void setSequence(double[] sequence) {
        this.sequence = sequence;
        init();
    }

    public double KolmogorovSmirnovTest(RealDistribution distribution) {
        KolmogorovSmirnovTest k = new KolmogorovSmirnovTest();
        return k.kolmogorovSmirnovTest(distribution, sequence);
    }

    private void init() {
        DescriptiveStatistics ds = new DescriptiveStatistics();
        for(double i : sequence) {
            ds.addValue(i);
        }
        min = ds.getMin();
        max = ds.getMax();
        mean = ds.getMean();
        stdDev = ds.getStandardDeviation();
        size = ds.getN();
        skew = ds.getSkewness();
        kurtosis = ds.getKurtosis();
    }


}
