package org.concerto.FinancialEngineeringToolbox.Util.Simulation;

import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.junit.jupiter.api.Test;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;


import static org.junit.jupiter.api.Assertions.*;

class GBMTest {

    @Test
    void staticSimulate() throws ParameterRangeErrorException {
        double S0 = 100;
        double riskFreeRate = 0.05;
        double sigma = 0.25;
        double T = 2.0;
        int times = 10000;
        double[] priceT = GBM.staticSimulate(S0, sigma, T, riskFreeRate, times);
        DescriptiveStatistics ds = new DescriptiveStatistics();
        for (double i : priceT) {
            ds.addValue(i);
        }

        assertEquals(110.5327, ds.getMean(), 0.5);
        assertEquals(40.51795, ds.getStandardDeviation(), 0.5);
        assertEquals(1.167, ds.getSkewness(), 0.5);
    }

    @Test
    void staticSimulateTimesError() {
        double S0 = 100;
        double riskFreeRate = 0.05;
        double sigma = 0.25;
        double T = 2.0;
        int times = -10000;
        assertThrows(ParameterRangeErrorException.class, ()-> GBM.staticSimulate(S0, sigma, T, riskFreeRate, times));
    }

    @Test
    void dynamicSimulate() throws ParameterRangeErrorException {
        double S0 = 100;
        double riskFreeRate = 0.05;
        double sigma = 0.25;
        int times = 1000;
        double deltaT = 0.02; // 0.02 yr
        int steps = 50; //simulation steps
        double[][] priceT = GBM.dynamicSimulate(S0, sigma, riskFreeRate, times, deltaT, steps);
        assertEquals(S0, priceT[0][0]);

        DescriptiveStatistics ds = new DescriptiveStatistics();
        for (double i : priceT[steps - 1]) {
            ds.addValue(i);
        }

        assertEquals(105.04348, ds.getMean(), 0.5);
        assertEquals(27.1867, ds.getStandardDeviation(), 0.5);
        assertEquals(1.167, ds.getSkewness(), 0.5);

    }

    @Test
    void dynamicSimulateTimesError() {
        double S0 = 100;
        double riskFreeRate = 0.05;
        double sigma = 0.25;
        int times = -1000;
        double deltaT = 0.02; // 0.02 yr
        int steps = 50; //simulation steps
        assertThrows(ParameterRangeErrorException.class, ()->GBM.dynamicSimulate(S0, sigma, riskFreeRate, times, deltaT, steps));
    }

    @Test
    void dynamicSimulateStepsError() {
        double S0 = 100;
        double riskFreeRate = 0.05;
        double sigma = 0.25;
        int times = 1000;
        double deltaT = 0.02; // 0.02 yr
        int steps = -50; //simulation steps
        assertThrows(ParameterRangeErrorException.class, ()->GBM.dynamicSimulate(S0, sigma, riskFreeRate, times, deltaT, steps));
    }

    @Test
    void dynamicSimulateDeltaTError() {
        double S0 = 100;
        double riskFreeRate = 0.05;
        double sigma = 0.25;
        int times = 1000;
        double deltaT = -0.02; // 0.02 yr
        int steps = 50; //simulation steps
        assertThrows(ParameterRangeErrorException.class, ()->GBM.dynamicSimulate(S0, sigma, riskFreeRate, times, deltaT, steps));
    }
}