package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DataProcessorTest extends LoadData {
    Map<String, double[]> generateP() {
        String[] symbles = {"BABA", "GOOG", "AAPL", "RRC", "BAC", "GM", "JPM", "SHLD", "PFE", "T", "UAA", "MA", "SBUX", "XOM", "AMD", "BBY", "FB", "AMZN", "GE", "WMT"};
        double[][] P = {
                {0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {-1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0.5, 0, 0, 0, 0, 0, 0, -0.5, -0.5, 0, 0, 0, 0, 0, 0, 0.5, 0, 0},
        };
        Map<String, double[]> p = new HashMap<>();
        for(int i = 0 ; i < symbles.length; i++) {
            double[] tmp = new double[P.length];
            for (int j = 0 ; j < P.length; j++ ) {
                tmp[j] = P[j][i];
            }
            p.put(symbles[i], tmp);
        }
        return p;
    }

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