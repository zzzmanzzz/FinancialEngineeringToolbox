package org.concerto.FinancialEngineeringToolbox.Bond;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PerpetualTest {

    @Test
    void getInitialPrice() {
        Perpetual p = new Perpetual(0.05 , 100, 95);
        double initPrice = p.getFairPrice(0.05);
        assertEquals(100, initPrice, ConstantForTest.EPSLION);
    }

    @Test
    void getYTM() {
        Perpetual p = new Perpetual(0.05 , 100, 95);
        double ytm = p.getYTM();
        assertEquals(0.05263157894736842, ytm, ConstantForTest.EPSLION);
    }

    @Test
    void getMacaulayDuration() {
        Perpetual p = new Perpetual(0.05 , 100, 95);
        double MD = p.getMacaulayDuration(p.getYTM());
        assertEquals(20, MD, ConstantForTest.EPSLION);
    }

    @Test
    void getModifiedDuration() {
        Perpetual p = new Perpetual(0.05 , 100, 95);
        double MDD = p.getModifiedDuration(p.getYTM());
        assertEquals(19, MDD, ConstantForTest.EPSLION);
    }

    @Test
    void getEffectiveDuration() {
        Perpetual p = new Perpetual(0.05 , 100, 95);
        double ED = p.getEffectiveDuration(p.getYTM(), 0.0001);
        assertEquals(19.00006, ED, ConstantForTest.EPSLION);
    }

    @Test
    void getEffectiveConvexity() {
        Perpetual p = new Perpetual(0.05 , 100, 95);
        double EC = p.getEffectiveConvexity(p.getYTM(), 0.01);
        assertEquals(374.52017, EC, ConstantForTest.EPSLION);
    }
}