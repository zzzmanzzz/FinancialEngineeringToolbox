package org.concerto.FinancialEngineeringToolbox.Util.Simulation.TreeModel.BinomialTree;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.concerto.FinancialEngineeringToolbox.Util.Simulation.TreeModel.Tree;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.concerto.FinancialEngineeringToolbox.Constant.OptionType.call;
import static org.concerto.FinancialEngineeringToolbox.Constant.OptionType.put;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JarrowRuddTest {

    @Test
    void getJREuropeanCallFairPrice() throws UndefinedParameterValueException {
        int N = 1000;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute , false);
        canExecute[N] = true;
        Tree jr = new JarrowRudd(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(18.343573,jr.getFairPrice(call, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getJREuropeanPutFairPrice() throws UndefinedParameterValueException {
        int N = 1000;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute , false);
        canExecute[N] = true;
        Tree jr = new JarrowRudd(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(16.777730,jr.getFairPrice(put, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getJREAmericanCallFairPrice() throws UndefinedParameterValueException {
        int N = 3;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute, true);
        Tree jre = new JarrowRudd(10, 10, 0.2, 0.02, N, 0.25);
        assertEquals( 0.81933 ,jre.getFairPrice(call, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getJREAmericanPutFairPrice() throws UndefinedParameterValueException {
        int N = 3;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute, true);
        Tree jre = new JarrowRudd(10, 10, 0.2, 0.02, N, 0.25);
        assertEquals(0.68302 ,jre.getFairPrice(put, canExecute), ConstantForTest.EPSLION);
    }

}