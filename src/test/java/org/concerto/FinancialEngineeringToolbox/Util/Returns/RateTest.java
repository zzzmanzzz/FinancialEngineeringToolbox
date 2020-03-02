package org.concerto.FinancialEngineeringToolbox.Util.Returns;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RateTest {

    @Test
    void getDiscount() {
        double facePrice = 1000;
        double price = 980;
        double ret = Rate.getDiscount(facePrice, price);
        assertEquals(0.02, ret, ConstantForTest.EPSLION);
    }

    @Test
    void getBankDiscountYield() throws ParameterRangeErrorException {
        double facePrice = 1000;
        double price = 980;
        int days = 180;
        double ret = Rate.getBankDiscountYield(facePrice, price, days);
        assertEquals(0.04, ret, ConstantForTest.EPSLION);
    }

    @Test
    void getBankDiscountYieldDaysError() {
        double facePrice = 1000;
        double price = 980;
        int days = -180;
        assertThrows(ParameterRangeErrorException.class, () -> Rate.getBankDiscountYield(facePrice, price, days));
    }

    @Test
    void getMoneyMarketYield() throws ParameterRangeErrorException {
        double facePrice = 1000;
        double price = 980;
        int days = 180;
        double ret = Rate.getMoneyMarketYield(facePrice, price, days);
        assertEquals(0.040816, ret, ConstantForTest.EPSLION);
    }

    @Test
    void getMoneyMarketYieldError() {
        double facePrice = 1000;
        double price = 980;
        int days = -180;
        assertThrows(ParameterRangeErrorException.class, () -> Rate.getMoneyMarketYield(facePrice, price, days));
    }

    @Test
    void getBondEquivalentYield() throws ParameterRangeErrorException {
        double facePrice = 1000;
        double price = 980;
        int days = 180;
        double ret = Rate.getBondEquivalentYield(facePrice, price, days);
        assertEquals(0.041383, ret, ConstantForTest.EPSLION);
    }

    @Test
    void getBondEquivalentYieldError() {
        double facePrice = 1000;
        double price = 980;
        int days = -180;
        assertThrows(ParameterRangeErrorException.class, () -> Rate.getBankDiscountYield(facePrice, price, days));
    }

    @Test
    void getEffectiveAnnualRate() throws ParameterRangeErrorException {
        double nominalAnnualRate = 0.05;
        int frequency = 2; //in one year
        double ret = Rate.getEffectiveAnnualRate(nominalAnnualRate, frequency);
        assertEquals(0.05062, ret, ConstantForTest.EPSLION);
    }

    @Test
    void getEffectiveAnnualRateError() {
        double nominalAnnualRate = 0.05;
        int frequency = -2;
        assertThrows(ParameterRangeErrorException.class, () ->  Rate.getEffectiveAnnualRate(nominalAnnualRate, frequency));
    }

    @Test
    void getCommonReturn() throws ParameterIsNullException {
        double[] price = {1.0, 0.8, 0.64};
        double[] ret = Rate.getCommonReturn(price);
        double[] exp = {-0.2, -0.2};
        assertArrayEquals(exp, ret, ConstantForTest.EPSLION);
    }

    @Test
    void getCommonReturnOneValueInput() throws ParameterIsNullException {
        double[] price = {1.0};
        double[] ret = Rate.getCommonReturn(price);
        double[] exp = new double[0];
        assertArrayEquals(exp, ret, ConstantForTest.EPSLION);
    }

    @Test
    void getLogReturn() throws ParameterIsNullException {
        double[] price = {1.0, 0.8, 0.64};
        double[] ret = Rate.getLogReturn(price);
        double[] exp = {-0.22314, -0.22314};
        assertArrayEquals(exp, ret, ConstantForTest.EPSLION);
    }

    @Test
    void getLogReturnOneValueInput() throws ParameterIsNullException {
        double[] price = {1.0};
        double[] ret = Rate.getLogReturn(price);
        double[] exp = new double[0];
        assertArrayEquals(exp, ret, ConstantForTest.EPSLION);
    }
}