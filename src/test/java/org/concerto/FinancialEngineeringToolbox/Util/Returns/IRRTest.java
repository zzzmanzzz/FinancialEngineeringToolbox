package org.concerto.FinancialEngineeringToolbox.Util.Returns;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;
import org.concerto.FinancialEngineeringToolbox.Util.Returns.IRR;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IRRTest {

    @Test
    void getIRR() throws DimensionMismatchException {
        double[] inflow = {0, 25, 50, 75};
        double[] outflow = {100, 0, 0, 0};
        assertEquals( 0.194377,
                IRR.getIRR(inflow, outflow),
                ConstantForTest.EPSLION);
    }

    @Test
    void getIRRArrayDimensionMisMatch() {
        double[] inflow = {0, 25, 50, 75, 100};
        double[] outflow = {100, 0, 0, 0};
        assertThrows(DimensionMismatchException.class, ()->IRR.getIRR(inflow, outflow));
    }
}