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
        double tau = 0.05;
        double riskFreeRate = 0.02;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, ConstantForTest.TRADINGDAYS);
        double[][] cov = ef.getCovariance(ReturnType.common);
        double[] mean = ef.getMean(ReturnType.common);
        double[][] Omega = BlackLitterman.getOmega(cov, Q, tau);
        double[] BLReturn = getBLMeanReturn( mean, cov, P, Q, Omega, tau);
        double[] expect = {0.08872, 0.11509, 0.08952, -0.37762, 0.12434, -0.04421, 0.14694, -0.39056, 0.06006, 0.05891, -0.03564, 0.18341, 0.11103, -0.05166, 0.32400, 0.23177, 0.17332, 0.32931, -0.18812, 0.05331};
        assertArrayEquals(expect, BLReturn, ConstantForTest.EPSLION);
    }

    @Test
    void getBLCovariance() {
    }
}