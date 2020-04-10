package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EfficientFrontierTest extends LoadData {

    @Test
    void getMaxSharpeRatioCommon() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException, DimensionMismatchException {
        double riskFreeRate = 0.02;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, ConstantForTest.TRADINGDAYS);
        double[] upper = new double[data.size()];
        double[] lower = new double[data.size()];
        double[] init = new double[data.size()];
        Arrays.fill(upper, 1.0);
        Arrays.fill(lower, 0.0);
        Arrays.fill(init, .05);

        Result ret = ef.getMaxSharpeRatio(upper, lower, init,Constant.ReturnType.common);
        assertEquals(1.42066, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.22292, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.02040, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.0, GOOG=0.0, AAPL=0.015070977025357825, RRC=0.0, BAC=0.0, GM=0.0, JPM=0.06123834001250623, SHLD=0.0, PFE=0.12707264785394531, T=0.1917118359483994, UAA=0.0, MA=0.14656058248947235, SBUX=0.049180638344572265, XOM=0.0, AMD=0.018660486353501468, BBY=0.07336363802514242, FB=0.007229341870841818, AMZN=0.2146699609887241, GE=0.0, WMT=0.09524155108753682}";
        assertEquals(weight, ret.getData().toString());
    }

    @Test
    void getMinVariance() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException, DimensionMismatchException {
        double riskFreeRate = 0.02;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, ConstantForTest.TRADINGDAYS);
        double[] upper = new double[data.size()];
        double[] lower = new double[data.size()];
        double[] init = new double[data.size()];
        Arrays.fill(upper, 1.0);
        Arrays.fill(lower, 0.0);
        Arrays.fill(init, 0.05);

        Result ret = ef.getMinVariance(upper, lower, init, Constant.ReturnType.common);
        assertEquals(0.55564, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.08795, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.01496, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.02730660475814947, GOOG=0.00787007386508332, AAPL=0.030814730296388353, RRC=0.0, BAC=0.0, GM=0.0, JPM=0.0, SHLD=0.0, PFE=0.1933923392929262, T=0.28726615207835865, UAA=0.0, MA=0.0, SBUX=0.11614851634180658, XOM=0.1253077819604376, AMD=0.0, BBY=0.015387669114923314, FB=0.010011490950475701, AMZN=0.013046615327733881, GE=0.03352001708618105, WMT=0.13992800892753585}";
        assertEquals(weight, ret.getData().toString());
    }

    @Test
    void getMinVarianceWithTargetReturn() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException, DimensionMismatchException {
        double riskFreeRate = 0.02;
        double targetReturn = 0.36;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, ConstantForTest.TRADINGDAYS);
        double[] upper = new double[data.size()];
        double[] lower = new double[data.size()];
        double[] init = new double[data.size()];
        Arrays.fill(upper, 1.0);
        Arrays.fill(lower, 0.0);
        Arrays.fill(init, .05);

        Result ret = ef.getMinVarianceWithTargetReturn(upper, lower, init, targetReturn, Constant.ReturnType.common);
        assertEquals(1.68719, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(targetReturn, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.04060, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.0, GOOG=0.0, AAPL=0.0, RRC=0.0, BAC=0.0, GM=0.0, JPM=0.11305832565293564, SHLD=0.0, PFE=0.0, T=0.0, UAA=0.0, MA=0.22227557005430515, SBUX=0.0, XOM=0.0, AMD=0.05536274833283162, BBY=0.1389125645715793, FB=1.4507995507690612E-4, AMZN=0.47024571143327143, GE=0.0, WMT=0.0}";
        assertEquals(weight, ret.getData().toString());
    }
}