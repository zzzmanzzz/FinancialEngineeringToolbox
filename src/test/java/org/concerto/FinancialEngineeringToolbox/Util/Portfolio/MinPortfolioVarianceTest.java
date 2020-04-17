package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MinPortfolioVarianceTest extends LoadData {
    final double riskFreeRate = 0.0;

    @Test
    void geOptimizeResult() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException {
        MinPortfolioVariance mpv = new MinPortfolioVariance(data, riskFreeRate, Constant.TRADINGDAYS);
        Result ret = mpv.getMarkowitzOptimizeResult(Constant.ReturnType.common);

        assertEquals(0.68881, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.08342, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.01466, ret.getPortfolioVariance(), Constant.EPSILON);
    }
}