package org.concerto.FinancialEngineeringToolbox.Util.Simulation.RandomGenerator;

import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.NormalizedRandomGenerator;
import org.apache.commons.math3.random.UncorrelatedRandomVectorGenerator;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;

public class NormalizedGaussian implements Generator {
    private UncorrelatedRandomVectorGenerator random;
    static final private Mean m = new Mean();
    static final private StandardDeviation std = new StandardDeviation();

    private void init(int dimension, int randomSeed) throws ParameterRangeErrorException {
        if(dimension < 1) {
            String msg = String.format("Dimension(%d) should >= 1");
            throw new ParameterRangeErrorException(msg, null);
        }
        NormalizedRandomGenerator nr = new GaussianRandomGenerator(new MersenneTwister(randomSeed));
        random = new UncorrelatedRandomVectorGenerator(dimension, nr);
    }

    public NormalizedGaussian(int dimension) throws ParameterRangeErrorException {
        init(dimension, Constant.RANDOMSEED);
    }

    public NormalizedGaussian(int dimension, int randomSeed) throws ParameterRangeErrorException {
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
