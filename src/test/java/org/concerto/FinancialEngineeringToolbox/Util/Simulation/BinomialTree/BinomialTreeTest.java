package org.concerto.FinancialEngineeringToolbox.Util.Simulation.BinomialTree;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.concerto.FinancialEngineeringToolbox.Constant.OptionType.call;
import static org.concerto.FinancialEngineeringToolbox.Constant.OptionType.put;
import static org.junit.jupiter.api.Assertions.*;

class BinomialTreeTest {


    @Test
    void getCRREuropeanCallFairPrice() throws UndefinedParameterValueException {
        int N = 1000;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute , false);
        canExecute[N] = true;
        CoxRossRubinstein crr = new CoxRossRubinstein(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(18.350952 ,crr.getFairPrice(call, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getCRREuropeanPutFairPrice() throws UndefinedParameterValueException {
        int N = 1000;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute , false);
        canExecute[N] = true;
        CoxRossRubinstein crr = new CoxRossRubinstein(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(16.784775 ,crr.getFairPrice(put, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getJREuropeanCallFairPrice() throws UndefinedParameterValueException {
        int N = 1000;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute , false);
        canExecute[N] = true;
        JarrowRudd jr = new JarrowRudd(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(18.343573,jr.getFairPrice(call, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getJREuropeanPutFairPrice() throws UndefinedParameterValueException {
        int N = 1000;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute , false);
        canExecute[N] = true;
        JarrowRudd jr = new JarrowRudd(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(16.777730,jr.getFairPrice(put, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getLREuropeanCallFairPrice() throws UndefinedParameterValueException {
        int N = 1000;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute , false);
        canExecute[N] = true;
        LeisenReimer lr = new LeisenReimer(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(18.33056,lr.getFairPrice(call, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getLREuropeanPutFairPrice() throws UndefinedParameterValueException {
        int N = 1000;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute , false);
        canExecute[N] = true;
        LeisenReimer lr = new LeisenReimer(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(16.76439,lr.getFairPrice(put, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getCRRAmericanCallFairPrice() throws UndefinedParameterValueException {
        int N = 3;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute, true);
        CoxRossRubinstein crr = new CoxRossRubinstein(10, 10, 0.2, 0.02, N, 0.25);
        assertEquals( 3.46377 ,crr.getFairPrice(call, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getCRRAmericanPutFairPrice() throws UndefinedParameterValueException {
        int N = 3;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute, true);
        CoxRossRubinstein crr = new CoxRossRubinstein(10, 10, 0.2, 0.02, N, 0.25);
        assertEquals(0.35151 ,crr.getFairPrice(put, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getJREAmericanCallFairPrice() throws UndefinedParameterValueException {
        int N = 3;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute, true);
        JarrowRudd jre = new JarrowRudd(10, 10, 0.2, 0.02, N, 0.25);
        assertEquals( 3.46377 ,jre.getFairPrice(call, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getJREAmericanPutFairPrice() throws UndefinedParameterValueException {
        int N = 3;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute, true);
        JarrowRudd jre = new JarrowRudd(10, 10, 0.2, 0.02, N, 0.25);
        assertEquals(0.35154 ,jre.getFairPrice(put, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getLRAmericanCallFairPrice() throws UndefinedParameterValueException {
        int N = 3;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute, true);
        LeisenReimer lr = new LeisenReimer(10, 10, 0.2, 0.02, N, 0.25);
        assertEquals( 3.17156 ,lr.getFairPrice(call, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getLRAmericanPutFairPrice() throws UndefinedParameterValueException {
        int N = 3;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute, true);
        LeisenReimer lr = new LeisenReimer(10, 10, 0.2, 0.02, N, 0.25);
        assertEquals(0.31618 ,lr.getFairPrice(put, canExecute), ConstantForTest.EPSLION);
    }
}