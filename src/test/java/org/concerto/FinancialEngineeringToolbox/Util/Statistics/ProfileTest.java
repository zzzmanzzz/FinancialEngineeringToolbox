package org.concerto.FinancialEngineeringToolbox.Util.Statistics;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {
    Profile p;
    static long size = 10000;

    @BeforeEach
    void init() {
        NormalDistribution n = new NormalDistribution(new MersenneTwister(123), 0, 1);
        double[] random = new double[(int)size];
        for(int i = 0 ; i < size ; i++) {
           random[i] = n.sample();
        }
        p = new Profile(random);
    }

    @Test
    void getStdDev() {
        assertEquals(1.00618, p.getStdDev(), ConstantForTest.EPSLION);
    }

    @Test
    void getSize() {
        assertEquals(size, p.getSize(), ConstantForTest.EPSLION);
    }

    @Test
    void getSkew() {
        assertEquals(0.01235, p.getSkew(), ConstantForTest.EPSLION);
    }

    @Test
    void getKurtosis() {
        assertEquals(0.01229, p.getKurtosis(), ConstantForTest.EPSLION);
    }

    @Test
    void kolmogorovSmirnovTest() {
        NormalDistribution n = new NormalDistribution(new MersenneTwister(123), 0, 1);
        assertEquals(true, p.KolmogorovSmirnovTest(n) > 0.05);
    }
}