package org.concerto.FinancialEngineeringToolbox.Util;

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
}