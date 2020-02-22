package org.concerto.FinancialEngineeringToolbox.Bond;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class VanillaTest {

    @Test
    void getYTM() {
        Vanilla v = new Vanilla(1000000, 0.0125, 6, 99711);
        assertEquals(0.529975, v.getYTM(), ConstantForTest.EPSLION);
    }

    @Test
    void getInitialPrice() {
        Vanilla v = new Vanilla(1000, 0.08, 5, 0);
        assertEquals(924.1842646118307, v.getFairPrice(0.1), ConstantForTest.EPSLION);
    }

    @Test
    void getMacaulayDuration() {
        Vanilla v = new Vanilla(1000, 0.1, 3, 1000);
        assertEquals(2.72867, v.getMacaulayDuration(0.12), ConstantForTest.EPSLION);
    }

    @Test
    void getModifiedDuration() {
        Vanilla v = new Vanilla(1000, 0.1, 3, 1000);
        assertEquals(2.43631, v.getModifiedDuration(0.12), ConstantForTest.EPSLION);
    }
    
    @Test
    void getEffectiveDuration() {
        Vanilla v = new Vanilla(1000, 0.1, 3, 1000);
        assertEquals(2.43631, v.getEffectiveDuration(0.12), ConstantForTest.EPSLION);
    }

    @Test
    void getCashFlow() {
        Vanilla v = new Vanilla(1000, 0.1, 3, 1000);
        double[] expectInflow = {0, 100, 100, 1100};
        double[] expectOutflow = {1000, 0, 0, 0};
        Map<String, double[]> cashFlow = v.getCashFlow();
        assertArrayEquals(expectInflow, cashFlow.get("inflow"));
        assertArrayEquals(expectOutflow, cashFlow.get("outflow"));
    }
}