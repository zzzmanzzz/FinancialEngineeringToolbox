package org.concerto.FinancialEngineeringToolbox.Util.Simulation.MeanReverting;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SquareRootDiffusionDiscreteEulerTest {

    @Test
    void dynamicSimulate() throws ParameterRangeErrorException {
        double x0 = 0.05;
        double sigma = 0.1;
        int simulationNumber = 10000;
        int times = 50;
        double kappa = 3.0;
        double theta = 0.02;
        double deltaT = 0.04;

        double[][] rate = SquareRootDiffusionDiscreteEuler.dynamicSimulate(x0, sigma, kappa, theta, deltaT, times, simulationNumber);
        DescriptiveStatistics ds = new DescriptiveStatistics();
        for (double i : rate[rate.length-1]) {
            ds.addValue(i);
        }

        assertEquals(theta, ds.getMean(), 1e-4);
    }
}