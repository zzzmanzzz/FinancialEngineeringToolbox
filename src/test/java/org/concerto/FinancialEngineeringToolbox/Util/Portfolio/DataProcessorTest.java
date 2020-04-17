package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DataProcessorTest extends LoadData {

    @Test
    void parsePSuccess() throws DimensionMismatchException, ParameterIsNullException {
        double[][] P = {
                {0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {-1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0.5, 0, 0, 0, 0, 0, 0, -0.5, -0.5, 0, 0, 0, 0, 0, 0, 0.5, 0, 0},
        };
        double[] Q = {0.2, 0.1, 0.05};
        Map<String, double[]> p = generateP();
        double[][] res = DataProcessor.parseP(p, Q, data);
        assertEquals(Arrays.deepToString(P), Arrays.deepToString(res));
    }

    @Test
    void parsePDataAndPMismatch() {
        Map<String, double[]> p = generateP();
        double[] Q = {0.2, 0.1, 0.05};
        p.remove("GM");
        assertThrows(DimensionMismatchException.class, ()-> DataProcessor.parseP(p, Q, data));
    }

    @Test
    void parsePInnerArraySizeMismatch() {
        double[] Q = {0.2, 0.1, 0.05};
        Map<String, double[]> p = generateP();
        p.put("GM", new double[]{0, 0});
        assertThrows(DimensionMismatchException.class, ()-> DataProcessor.parseP(p, Q, data));
    }

}