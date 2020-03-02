package org.concerto.FinancialEngineeringToolbox.Util.Simulation.RandomGenerator;

import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;

import java.util.stream.IntStream;

public class NormalizedPoisson implements Generator {
    private IntegerDistribution poisson;
    private double mean;
    private int dimension = 0;
    static final private Mean m = new Mean();

    private void init(double mean, int dimension, int randomSeed) throws ParameterRangeErrorException {
        if(dimension < 1) {
            String msg = String.format("Dimension(%d) should >= 1");
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
        double [] tmp = IntStream.range(0, dimension).mapToDouble(
            i -> poisson.sample()
        ).toArray();

        double bias = m.evaluate(tmp, 0 , tmp.length) - this.mean;
        return IntStream.range( 0, dimension).mapToDouble(
            i -> tmp[i] - bias
        ).toArray();
    }
}
