package org.concerto.FinancialEngineeringToolbox.Util.Simulation.TreeModel.TrinomialTree;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Util.Simulation.TreeModel.Tree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoyleTest {
    @Test
    void getDiscount() {
        int N = 1000;
        Tree b = new Boyle(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(0.99988 ,b.getDiscount(), ConstantForTest.EPSLION);
    }

    @Test
    void getU() {
        int N = 1000;
        Tree b = new Boyle(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(1.02019, b.getU(), ConstantForTest.EPSLION);
    }

    @Test
    void getD() {
        int N = 1000;
        Tree b = new Boyle(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(0.98020, b.getD(), ConstantForTest.EPSLION);
    }

    @Test
    void getProbabilityUp() {
        int N = 1000;
        Tree b = new Boyle(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(0.25027, b.getProbabilityUp(), ConstantForTest.EPSLION);
    }

    @Test
    void getProbabilityDown() {
        int N = 1000;
        Tree b = new Boyle(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(0.24972, b.getProbabilityDown(), ConstantForTest.EPSLION);
    }

}