package org.concerto.FinancialEngineeringToolbox.Util.Simulation.TreeModel.BinomialTree;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.concerto.FinancialEngineeringToolbox.Util.Simulation.TreeModel.Tree;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.concerto.FinancialEngineeringToolbox.Constant.OptionType.call;
import static org.concerto.FinancialEngineeringToolbox.Constant.OptionType.put;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CoxRossRubinsteinTest {

    @Test
    void getDiscount() {
        int N = 1000;
        Tree crr = new CoxRossRubinstein(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(0.99988 ,crr.getDiscount(), ConstantForTest.EPSLION);
    }

    @Test
    void getU() {
        int N = 1000;
        Tree crr = new CoxRossRubinstein(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(1.01424 ,crr.getU(), ConstantForTest.EPSLION);
    }

    @Test
    void getD() {
        int N = 1000;
        Tree crr = new CoxRossRubinstein(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(0.98595 ,crr.getD(), ConstantForTest.EPSLION);
    }

    @Test
    void getProbabilityUp() {
        int N = 1000;
        Tree crr = new CoxRossRubinstein(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(0.50039 ,crr.getProbabilityUp(), ConstantForTest.EPSLION);
    }

    @Test
    void getProbabilityDown() {
        int N = 1000;
        Tree crr = new CoxRossRubinstein(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(0.49961 ,crr.getProbabilityDown(), ConstantForTest.EPSLION);
    }

    @Test
    void getCRREuropeanCallFairPrice() throws UndefinedParameterValueException {
        int N = 1000;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute , false);
        canExecute[N] = true;
        Tree crr = new CoxRossRubinstein(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(18.350952 ,crr.getFairPrice(call, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getCRREuropeanPutFairPrice() throws UndefinedParameterValueException {
        int N = 1000;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute , false);
        canExecute[N] = true;
        Tree crr = new CoxRossRubinstein(100, 110, 0.3, 0.05, N, 0.002221918);
        assertEquals(16.784775 ,crr.getFairPrice(put, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getCRRAmericanCallFairPrice() throws UndefinedParameterValueException {
        int N = 3;
        double deltaT = 0.25 / N;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute, true);
        Tree crr = new CoxRossRubinstein(60, 60, 0.45, 0.1, N, deltaT);
        assertEquals( 6.52161 ,crr.getFairPrice(call, canExecute), ConstantForTest.EPSLION);
    }

    @Test
    void getCRRAmericanPutFairPrice() throws UndefinedParameterValueException {
        int N = 3;
        double deltaT = 0.25 / N;
        boolean[] canExecute = new boolean[N+1];
        Arrays.fill(canExecute, true);
        Tree crr = new CoxRossRubinstein(60, 60, 0.45, 0.1, N, deltaT);
        assertEquals(5.16278 ,crr.getFairPrice(put, canExecute), ConstantForTest.EPSLION);
    }

}