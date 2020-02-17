package org.concerto.FinancialEngineeringToolbox.Util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlackScholesTest {

    @org.junit.jupiter.api.Test
    void d_1() {
        BlackScholes b = new BlackScholes(100, 100,0.6, 0.06, 1, 0);
        assertEquals(0.4, b.d_1(), 1e-10);
    }

    @org.junit.jupiter.api.Test
    void d_2() {
        BlackScholes b = new BlackScholes(100, 100,0.6, 0.06, 1, 0);
        assertEquals(-0.2, b.d_2(), 1e-10);
    }

    @org.junit.jupiter.api.Test
    void getCallPrice() {
        BlackScholes b = new BlackScholes(100, 100,0.6, 0.06, 1, 0);
        assertEquals(25.918345811013978, b.getCallPrice(), 1e-10);
    }

    @org.junit.jupiter.api.Test
    void getPutPrice() {
        BlackScholes b = new BlackScholes(100, 100,0.6, 0.06, 1, 0);
        assertEquals(20.09479916943885, b.getPutPrice(), 1e-10);
    }

    @org.junit.jupiter.api.Test
    void getCallDelta() {
        BlackScholes b = new BlackScholes(43, 40,0.1414, 0.05, 1, 0);
        assertEquals(0.8253037591080123, b.getCallDelta(), 1e-10);
    }

    @org.junit.jupiter.api.Test
    void getPutDelta() {
        BlackScholes b = new BlackScholes(43, 40,0.1414, 0.05, 1, 0);
        assertEquals(0.1746962408919876, b.getPutDelta(), 1e-10);
    }

    @org.junit.jupiter.api.Test
    void getGamma() {
        BlackScholes b = new BlackScholes(43, 40,0.1414, 0.05, 1, 0);
        assertEquals(0.042349134063950106, b.getGamma(), 1e-10);
    }

    @org.junit.jupiter.api.Test
    void getCallRho() {
    }

    @org.junit.jupiter.api.Test
    void getPutRho() {
    }

    @org.junit.jupiter.api.Test
    void getVega() {
    }

    @org.junit.jupiter.api.Test
    void getCallTheta() {
    }

    @org.junit.jupiter.api.Test
    void getPutTheta() {
    }
}