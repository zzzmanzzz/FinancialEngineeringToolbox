package org.concerto.FinancialEngineeringToolbox.Util.Simulation.RandomGenerator;

import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.NormalizedRandomGenerator;
import org.apache.commons.math3.random.UncorrelatedRandomVectorGenerator;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;

import java.util.LinkedList;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

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
        double[] tmp = random.nextVector();
        double mean = m.evaluate(tmp, 0, tmp.length);
        double stdDev = std.evaluate(tmp, mean);

        return IntStream.range(0, tmp.length).mapToDouble( i ->
             (tmp[i] - mean) / stdDev
        ).toArray();
    }
}
