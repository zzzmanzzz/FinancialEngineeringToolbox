package org.concerto.FinancialEngineeringToolbox.Util.Simulation.MeanReverting;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Util.Simulation.Result;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StochasticVolatilityTest {

    @Test
    void dynamicSimulate() throws ParameterRangeErrorException {
        double s0 = 100;
        double riskFreeRate = 0.05;
        double v0 = 0.1;
        double kappa = 3;
        double theta = 0.25;
        double sigma = 0.1;
        double rho = 0.6;
        double deltaT = 0.02;
        int simulateTime = 10000;
        int step = 50;

        Result r = StochasticVolatility.dynamicSimulate(s0, v0, sigma,riskFreeRate, kappa,theta,rho,deltaT,step,simulateTime, 123);

        DescriptiveStatistics d = new DescriptiveStatistics();

        double[][] violatility = r.getVolatility();
        double[][] indexLevel = r.getIndexLevel();

        for(double i : violatility[violatility.length-1]) {
            d.addValue(i);
        }

        assertEquals(0.24279, d.getMean(), ConstantForTest.EPSLION);
        assertEquals(simulateTime, d.getN(), ConstantForTest.EPSLION);
        assertEquals(0.02019, d.getStandardDeviation(), ConstantForTest.EPSLION);
        assertEquals(0.13492, d.getSkewness(), ConstantForTest.EPSLION);
        assertEquals(0.02963, d.getKurtosis(), ConstantForTest.EPSLION);

        d.clear();

        for(double i : indexLevel[indexLevel.length-1]) {
            d.addValue(i);
        }

        assertEquals(108.13978, d.getMean(), ConstantForTest.EPSLION);
        assertEquals(simulateTime, d.getN(), ConstantForTest.EPSLION);
        assertEquals(51.29415, d.getStandardDeviation(), ConstantForTest.EPSLION);
        assertEquals(1.59808, d.getSkewness(), ConstantForTest.EPSLION);
        assertEquals(4.93182, d.getKurtosis(), ConstantForTest.EPSLION);


    }
}