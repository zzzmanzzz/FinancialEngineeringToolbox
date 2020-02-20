package org.concerto.FinancialEngineeringToolbox.Util.Simulation;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.junit.jupiter.api.Test;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;


import static org.junit.jupiter.api.Assertions.*;

class GBMTest {

    @Test
    void staticPriceSimulation() {
        double S0 = 100;
        double riskFreeRate = 0.05;
        double sigma = 0.25;
        double T = 2.0;
        int times = 10000;
        double[] priceT = GBM.staticPriceSimulation(S0, sigma, T, riskFreeRate, times);
        DescriptiveStatistics ds = new DescriptiveStatistics();
        for (double i : priceT) {
            ds.addValue(i);
        }

        assertEquals(110.5327, ds.getMean(), 0.5);
        assertEquals(40.51795, ds.getStandardDeviation(), 0.5);
        assertEquals(1.167, ds.getSkewness(), 0.5);
    }
}