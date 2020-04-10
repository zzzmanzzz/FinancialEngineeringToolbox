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
        Arrays.fill(upper, 1);
        Arrays.fill(lower, 0);
        Arrays.fill(init, 1e-8);

        Result ret = ef.getMaxSharpeRatio(upper, lower, init,Constant.ReturnType.common);
        assertEquals(1.22407, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.23574, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.03106, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.0, GOOG=0.14458474088318657, AAPL=0.0, RRC=0.03808279001496605, BAC=0.0, GM=0.04964779603041448, JPM=0.0, SHLD=0.011031191012579027, PFE=0.0, T=0.0, UAA=0.0, MA=0.2738581947233168, SBUX=0.10354847940214662, XOM=0.0, AMD=0.0, BBY=0.23621653454799224, FB=0.0, AMZN=0.14303027338539825, GE=0.0, WMT=0.0}";
        assertEquals(weight, ret.getData().toString());
    }

    @Test
    void getMinVariance() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException, DimensionMismatchException {
        double riskFreeRate = 0.02;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, ConstantForTest.TRADINGDAYS);
        double[] upper = new double[data.size()];
        double[] lower = new double[data.size()];
        double[] init = new double[data.size()];
        Arrays.fill(upper, 1);
        Arrays.fill(lower, 0);
        Arrays.fill(init, 1e-1);

        Result ret = ef.getMinVariance(upper, lower, init, Constant.ReturnType.common);
        assertEquals(0.55432, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.08779, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.01496, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.02742605635400424, GOOG=0.007948122739282384, AAPL=0.03081556648546749, RRC=0.0, BAC=0.0, GM=0.0, JPM=0.0, SHLD=0.0, PFE=0.19312231646394565, T=0.28754266960018454, UAA=0.0, MA=0.0, SBUX=0.11665253774828244, XOM=0.12530327203225, AMD=0.0, BBY=0.015125250854337337, FB=0.010353699130846584, AMZN=0.01234275992367111, GE=0.033344824249739384, WMT=0.1400229244179889}";
        assertEquals(weight, ret.getData().toString());
    }

    @Test
    void getMinVarianceWithTargetReturn() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException, DimensionMismatchException {
        double riskFreeRate = 0.02;
        double targetReturn = 0.3;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, ConstantForTest.TRADINGDAYS);
        double[] upper = new double[data.size()];
        double[] lower = new double[data.size()];
        double[] init = new double[data.size()];
        Arrays.fill(upper, 1);
        Arrays.fill(lower, 0);
        Arrays.fill(init, 1e-8);

        Result ret = ef.getMinVarianceWithTargetReturn(upper, lower, init, targetReturn, Constant.ReturnType.common);
        assertEquals(1.60524, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(targetReturn, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.03042, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.0011405050348741164, GOOG=0.0, AAPL=0.0, RRC=0.0, BAC=0.0, GM=0.0, JPM=0.14113014550261893, SHLD=0.0, PFE=0.0, T=0.12725069631191777, UAA=0.0, MA=0.25108047879775697, SBUX=0.001052909853934675, XOM=0.0, AMD=0.0, BBY=0.11940982708285024, FB=0.002228958019926735, AMZN=0.3567064793961206, GE=0.0, WMT=0.0}";
        assertEquals(weight, ret.getData().toString());
    }
}