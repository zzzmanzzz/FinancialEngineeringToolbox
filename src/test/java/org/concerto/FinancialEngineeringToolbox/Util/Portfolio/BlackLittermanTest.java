package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import static org.concerto.FinancialEngineeringToolbox.Util.Portfolio.BlackLitterman.getBLMeanReturn;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.concerto.FinancialEngineeringToolbox.Constant.ReturnType;
import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.junit.jupiter.api.Test;


class BlackLittermanTest extends LoadData {
    //BABA, GOOG, AAPL, RRC, BAC, GM, JPM, SHLD, PFE, T, UAA, MA, SBUX, XOM, AMD, BBY, FB, AMZN, GE, WMT";
    final static double[] marketCap = {533e9, 927e9, 1.19e12, 1e9, 301e9, 51e9, 422e9, 0, 212e9, 61e9, 78e9, 288e9, 102e9, 295e9, 43e9, 22e9, 574e9, 867e9, 96e9, 339e9};
    final static double tau = 0.05;
    final static double riskFreeRate = 0.02;
    final static double marketReturn = 0.10845 / ConstantForTest.TRADINGDAYS;
    final static double makertVariance = 0.03294 / ConstantForTest.TRADINGDAYS;

    @Test
    void getOmega() throws ParameterIsNullException, UndefinedParameterValueException {
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, ConstantForTest.TRADINGDAYS);
        double[][] cov = ef.getCovariance(ReturnType.common);
        double[][] Q = new double[1][cov.length];
        Arrays.fill(Q[0], 0.1);
        double[][] Omega = BlackLitterman.getOmega(cov, Q, tau);
        assertEquals(0.00512, Omega[0][0], ConstantForTest.EPSLION) ;
    }

    @Test
    void getMarketImpliedRiskAversion() {
        double riskAversion = BlackLitterman.getMarketImpliedRiskAversion(marketReturn, makertVariance, riskFreeRate, ConstantForTest.TRADINGDAYS);
        assertEquals(2.68518, riskAversion, ConstantForTest.EPSLION);
    }

    @Test
    void getPriorReturnTest() throws UndefinedParameterValueException, ParameterIsNullException {
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, ConstantForTest.TRADINGDAYS);
        double riskAversion = BlackLitterman.getMarketImpliedRiskAversion(marketReturn, makertVariance, riskFreeRate, ConstantForTest.TRADINGDAYS);
        double[][] cov = ef.getCovariance(ReturnType.common);
        double[] priorReturn = BlackLitterman.getPriorReturns(cov, riskAversion, riskFreeRate, marketCap, ConstantForTest.TRADINGDAYS);
        double[] expect = {0.10361, 0.10000, 0.09580, 0.06722, 0.09228, 0.073358, 0.08320, 0.06864, 0.05879, 0.04820, 0.09590, 0.08430, 0.07178, 0.06229, 0.10629, 0.06710, 0.10196, 0.11329, 0.06459, 0.052570};
        assertArrayEquals(expect, priorReturn, ConstantForTest.EPSLION);
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

        double riskAversion = BlackLitterman.getMarketImpliedRiskAversion(marketReturn, makertVariance, riskFreeRate, ConstantForTest.TRADINGDAYS);
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, ConstantForTest.TRADINGDAYS);
        double[][] cov = ef.getCovariance(ReturnType.common);
        double[] priorReturn = BlackLitterman.getPriorReturns(cov, riskAversion, riskFreeRate, marketCap, ConstantForTest.TRADINGDAYS);
        double[][] Omega = BlackLitterman.getOmega(cov, Q, tau);

        double[] BLReturn = getBLMeanReturn( priorReturn, cov, P, Q, Omega, tau);
        double[] expect = {0.02160, 0.07264, 0.05494, -0.00068, 0.02099, -0.06406, 0.02196, -0.02237, 0.02598, 0.02267, 0.02001, 0.04138, 0.03471, 0.02514, 0.01384, 0.00729, 0.06273, 0.08145, 0.01540, 0.02552};
        //logger.info(Arrays.toString(BLReturn));
        assertArrayEquals(expect, BLReturn, ConstantForTest.EPSLION);
    }

    @Test
    void getBLCovariance() throws ParameterIsNullException, UndefinedParameterValueException {
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
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, ConstantForTest.TRADINGDAYS);
        double[][] cov = ef.getCovariance(ReturnType.common);
        double[][] Omega = BlackLitterman.getOmega(cov, Q, tau);
        double[][] BLcov = BlackLitterman.getBLCovariance(cov, Q, Omega, tau);
        assertEquals(true, MatrixUtils.isSymmetric(new Array2DRowRealMatrix(BLcov), ConstantForTest.EPSLION));
    }

    @Test
    void NoUncertainty() throws ParameterIsNullException, UndefinedParameterValueException {
        double riskAversion = BlackLitterman.getMarketImpliedRiskAversion(marketReturn, makertVariance, riskFreeRate, ConstantForTest.TRADINGDAYS);
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, ConstantForTest.TRADINGDAYS);
        double[][] cov = ef.getCovariance(ReturnType.common);
        double[] priorReturn = BlackLitterman.getPriorReturns(cov, riskAversion, riskFreeRate, marketCap, ConstantForTest.TRADINGDAYS);
        double[][] Q = {
            {0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {-1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0.5, 0, 0, 0, 0, 0, 0, -0.5, -0.5, 0, 0, 0, 0, 0, 0, 0.5, 0, 0},
        };
        double[] P = {0.2, 0.1, 0.05};
        double[][] Omega = new double[P.length][P.length];
        double[] BLReturn = getBLMeanReturn( priorReturn, cov, P, Q, Omega, tau);

        //logger.info(Arrays.toString(BLReturn));

    }

}