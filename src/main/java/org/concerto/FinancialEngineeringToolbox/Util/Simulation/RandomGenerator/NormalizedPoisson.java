package org.concerto.FinancialEngineeringToolbox.Util.Simulation.RandomGenerator;

import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;

public class NormalizedPoisson implements Generator {
    private IntegerDistribution poisson;
    private double mean;
    private int dimension = 0;
    static final private Mean m = new Mean();

    private void init(double mean, int dimension, int randomSeed) throws ParameterRangeErrorException {
        if(dimension < 1) {
            String msg = "Dimension(%d) should >= 1";
            throw new ParameterRangeErrorException(msg, null);
        }
        poisson = new PoissonDistribution(new MersenneTwister(randomSeed), mean, PoissonDistribution.DEFAULT_EPSILON, PoissonDistribution.DEFAULT_MAX_ITERATIONS);
        this.dimension = dimension;
        this.mean = mean;
    }

    public NormalizedPoisson(double mean, int dimension) throws ParameterRangeErrorException {
        init(mean, dimension, Constant.RANDOMSEED);
    }

    public NormalizedPoisson(double mean, int dimension, int randomSeed) throws ParameterRangeErrorException {
        init(mean, dimension,randomSeed);
    }

    @Override
    public double[] nextRandomVector() {
        double [] ret = new double[dimension];
        for(int i = 0 ; i < dimension ; i++) {
            ret[i] = poisson.sample();
        }
        double bias = m.evaluate(ret, 0 , ret.length) - this.mean;
        for(int i = 0 ; i < dimension ; i++) {
            ret[i] = ret[i] - bias;
        }
        return ret;
    }
}
