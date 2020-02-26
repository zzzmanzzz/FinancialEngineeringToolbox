package org.concerto.FinancialEngineeringToolbox.Util.Simulation;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.CorrelatedRandomVectorGenerator;
import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.MersenneTwister;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;

public class CorrelatedGaussianVectorGenerator {
    private CorrelatedRandomVectorGenerator RG;

    public CorrelatedGaussianVectorGenerator(double[][] covariance, int randomSeed) {
        RealMatrix cov = MatrixUtils.createRealMatrix(covariance);

        RG = new CorrelatedRandomVectorGenerator(cov, 1.0e-12 * cov.getNorm(), new GaussianRandomGenerator(new MersenneTwister(randomSeed)));
    }

    public CorrelatedGaussianVectorGenerator(double[][] covariance, GaussianRandomGenerator G) {
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
        return ret;
    }


}
