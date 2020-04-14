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
    void getMaxSharpeRatio() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException, DimensionMismatchException {
        double riskFreeRate = 0.02;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, ConstantForTest.TRADINGDAYS);
        double[] upper = new double[data.size()];
        double[] lower = new double[data.size()];
        double[] init = new double[data.size()];
        Arrays.fill(upper, 1.0);
        Arrays.fill(lower, 0.0);
        Arrays.fill(init, .05);

        Result ret = ef.getMaxSharpeRatio(upper, lower, init,Constant.ReturnType.common);
        assertEquals(1.68845, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.36789, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.04245, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.0, GOOG=0.0, AAPL=0.0, RRC=0.0, BAC=0.0, GM=0.0, JPM=0.09098985085840304, SHLD=0.0, PFE=0.0, T=0.0, UAA=0.0, MA=0.20706901938386119, SBUX=0.0, XOM=0.0, AMD=0.0604534994530656, BBY=0.14140883471571586, FB=0.0, AMZN=0.5000787955889543, GE=0.0, WMT=0.0}";
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
        assertEquals(0.55476, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.08785, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.01496, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.027543276088354992, GOOG=0.007943429286408548, AAPL=0.030434738500801275, RRC=0.0, BAC=0.0, GM=0.0, JPM=0.0, SHLD=0.0, PFE=0.19284943871407487, T=0.28800039780774406, UAA=0.0, MA=0.0, SBUX=0.11666452829227382, XOM=0.12521028462813966, AMD=0.0, BBY=0.015154098622321222, FB=0.010479900523676396, AMZN=0.012468145691380168, GE=0.03330332659329855, WMT=0.1399484352515264}";
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
        assertEquals(1.68196, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(targetReturn, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.04086, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.002914354411220618, GOOG=0.0, AAPL=0.0, RRC=0.0, BAC=0.0, GM=0.0, JPM=0.08407050284225125, SHLD=0.0, PFE=0.0, T=0.0, UAA=0.0, MA=0.1966086955408023, SBUX=0.0, XOM=0.0, AMD=0.05829342920634812, BBY=0.14759522295615427, FB=0.028109728423992862, AMZN=0.4714336739886135, GE=0.0, WMT=0.010974392630617008}";
        assertEquals(weight, ret.getData().toString());
    }
}