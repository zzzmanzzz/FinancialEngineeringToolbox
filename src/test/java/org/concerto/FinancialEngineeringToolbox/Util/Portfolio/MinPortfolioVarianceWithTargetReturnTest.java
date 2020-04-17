package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MinPortfolioVarianceWithTargetReturnTest extends LoadData {
    final double riskFreeRate = 0.0;

    @Test
    void geOptimizeResult() throws ParameterIsNullException, UndefinedParameterValueException {
        double targetReturn = 0.35;
        MinPortfolioVarianceWithTargetReturn mpv = new MinPortfolioVarianceWithTargetReturn(data, riskFreeRate, Constant.TRADINGDAYS);
        Result ret = mpv.getMarkowitzOptimizeResult(data, targetReturn, Constant.ReturnType.common, riskFreeRate, ConstantForTest.TRADINGDAYS);

        assertEquals(2.22991, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(targetReturn, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.02463, ret.getPortfolioVariance(), Constant.EPSILON);
    }
}