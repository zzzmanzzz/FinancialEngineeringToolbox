package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.concerto.FinancialEngineeringToolbox.Util.Portfolio.MinPortfolioVarianceWithTargetReturn;
import org.concerto.FinancialEngineeringToolbox.Util.Portfolio.Result;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MinPortfolioVarianceWithTargetReturnTest extends LoadData {

    @Test
    void geOptimizeResult() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException {
        double targetReturn = 0.35;
        MinPortfolioVarianceWithTargetReturn mpv = new MinPortfolioVarianceWithTargetReturn();
        Result ret = mpv.getMarkowitzOptimizeResult(data, targetReturn, Constant.ReturnType.common, ConstantForTest.TRADINGDAYS);

        assertEquals(2.22991, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(targetReturn, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.02463, ret.getPortfolioVariance(), Constant.EPSILON);
    }
}