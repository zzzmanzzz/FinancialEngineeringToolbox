package org.concerto.FinancialEngineeringToolbox.Util.Simulation;

import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.NormalizedRandomGenerator;
import org.apache.commons.math3.random.UncorrelatedRandomVectorGenerator;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.concerto.FinancialEngineeringToolbox.Constant;

public class NormalizedGaussian {
    private UncorrelatedRandomVectorGenerator random;

    private void init(int dimension, int randomSeed) {
        NormalizedRandomGenerator nr = new GaussianRandomGenerator(new JDKRandomGenerator(randomSeed)) ;
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

        for(int i = 0 ; i < ret.length; i++ ) {
            ret[i] = (ret[i] - stats.getMean()) / stats.getStandardDeviation();
        }
        return ret;
    }
}
