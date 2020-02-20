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

        assertEquals(103.82119, ds.getMean(), ConstantForTest.EPSLION);
    }
}