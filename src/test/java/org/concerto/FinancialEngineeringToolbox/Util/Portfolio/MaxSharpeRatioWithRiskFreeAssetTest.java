package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MaxSharpeRatioWithRiskFreeAssetTest extends LoadData {
    final static double tau = 0.05;
    final static double riskFreeRate = 0.02;
    final static double marketReturn = 0.10845;
    final static double marketVariance = 0.03294;

    @Test
    void geOptimizeResult() throws ParameterIsNullException, UndefinedParameterValueException {
        MaxSharpeRatioWithRiskFreeAsset mpv = new MaxSharpeRatioWithRiskFreeAsset(data, riskFreeRate, Constant.TRADINGDAYS);
        Result ret = mpv.getMarkowitzOptimizeResult(data, riskFreeRate, Constant.ReturnType.common, ConstantForTest.TRADINGDAYS);

        assertEquals(2.71645, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(1.33719, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.23512, ret.getPortfolioVariance(), Constant.EPSILON);
    }

    @Test
    void getBlackLittermanOptimizeResult() throws ParameterIsNullException, UndefinedParameterValueException, DimensionMismatchException {
        double[] Q = {0.2, 0.1, 0.05};
        Map<String, double[]> P = generateP();

        Map<String, Double> marketCap = generateMarketCap();
        MaxSharpeRatioWithRiskFreeAsset mpv = new MaxSharpeRatioWithRiskFreeAsset(data, riskFreeRate, Constant.TRADINGDAYS);
        Result res = mpv.getBlackLittermanOptimizeResult(P, marketCap, Q, tau, marketReturn, marketVariance, Constant.ReturnType.common);

        assertEquals(0.50705, res.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.18725, res.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.10881, res.getPortfolioVariance(), Constant.EPSILON);
    }

    @Test
    void getBlackLittermanOptimizeResultWithOmega() throws ParameterIsNullException, DateFormatException, UndefinedParameterValueException, DimensionMismatchException, ParameterRangeErrorException {
        double[] Q = {0.2, 0.1, 0.05};
        Map<String, double[]> P = generateP();
        double[] omega = {0.1, 0.3, 0.5};

        Map<String, Double> marketCap = generateMarketCap();
        MaxSharpeRatioWithRiskFreeAsset mpv = new MaxSharpeRatioWithRiskFreeAsset(data, riskFreeRate, Constant.TRADINGDAYS);
        Result res = mpv.getBlackLittermanOptimizeResult(P, marketCap, Q, omega, tau, marketReturn, marketVariance, Constant.ReturnType.common);

        assertEquals(0.40547, res.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.07900, res.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.02117, res.getPortfolioVariance(), Constant.EPSILON);
    }
}