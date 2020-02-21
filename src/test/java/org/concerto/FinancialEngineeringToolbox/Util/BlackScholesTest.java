package org.concerto.FinancialEngineeringToolbox.Util;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlackScholesTest {

    @org.junit.jupiter.api.Test
    void d_1() {
        BlackScholes b = new BlackScholes(100, 100,0.6, 0.06, 1, 0);
        assertEquals(0.4, b.d_1(), ConstantForTest.EPSLION);
    }

    @org.junit.jupiter.api.Test
    void d_2() {
        BlackScholes b = new BlackScholes(100, 100,0.6, 0.06, 1, 0);
        assertEquals(-0.2, b.d_2(), ConstantForTest.EPSLION);
    }

    @org.junit.jupiter.api.Test
    void getCallPrice() {
        BlackScholes b = new BlackScholes(100, 100,0.6, 0.06, 1, 0);
        assertEquals(25.918345811013978, b.getCallPrice(), ConstantForTest.EPSLION);
    }

    @org.junit.jupiter.api.Test
    void getPutPrice() {
        BlackScholes b = new BlackScholes(100, 100,0.6, 0.06, 1, 0);
        assertEquals(20.09479916943885, b.getPutPrice(), ConstantForTest.EPSLION);
    }

    @org.junit.jupiter.api.Test
    void getCallDelta() {
        BlackScholes b = new BlackScholes(43, 40,0.1414, 0.05, 1, 0);
        assertEquals(0.8253037591080123, b.getCallDelta(), ConstantForTest.EPSLION);
    }

    @org.junit.jupiter.api.Test
    void getPutDelta() {
        BlackScholes b = new BlackScholes(43, 40,0.1414, 0.05, 1, 0);
        assertEquals(0.1746962408919876, b.getPutDelta(), ConstantForTest.EPSLION);
    }

    @org.junit.jupiter.api.Test
    void getGamma() {
        BlackScholes b = new BlackScholes(350, 340,0.27, 0.1, 0.5, 0.05);
        assertEquals( 0.00542, b.getGamma(), ConstantForTest.EPSLION);
    }

    @org.junit.jupiter.api.Test
    void getCallRho() {
        BlackScholes b = new BlackScholes(70, 73,0.15, 0.09, 0.75, 0);
        assertEquals(28.26769, b.getCallRho(), ConstantForTest.EPSLION);
    }

    @org.junit.jupiter.api.Test
    void getPutRho() {
        BlackScholes b = new BlackScholes(70, 73,0.15, 0.09, 0.75, 0);
        assertEquals(-22.90864, b.getPutRho(), ConstantForTest.EPSLION);
    }

    @org.junit.jupiter.api.Test
    void getVega() {
        BlackScholes b = new BlackScholes(350, 340,0.22, 0.06, 0.5, 0.02);
        assertEquals(90.4973, b.getVega(), ConstantForTest.EPSLION);
    }

    @org.junit.jupiter.api.Test
    void getCallTheta() {
        BlackScholes b = new BlackScholes(450, 430,0.22, 0.08, 0.0833, 0.06);
        assertEquals(-0.220257, b.getCallTheta(), ConstantForTest.EPSLION);
    }

    @org.junit.jupiter.api.Test
    void getPutTheta() {
        BlackScholes b = new BlackScholes(450, 430,0.22, 0.08, 0.0833, 0.06);
        assertEquals(-0.191265, b.getPutTheta(), ConstantForTest.EPSLION);
    }
}