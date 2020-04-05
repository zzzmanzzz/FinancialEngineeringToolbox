package org.concerto.FinancialEngineeringToolbox.Util.Simulation.TreeModel.TrinomialTree;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.concerto.FinancialEngineeringToolbox.Util.Simulation.TreeModel.Tree;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.concerto.FinancialEngineeringToolbox.Constant.OptionType.call;
import static org.concerto.FinancialEngineeringToolbox.Constant.OptionType.put;
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

    @Test
    void getBoyleEuropeanCallFairPrice() throws UndefinedParameterValueException {
        int N = 3;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute , false);
        canExecute[N] = true;
        Tree b = new Boyle(30, 29, 0.3, 0.05, N, 0.166666667);
        assertEquals(3.43258, b.getFairPrice(call, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getBoyleEuropeanPutFairPrice() throws UndefinedParameterValueException {
        int N = 3;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute , false);
        canExecute[N] = true;
        Tree b = new Boyle(30, 29, 0.3, 0.05, N, 0.166666667);
        assertEquals(1.71656, b.getFairPrice(put, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getLRAmericanCallFairPrice() throws UndefinedParameterValueException {
        int N = 3;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute, true);
        Tree b = new Boyle(30, 29, 0.3, 0.05, N, 0.166666667);
        assertEquals( 3.43258, b.getFairPrice(call, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getLRAmericanPutFairPrice() throws UndefinedParameterValueException {
        int N = 3;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute, true);
        Tree b = new Boyle(30, 29, 0.3, 0.05, N, 0.166666667);
        assertEquals( 1.73107, b.getFairPrice(put, canExecute), ConstantForTest.EPSLION);
    }

}