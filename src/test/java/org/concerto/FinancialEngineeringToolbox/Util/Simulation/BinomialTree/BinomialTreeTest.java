package org.concerto.FinancialEngineeringToolbox.Util.Simulation.BinomialTree;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BinomialTreeTest {

    @Test
    void getCRREuropeanCallFairPrice() {
        CoxRossRubinstein crr = new CoxRossRubinstein(100, 110, 0.3, 0.05, 1000, 0.002221918);
        assertEquals(18.350952 ,crr.getFairPrice("call", null), ConstantForTest.EPSLION);
    }

    @Test
    void getCRREuropeanPutFairPrice() {
        CoxRossRubinstein crr = new CoxRossRubinstein(100, 110, 0.3, 0.05, 1000, 0.002221918);
        assertEquals(16.784775 ,crr.getFairPrice("put", null), ConstantForTest.EPSLION);
    }

    @Test
    void getJREuropeanCallFairPrice() {
        JarrowRudd jr = new JarrowRudd(100, 110, 0.3, 0.05, 1000, 0.002221918);
        assertEquals(18.343573,jr.getFairPrice("call", null), ConstantForTest.EPSLION);
    }

    @Test
    void getJREuropeanPutFairPrice() {
        JarrowRudd jr = new JarrowRudd(100, 110, 0.3, 0.05, 1000, 0.002221918);
        assertEquals(16.777730,jr.getFairPrice("put", null), ConstantForTest.EPSLION);
    }
}