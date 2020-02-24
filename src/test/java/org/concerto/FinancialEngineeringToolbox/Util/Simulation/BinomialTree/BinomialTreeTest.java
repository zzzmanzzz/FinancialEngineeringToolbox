package org.concerto.FinancialEngineeringToolbox.Util.Simulation.BinomialTree;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class BinomialTreeTest {


    @Test
    void getCRREuropeanCallFairPrice() {
        int N = 1000;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute , false);
        canExecute[N] = true;
        CoxRossRubinstein crr = new CoxRossRubinstein(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(18.350952 ,crr.getFairPrice("call", canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getCRREuropeanPutFairPrice() {
        int N = 1000;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute , false);
        canExecute[N] = true;
        CoxRossRubinstein crr = new CoxRossRubinstein(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(16.784775 ,crr.getFairPrice("put", canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getJREuropeanCallFairPrice() {
        int N = 1000;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute , false);
        canExecute[N] = true;
        JarrowRudd jr = new JarrowRudd(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(18.343573,jr.getFairPrice("call", canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getJREuropeanPutFairPrice() {
        int N = 1000;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute , false);
        canExecute[N] = true;
        JarrowRudd jr = new JarrowRudd(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(16.777730,jr.getFairPrice("put", canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getCRRAmericanCRRCallFairPrice() {
        int N = 3;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute, true);
        CoxRossRubinstein crr = new CoxRossRubinstein(10, 10, 0.2, 0.02, N, 0.25);
        assertEquals( 3.46377 ,crr.getFairPrice("call", canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getCRRAmericanCRRPutFairPrice() {
        int N = 3;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute, true);
        CoxRossRubinstein crr = new CoxRossRubinstein(10, 10, 0.2, 0.02, N, 0.25);
        assertEquals(0.35151 ,crr.getFairPrice("put", canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getCRRAmericanJRECallFairPrice() {
        int N = 3;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute, true);
        JarrowRudd jre = new JarrowRudd(10, 10, 0.2, 0.02, N, 0.25);
        assertEquals( 3.46377 ,jre.getFairPrice("call", canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getCRRAmericanJREPutFairPrice() {
        int N = 3;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute, true);
        JarrowRudd jre = new JarrowRudd(10, 10, 0.2, 0.02, N, 0.25);
        assertEquals(0.35154 ,jre.getFairPrice("put", canExecute), ConstantForTest.EPSLION);
    }

}