package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.DateFormatException;
import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MinPortfolioVarianceWithTargetReturnTest extends LoadData {
    final static double tau = 0.05;
    final static double riskFreeRate = 0.02;
    final static double marketReturn = 0.10845;
    final static double marketVariance = 0.03294;

    @Test
    void geOptimizeResult() throws ParameterIsNullException, UndefinedParameterValueException {
        double targetReturn = 0.35;
        MinPortfolioVarianceWithTargetReturn mpv = new MinPortfolioVarianceWithTargetReturn(data, riskFreeRate, Constant.TRADINGDAYS);
        Result ret = mpv.getMarkowitzOptimizeResult(data, targetReturn, Constant.ReturnType.common, riskFreeRate, ConstantForTest.TRADINGDAYS);

        assertEquals(2.10249, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(targetReturn, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.02463, ret.getPortfolioVariance(), Constant.EPSILON);
    }

    @Test
    void getBlackLittermanOptimizeResult() throws ParameterIsNullException, UndefinedParameterValueException, DimensionMismatchException {
        double targetReturn = 0.35;
        double[] Q = {0.2, 0.1, 0.05};
        Map<String, double[]> P = generateP();

        Map<String, Double> marketCap = generateMarketCap();
        MinPortfolioVarianceWithTargetReturn mpv = new MinPortfolioVarianceWithTargetReturn(data, riskFreeRate, Constant.TRADINGDAYS);
        Result res = mpv.getBlackLittermanOptimizeResult(targetReturn, P, marketCap, Q, tau, marketReturn, marketVariance, Constant.ReturnType.common);

        assertEquals(0.52601, res.getSharpeRatio(), Constant.EPSILON);
        assertEquals(targetReturn, res.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.39358, res.getPortfolioVariance(), Constant.EPSILON);
    }

    @Test
    void getBlackLittermanOptimizeResultWithOmega() throws ParameterIsNullException, DateFormatException, UndefinedParameterValueException, DimensionMismatchException {
        double targetReturn = 0.35;
        double[] Q = {0.2, 0.1, 0.05};
        Map<String, double[]> P = generateP();
        double[] omega = {0.9, 0.3, 0.5};

        Map<String, Double> marketCap = generateMarketCap();
        MinPortfolioVarianceWithTargetReturn mpv = new MinPortfolioVarianceWithTargetReturn(data, riskFreeRate, Constant.TRADINGDAYS);
        Result res = mpv.getBlackLittermanOptimizeResult(targetReturn, P, marketCap, Q, omega, tau, marketReturn, marketVariance, Constant.ReturnType.common);

        assertEquals(0.32400, res.getSharpeRatio(), Constant.EPSILON);
        assertEquals(targetReturn, res.getWeightedReturns(), Constant.EPSILON);
        assertEquals(1.03736, res.getPortfolioVariance(), Constant.EPSILON);
    }
}