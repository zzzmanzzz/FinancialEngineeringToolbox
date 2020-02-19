package org.concerto.FinancialEngineeringToolbox.Util;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NPVTest {

    @Test
    void getPresentValueOK() throws DimensionMismatchException {
        double[] inflow = {0, 25, 50, 75};
        double[] outflow = {100, 0, 0, 0};
        double[] rate = {0.09, 0.09, 0.09, 0.09};
        int N = 3;
        assertEquals(22.93354, NPV.getPresentValue(inflow, outflow, rate, N), ConstantForTest.EPSLION);
    }

    @Test
    void testGetPresentValueNTooLarge() {
        double[] inflow = {0, 25, 50, 75};
        double[] outflow = {100, 0, 0, 0};
        double[] rate = {0.09, 0.09, 0.09, 0.09};
        int N = 4;
        assertThrows(DimensionMismatchException.class, ()->NPV.getPresentValue(inflow, outflow, rate, N));
    }

    @Test
    void testGetPresentValue1() {
    }

    @Test
    void testGetPresentValue2() {
    }
}