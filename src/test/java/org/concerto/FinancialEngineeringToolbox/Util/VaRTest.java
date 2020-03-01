package org.concerto.FinancialEngineeringToolbox.Util;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Util.Simulation.RandomGenerator.Generator;
import org.concerto.FinancialEngineeringToolbox.Util.Simulation.RandomGenerator.NormalizedGaussian;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VaRTest {

    @Test
    void getValueAtRisk() throws ParameterRangeErrorException {
        int N = 100000;
        Generator g  = new NormalizedGaussian(N, 123);
        final double[] confidenceLevel = {99.99, 99.90, 99.0, 97.5, 95.0, 90.0};
        final double[] data = g.nextRandomVector();


        double[] q = VaR.getValueAtRisk(data, confidenceLevel);

        assertEquals(-3.76827, q[0], ConstantForTest.EPSLION); // 99.99%
        assertEquals(-3.08952, q[1], ConstantForTest.EPSLION); // 99.90%
        assertEquals(-2.31974, q[2], ConstantForTest.EPSLION); // 99.0%
        assertEquals(-1.95536, q[3], ConstantForTest.EPSLION); // 97.5%
        assertEquals(-1.65019, q[4], ConstantForTest.EPSLION); // 95.0%
        assertEquals(-1.28177, q[5], ConstantForTest.EPSLION); // 90.0%
    }

}