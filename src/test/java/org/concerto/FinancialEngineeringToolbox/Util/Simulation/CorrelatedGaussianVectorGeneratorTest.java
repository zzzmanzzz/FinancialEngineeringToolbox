package org.concerto.FinancialEngineeringToolbox.Util.Simulation;

import org.apache.commons.math3.stat.correlation.Covariance;
import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorrelatedGaussianVectorGeneratorTest {

    @Test
    void nextRandomVector() throws ParameterRangeErrorException {
        double[][] cov = {{1, 0.6}, {0.6, 1}};
        int N = 10000;
        double[][] vecs = new double[2][N];
        CorrelatedGaussianVectorGenerator CGVG = new CorrelatedGaussianVectorGenerator(cov, 123);
        for(int i = 0 ; i < N ; i++) {
            double[] tmp = CGVG.nextRandomVector();
            vecs[0][i] = tmp[0];
            vecs[1][i] = tmp[1];
        }

        Covariance covariance = new Covariance();
        double ret = covariance.covariance(vecs[0], vecs[1]);

        assertEquals(0.6, ret, 1e-3);

    }

    @Test
    void nextManyRandomVector() throws ParameterRangeErrorException {
        double[][] cov = {{1, 0.6}, {0.6, 1}};
        int N = 10000;
        CorrelatedGaussianVectorGenerator CGVG = new CorrelatedGaussianVectorGenerator(cov, 123);

        double[][] vecs = CGVG.nextRandomVector(N);
        Covariance covariance = new Covariance();
        double ret = covariance.covariance(vecs[0], vecs[1]);

        assertEquals(0.6, ret, 1e-3);

    }

    @Test
    void nextManyRandomVectorThrowsParameterRangeErrorException() {
        double[][] cov = {{1}, {1}};
        assertThrows(ParameterRangeErrorException.class, ()-> new CorrelatedGaussianVectorGenerator(cov, 123) );
    }
}