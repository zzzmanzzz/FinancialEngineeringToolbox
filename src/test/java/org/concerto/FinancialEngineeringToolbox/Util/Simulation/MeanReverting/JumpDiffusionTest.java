package org.concerto.FinancialEngineeringToolbox.Util.Simulation.MeanReverting;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class JumpDiffusionTest {

    @Test
    void dynamicSimulate() throws ParameterRangeErrorException {
        double S0 = 100;
        double sigma = 0.2;
        double lambda = 0.75;
        double riskFreeRate = 0.05;
        int simulationNumber = 10000;
        int times = 50;
        double mu = -0.6;
        double delta = 0.25;
        double deltaT = 0.02;

        double[][] ret = JumpDiffusion.dynamicSimulate(S0, sigma, lambda, riskFreeRate, mu, delta, deltaT, times, simulationNumber);

        List<Double> temp = new LinkedList<>();

        DoubleStream differences =
                IntStream.range(0, times - 1)
                        .mapToDouble(i -> {
                            temp.add(ret[i][0]);
                            return ret[i + 1][0] / ret[i][0] - 1;
                        });

        double minDiff = differences.min().getAsDouble();

        double[] lastTime  = ret[times - 1];

        assertEquals(S0, ret[0][0], ConstantForTest.EPSLION);
        assertEquals(-0.29804, minDiff, ConstantForTest.EPSLION);
        assertEquals(simulationNumber, lastTime.length);
        

    }
}