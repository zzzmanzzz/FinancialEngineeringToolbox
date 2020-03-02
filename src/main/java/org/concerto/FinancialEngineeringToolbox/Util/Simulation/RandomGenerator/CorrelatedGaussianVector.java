package org.concerto.FinancialEngineeringToolbox.Util.Simulation.RandomGenerator;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.CorrelatedRandomVectorGenerator;
import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;

public class CorrelatedGaussianVector {
    private CorrelatedRandomVectorGenerator RG;
    static final private Mean m = new Mean();
    static final private StandardDeviation std = new StandardDeviation();

    public CorrelatedGaussianVector(double[][] covariance, int randomSeed) throws ParameterRangeErrorException {
        if(covariance[0].length < 2) {
            String msg = String.format("covariance dimension (%d) should >= 2", covariance[0].length);
            throw new ParameterRangeErrorException(msg, null);
        }
        RealMatrix cov = MatrixUtils.createRealMatrix(covariance);
        RG = new CorrelatedRandomVectorGenerator(cov, 1.0e-12 * cov.getNorm(), new GaussianRandomGenerator(new MersenneTwister(randomSeed)));
    }

    public CorrelatedGaussianVector(double[][] covariance, GaussianRandomGenerator G) throws ParameterRangeErrorException {
        if(covariance[0].length < 2) {
            String msg = String.format("covariance dimension (%d) should >= 2", covariance[0].length);
            throw new ParameterRangeErrorException(msg, null);
        }
        RealMatrix cov = MatrixUtils.createRealMatrix(covariance);
        RG = new CorrelatedRandomVectorGenerator(cov, 1.0e-12 * cov.getNorm(), G);
    }

    public double[] nextRandomVector() {
       return RG.nextVector();
    }

    public double[][] nextRandomVector(int simulationNumber) throws ParameterRangeErrorException {

        if(simulationNumber <= 0) {
            String msg = String.format("simulationNumber(%d) should > 0", simulationNumber);
            throw new ParameterRangeErrorException(msg, null);
        }

        double[][] ret = new double[RG.getRank()][simulationNumber];
        for (int i = 0 ; i < simulationNumber ; i++) {
            double[] tmp = RG.nextVector();
            for(int j = 0 ; j < tmp.length ; j++) {
                ret[j][i] = tmp[j];
            }
        }

        //normalize
        for(int i = 0 ; i < ret.length ; i++) {
            double mean = m.evaluate(ret[i],0,ret[i].length);
            double stdDev = std.evaluate(ret[i], mean);

            for(int j = 0 ; j < ret[i].length; j++ ) {
                ret[i][j] = (ret[i][j] - mean) / stdDev;
            }
        }

        return ret;
    }


}
