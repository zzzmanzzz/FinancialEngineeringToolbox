package org.concerto.FinancialEngineeringToolbox.Util.Returns;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;
import org.concerto.FinancialEngineeringToolbox.Exception.IndexOutOfRangeException;
import org.concerto.FinancialEngineeringToolbox.Util.Returns.NPV;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NPVTest {

    @Test
    void getPresentValueOK1() throws DimensionMismatchException, IndexOutOfRangeException {
        double[] inflow = {0, 25, 50, 75};
        double[] outflow = {100, 0, 0, 0};
        double[] rate = {0.09, 0.09, 0.09, 0.09};
        int from = 0;
        int to = 3;
        assertEquals(22.93354, NPV.getPresentValue(inflow, outflow, rate, 0, 3), ConstantForTest.EPSLION);
    }

    @Test
    void getPresentValueOK2() throws DimensionMismatchException, IndexOutOfRangeException {
        double[] inflow = {0, 100, 100, 1100};
        int from = 0;
        int to = 3;
        assertEquals(951.96337, NPV.getPresentValue(inflow, 0, 0.12, 0, 3), ConstantForTest.EPSLION);
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