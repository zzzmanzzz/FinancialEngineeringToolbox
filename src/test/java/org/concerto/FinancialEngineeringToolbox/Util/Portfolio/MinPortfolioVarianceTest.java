package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.*;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinPortfolioVarianceTest extends LoadData {
    final static double tau = 0.05;
    final static double riskFreeRate = 0.02;
    final static double marketReturn = 0.10845;
    final static double marketVariance = 0.03294;

    @Test
    void getMarkowitzOptimizeResult() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException {
        MinPortfolioVariance mpv = new MinPortfolioVariance(data, riskFreeRate, Constant.TRADINGDAYS);
        Result ret = mpv.getMarkowitzOptimizeResult(Constant.ReturnType.common);

        assertEquals(0.52368, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.08342, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.01466, ret.getPortfolioVariance(), Constant.EPSILON);
    }

    @Test
    void getBlackLittermanOptimizeResult() throws ParameterIsNullException, UndefinedParameterValueException, DimensionMismatchException {
        double[] Q = {0.2, 0.1, 0.05};
        Map<String, double[]> P = generateP();

        Map<String, Double> marketCap = generateMarketCap();
        MinPortfolioVariance mpv = new MinPortfolioVariance(data, riskFreeRate, Constant.TRADINGDAYS);
        Result res = mpv.getBlackLittermanOptimizeResult(P, marketCap, Q, tau, marketReturn, marketVariance, Constant.ReturnType.common);

        assertEquals(0.05134, res.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.02635, res.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.01531, res.getPortfolioVariance(), Constant.EPSILON);
    }

    @Test
    void getBlackLittermanOptimizeResultWithOmega() throws ParameterIsNullException, DateFormatException, UndefinedParameterValueException, DimensionMismatchException, ParameterRangeErrorException {
        double[] Q = {0.2, 0.1, 0.05};
        Map<String, double[]> P = generateP();
        double[] omega = {0.1, 0.3, 0.5};

        Map<String, Double> marketCap = generateMarketCap();
        MinPortfolioVariance mpv = new MinPortfolioVariance(data, riskFreeRate, Constant.TRADINGDAYS);
        Result res = mpv.getBlackLittermanOptimizeResult(P, marketCap, Q, omega, tau, marketReturn, marketVariance, Constant.ReturnType.common);

        assertEquals(0.30178, res.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.05744, res.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.01540, res.getPortfolioVariance(), Constant.EPSILON);
    }
}