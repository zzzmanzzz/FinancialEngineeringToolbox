package org.concerto.FinancialEngineeringToolbox.Util.Simulation;

import org.apache.commons.math3.random.*;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.concerto.FinancialEngineeringToolbox.Constant;

public class NormalizedGaussian {
    private UncorrelatedRandomVectorGenerator random;
    static final private Mean m = new Mean();
    static final private StandardDeviation std = new StandardDeviation();

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
        double mean = m.evaluate(ret, 0, ret.length);
        double stdDev = std.evaluate(ret, mean);

        for(int i = 0 ; i < ret.length; i++ ) {
            ret[i] = (ret[i] - mean) / stdDev;
        }
        return ret;
    }
}
