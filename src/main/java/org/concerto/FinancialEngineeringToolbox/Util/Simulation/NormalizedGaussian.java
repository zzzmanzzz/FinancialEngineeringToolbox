package org.concerto.FinancialEngineeringToolbox.Util.Simulation;

import org.apache.commons.math3.random.*;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.concerto.FinancialEngineeringToolbox.Constant;

public class NormalizedGaussian {
    private UncorrelatedRandomVectorGenerator random;

    private void init(int dimension, int randomSeed) {
        NormalizedRandomGenerator nr = new GaussianRandomGenerator(new MersenneTwister(randomSeed));
        random = new UncorrelatedRandomVectorGenerator(dimension, nr);
    }

    public NormalizedGaussian(int dimension) {
        init(dimension, Constant.RANDOMSEED);
    }

    public NormalizedGaussian(int dimension, int randomSeed) {
        init(dimension, randomSeed);
    }

    public double[] nextRandomVector() {
        double[] ret = random.nextVector();
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (double i : ret) {
           stats.addValue(i);
        }
        double mean = stats.getMean();
        double stdDev = stats.getStandardDeviation();

        for(int i = 0 ; i < ret.length; i++ ) {
            ret[i] = (ret[i] - mean) / stdDev;
        }
        return ret;
    }
}
