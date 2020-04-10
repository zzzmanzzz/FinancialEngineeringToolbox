package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.concerto.FinancialEngineeringToolbox.Util.Portfolio.MinPortfolioVariance;
import org.concerto.FinancialEngineeringToolbox.Util.Portfolio.Result;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MinPortfolioVarianceTest extends LoadData {

    @Test
    void geOptimizeResult() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException {
        MinPortfolioVariance mpv = new MinPortfolioVariance();
        Result ret = mpv.getMarkowitzOptimizeResult(data, Constant.ReturnType.common, ConstantForTest.TRADINGDAYS);

        assertEquals(0.68881, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.08342, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.01466, ret.getPortfolioVariance(), Constant.EPSILON);
    }
}