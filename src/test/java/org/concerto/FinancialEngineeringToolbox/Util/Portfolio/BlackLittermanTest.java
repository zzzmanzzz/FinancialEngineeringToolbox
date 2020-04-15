package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import static org.concerto.FinancialEngineeringToolbox.Util.Portfolio.BlackLitterman.getBLMeanReturn;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import org.concerto.FinancialEngineeringToolbox.Constant.ReturnType;
import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.junit.jupiter.api.Test;


class BlackLittermanTest extends LoadData {
    @Test
    void getOmega() throws ParameterIsNullException, UndefinedParameterValueException {
        double riskFreeRate = 0.02;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, ConstantForTest.TRADINGDAYS);
        double[][] cov = ef.getCovariance(ReturnType.common);
        double[][] Q = new double[1][cov.length];
        Arrays.fill(Q[0], 0.1);
        double tau = 0.05;
        double[][] Omega = BlackLitterman.getOmega(cov, Q, tau);
        assertEquals(0.00512, Omega[0][0], ConstantForTest.EPSLION) ;
    }

    @Test
    void getMarketImpliedRiskAversion() {
    }

    @Test
    void getPriorReturnTest() {

    }

    @Test
    void getBLMeanReturnTest() throws ParameterIsNullException, UndefinedParameterValueException {
        //BABA, GOOG, AAPL, RRC, BAC, GM, JPM, SHLD, PFE, T, UAA, MA, SBUX, XOM, AMD, BBY, FB, AMZN, GE, WMT";
        //GM drop 20%
        //GOOG outperforms BABA by 10%
        //AMZN and AAPL will outperform T and UAA 5%
        double[][] Q = {
             {0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
             {-1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
             {0, 0, 0.5, 0, 0, 0, 0, 0, 0, -0.5, -0.5, 0, 0, 0, 0, 0, 0, 0.5, 0, 0},
        };
        double[] P = {0.2, 0.1, 0.05};
        double[] marketCap = {533e9, 927e9, 1.19e12, 1e9, 301e9, 51e9, 422e9, 0, 212e9, 61e9, 78e9, 288e9, 102e9, 295e9, 43e9, 22e9, 574e9, 867e9, 96e9, 339e9};
        double tau = 0.05;
        double riskFreeRate = 0.02;
        double riskAversion = BlackLitterman.getMarketImpliedRiskAversion(0.03, 0.01, riskFreeRate, ConstantForTest.TRADINGDAYS);
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, ConstantForTest.TRADINGDAYS);
        double[][] cov = ef.getCovariance(ReturnType.common);
        double[] priorReturn = BlackLitterman.getPriorReturns(cov, riskAversion, riskFreeRate, marketCap, ConstantForTest.TRADINGDAYS);
        double[][] Omega = BlackLitterman.getOmega(cov, Q, tau);
        double[] BLReturn = getBLMeanReturn( priorReturn, cov, P, Q, Omega, tau);
        double[] expect = {0.01405, 0.06204, 0.04355, -0.01097, 0.01316, -0.07023, 0.01264, -0.02958, 0.01234, 0.00827, 0.01949, 0.03031, 0.02234, 0.01218, 0.00748, -0.00355, 0.05322, 0.07043, 0.00378, 0.01106};

        assertArrayEquals(expect, BLReturn, ConstantForTest.EPSLION);
    }

    @Test
    void getBLCovariance() {
    }



    @Test
    void testGetBLCovariance() {
    }
}