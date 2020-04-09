package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EfficientFrontierTest extends LoadData {

    @Test
    void getEfficientFrontierCommon() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException {
        double riskFreeRate = 0.01 / ConstantForTest.TRADINGDAYS;
        EfficientFrontier ef = new EfficientFrontier();
        Result ret = ef.getEfficientFrontier(data, riskFreeRate, Constant.ReturnType.common);
        assertEquals(0.10181, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.00089, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.00008, ret.getPortfolioVariance(), Constant.EPSILON);

    }

    @Test
    void getEfficientFrontierLog() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException {
        double riskFreeRate = 0.01 / ConstantForTest.TRADINGDAYS;
        EfficientFrontier ef = new EfficientFrontier();
        Result ret = ef.getEfficientFrontier(data, riskFreeRate, Constant.ReturnType.log);

        assertEquals(0.08718, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.00077, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.00008, ret.getPortfolioVariance(), Constant.EPSILON);

    }

}