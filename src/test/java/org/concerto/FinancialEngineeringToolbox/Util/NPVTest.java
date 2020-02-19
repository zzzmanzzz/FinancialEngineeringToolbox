package org.concerto.FinancialEngineeringToolbox.Util;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;
import org.concerto.FinancialEngineeringToolbox.Exception.IndexOutOfRangeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NPVTest {

    @Test
    void getPresentValueOK() throws DimensionMismatchException, IndexOutOfRangeException {
        double[] inflow = {0, 25, 50, 75};
        double[] outflow = {100, 0, 0, 0};
        double[] rate = {0.09, 0.09, 0.09, 0.09};
        int from = 0;
        int to = 3;
        assertEquals(22.93354, NPV.getPresentValue(inflow, outflow, rate, 0, 3), ConstantForTest.EPSLION);
    }

    @Test
    void testGetPresentValueToOutOfRange() {
        double[] inflow = {0, 25, 50, 75};
        double[] outflow = {100, 0, 0, 0};
        double[] rate = {0.09, 0.09, 0.09, 0.09};
        int from = 0;
        int to = 4;
        assertThrows(IndexOutOfRangeException.class, ()->NPV.getPresentValue(inflow, outflow, rate, from, to));
    }

    @Test
    void testGetPresentValueFromOutOfRange() {
        double[] inflow = {0, 25, 50, 75};
        double[] outflow = {100, 0, 0, 0};
        double[] rate = {0.09, 0.09, 0.09, 0.09};
        int from = -1;
        int to = 3;
        assertThrows(IndexOutOfRangeException.class, ()->NPV.getPresentValue(inflow, outflow, rate, from, to));
    }

    @Test
    void testGetPresentValueArraySizeMismatch() {
        double[] inflow = {0, 25, 50, 75, 100};
        double[] outflow = {100, 0, 0, 0};
        double[] rate = {0.09, 0.09, 0.09, 0.09};
        int from = 0;
        int to = 3;
        assertThrows(DimensionMismatchException.class, ()->NPV.getPresentValue(inflow, outflow, rate, from, to));
    }

    @Test
    void testGetPresentValueFixInitialOutflow() throws IndexOutOfRangeException, DimensionMismatchException {
        double[] inflow = {0, 25, 50, 75};
        double[] rate = {0.09, 0.09, 0.09, 0.09};
        double outflow = 100;
        int from = 0;
        int to = 3;
        assertEquals(22.93354, NPV.getPresentValue(inflow, outflow, rate, from, to), ConstantForTest.EPSLION);
    }

    @Test
    void testGetPresentValueFixOutflowFixRate() throws IndexOutOfRangeException, DimensionMismatchException {
        double[] inflow = {0, 25, 50, 75};
        double rate = 0.09;
        double outflow = 100;
        int from = 0;
        int to = 3;
        assertEquals(22.93354, NPV.getPresentValue(inflow, outflow, rate, from, to), ConstantForTest.EPSLION);
    }

    @Test
    void testGetPresentValueVariantOutflowFixRate() throws IndexOutOfRangeException, DimensionMismatchException {
        double[] inflow = {0, 25, 50, 75};
        double rate = 0.09;
        double[] outflow = {100, 0, 0, 0};
        int from = 0;
        int to = 3;
        assertEquals(22.93354, NPV.getPresentValue(inflow, outflow, rate, from, to), ConstantForTest.EPSLION);
    }
}