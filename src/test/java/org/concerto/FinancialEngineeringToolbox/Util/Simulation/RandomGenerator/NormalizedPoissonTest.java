package org.concerto.FinancialEngineeringToolbox.Util.Simulation.RandomGenerator;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

class NormalizedPoissonTest {

    @Test
    void nextRandomVector() throws ParameterRangeErrorException {
        int N = 100000;
        double mean = 5;
        DescriptiveStatistics d = new DescriptiveStatistics();

        Generator g = new NormalizedPoisson(mean, N);

        double[] ret = g.nextRandomVector();

        for(double i : ret) {
            d.addValue(i);
        }
        assertEquals(mean, d.getMean(), ConstantForTest.EPSLION);
        assertEquals(5.00226, d.getVariance(), ConstantForTest.EPSLION);
        assertEquals(0.44710, d.getSkewness(), ConstantForTest.EPSLION);
        assertEquals(0.20790, d.getKurtosis(), ConstantForTest.EPSLION);
    }
}