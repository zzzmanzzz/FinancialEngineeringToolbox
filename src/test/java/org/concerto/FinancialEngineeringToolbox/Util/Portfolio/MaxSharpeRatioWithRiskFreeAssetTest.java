package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaxSharpeRatioWithRiskFreeAssetTest extends LoadData {

    @Test
    void geOptimizeResult() throws ParameterIsNullException, UndefinedParameterValueException {
        double riskFreeRate = 0.01 / ConstantForTest.TRADINGDAYS;
        MaxSharpeRatioWithRiskFreeAsset mpv = new MaxSharpeRatioWithRiskFreeAsset();
        Result ret = mpv.getMarkowitzOptimizeResult(data, riskFreeRate, Constant.ReturnType.common);

        assertEquals(0.33638, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.01404, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.00173, ret.getPortfolioVariance(), Constant.EPSILON);

        assertEquals(-0.44096, ret.getWeight("AAPL"), Constant.EPSILON);
        assertEquals(-1.14401, ret.getWeight("GOOG"), Constant.EPSILON);
        assertEquals(0.87049, ret.getWeight("AMZN"), Constant.EPSILON);
       // assertEquals(0.50164, ret.getWeight("ddd"), Constant.EPSILON);
       // assertEquals(0.18691, ret.getWeight("eee"), Constant.EPSILON);
    }
}