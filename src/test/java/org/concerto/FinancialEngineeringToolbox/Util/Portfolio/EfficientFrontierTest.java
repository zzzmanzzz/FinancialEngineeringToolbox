package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EfficientFrontierTest extends LoadData {
    final static double tau = 0.05;
    final static double marketReturn = 0.10845;
    final static double marketVariance = 0.03294;

    @Test
    void getMaxSharpeRatio() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException, DimensionMismatchException {
        double riskFreeRate = 0.02;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, Constant.ReturnType.common,ConstantForTest.TRADINGDAYS);
        double[] upper = new double[data.size()];
        double[] lower = new double[data.size()];
        double[] init = new double[data.size()];
        Arrays.fill(upper, 1.0);
        Arrays.fill(lower, 0.0);
        Arrays.fill(init, .05);

        Result ret = ef.getMaxSharpeRatio(upper, lower, init, Constant.ReturnType.common);
        assertEquals(1.68845, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.36789, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.04245, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.0, GOOG=0.0, AAPL=0.0, RRC=0.0, BAC=0.0, GM=0.0, JPM=0.09098985085840304, SHLD=0.0, PFE=0.0, T=0.0, UAA=0.0, MA=0.20706901938386119, SBUX=0.0, XOM=0.0, AMD=0.0604534994530656, BBY=0.14140883471571586, FB=0.0, AMZN=0.5000787955889543, GE=0.0, WMT=0.0}";
        assertEquals(weight, ret.getData().toString());
    }

    @Test
    void getMaxSharpeRatioBLWithExactlyCertaintyOmega() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException, DimensionMismatchException, DateFormatException {
        double riskFreeRate = 0.02;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, Constant.ReturnType.common, ConstantForTest.TRADINGDAYS);
        double[] upper = new double[data.size()];
        double[] lower = new double[data.size()];
        double[] init = new double[data.size()];
        Arrays.fill(upper, 1.0);
        Arrays.fill(lower, 0.0);
        Arrays.fill(init, .05);

        double[] Q = {0.2, 0.1, 0.05};
        double[] Omega = {0, 0, 0};

        Map<String, double[]> P = generateP();

        Map<String, Double> marketCap = generateMarketCap();

        Result ret = ef.getMaxSharpeRatio(upper, lower, init, P, marketCap, Q, Omega, tau, marketReturn, marketVariance);
        assertEquals(0.09858, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.04247, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.05196, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.0, GOOG=0.696112753580014, AAPL=0.0, RRC=0.0, BAC=0.0, GM=0.0, JPM=0.0, SHLD=0.0, PFE=0.0, T=0.0, UAA=0.0, MA=0.0, SBUX=0.0, XOM=0.0, AMD=0.0, BBY=0.0, FB=0.0, AMZN=0.3038872464199861, GE=0.0, WMT=0.0}";
        assertEquals(weight, ret.getData().toString());
    }

    @Test
    void getMaxSharpeRatioBLWithCompletelyUncertaintyOmega() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException, DimensionMismatchException, DateFormatException {
        double riskFreeRate = 0.02;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, Constant.ReturnType.common, ConstantForTest.TRADINGDAYS);
        double[] upper = new double[data.size()];
        double[] lower = new double[data.size()];
        double[] init = new double[data.size()];
        Arrays.fill(upper, 1.0);
        Arrays.fill(lower, 0.0);
        Arrays.fill(init, .05);

        double[] Q = {0.2, 0.1, 0.05};
        double[] Omega = {1e100, 1e100, 1e100};

        Map<String, double[]> P = generateP();

        Map<String, Double> marketCap = generateMarketCap();

        Result ret = ef.getMaxSharpeRatio(upper, lower, init, P, marketCap, Q, Omega, tau, marketReturn, marketVariance);
        assertEquals(0.42879, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.09189, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.02811, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.08325523801868315, GOOG=0.1447984896616167, AAPL=0.18587940401636127, RRC=1.5620203126483334E-4, BAC=0.047016530211756134, GM=0.007966258659696374, JPM=0.06591694915372352, SHLD=0.0, PFE=0.033114651017986274, T=0.009528299879614146, UAA=0.012183698386671429, MA=0.044985986447805194, SBUX=0.01593250828610143, XOM=0.046079300770542286, AMD=0.006716651547001417, BBY=0.003436421888829172, FB=0.08965946977184121, AMZN=0.1354264384948007, GE=0.014995300516975197, WMT=0.052952201238729554}";
        assertEquals(weight, ret.getData().toString());
    }

    @Test
    void getMaxSharpeRatioBL() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException, DimensionMismatchException, DateFormatException {
        double riskFreeRate = 0.02;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, Constant.ReturnType.common, ConstantForTest.TRADINGDAYS);
        double[] upper = new double[data.size()];
        double[] lower = new double[data.size()];
        double[] init = new double[data.size()];
        Arrays.fill(upper, 1.0);
        Arrays.fill(lower, 0.0);
        Arrays.fill(init, .05);

        double[] Q = {0.2, 0.1, 0.05};
        Map<String, double[]> P = generateP();

        Map<String, Double> marketCap = generateMarketCap();

        Result ret = ef.getMaxSharpeRatio(upper, lower, init, P, marketCap, Q, tau, marketReturn, marketVariance);
        assertEquals(0.24487, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.07269, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.04630, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.0, GOOG=0.5180298338316487, AAPL=0.14213503741156325, RRC=0.0, BAC=0.0, GM=0.0, JPM=0.0, SHLD=0.0, PFE=0.0, T=0.0, UAA=0.0, MA=0.0, SBUX=0.0, XOM=0.0, AMD=0.0, BBY=0.0, FB=0.02271608314880785, AMZN=0.31711904560798015, GE=0.0, WMT=0.0}";
        assertEquals(weight, ret.getData().toString());
    }

    @Test
    void getMinVarianceBL() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException, DimensionMismatchException, DateFormatException {
        double riskFreeRate = 0.02;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, Constant.ReturnType.common, ConstantForTest.TRADINGDAYS);
        double[] upper = new double[data.size()];
        double[] lower = new double[data.size()];
        double[] init = new double[data.size()];
        Arrays.fill(upper, 1.0);
        Arrays.fill(lower, 0.0);
        Arrays.fill(init, 0.05);

        double[] Q = {0.2, 0.1, 0.05};
        Map<String, double[]> P = generateP();

        Map<String, Double> marketCap = generateMarketCap();

        Result ret = ef.getMinVariance(upper, lower, init, P, marketCap, Q, tau, marketReturn, marketVariance);
        assertEquals(0.05719, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.02714, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.01561, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.027377002911909316, GOOG=0.008049673221518857, AAPL=0.03056362214259695, RRC=0.0, BAC=0.0, GM=0.003139466964626523, JPM=0.0, SHLD=0.0, PFE=0.19228094679112853, T=0.28647374600274456, UAA=0.0, MA=0.0, SBUX=0.11620020770388369, XOM=0.12498598031936779, AMD=0.0, BBY=0.015267454864065775, FB=0.010607869574562681, AMZN=0.011888696963645345, GE=0.033921039448292806, WMT=0.13924429309165717}";
        assertEquals(weight, ret.getData().toString());
    }
    @Test
    void getMinVarianceBLWithExactlyCertaintyOmega() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException, DimensionMismatchException, DateFormatException {
        double riskFreeRate = 0.02;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, Constant.ReturnType.common, ConstantForTest.TRADINGDAYS);
        double[] upper = new double[data.size()];
        double[] lower = new double[data.size()];
        double[] init = new double[data.size()];
        Arrays.fill(upper, 1.0);
        Arrays.fill(lower, 0.0);
        Arrays.fill(init, 0.05);

        double[] Q = {0.2, 0.1, 0.05};
        double[] Omega = {0, 0, 0};

        Map<String, double[]> P = generateP();

        Map<String, Double> marketCap = generateMarketCap();

        Result ret = ef.getMinVariance(upper, lower, init, P, marketCap, Q, Omega, tau, marketReturn, marketVariance);
        assertEquals(-0.22136, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(-0.00757, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.01551, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.026816053651845282, GOOG=0.008020009489481944, AAPL=0.03053514493155577, RRC=0.0, BAC=0.0, GM=0.010028895807356976, JPM=0.0, SHLD=0.0, PFE=0.19060542351474605, T=0.284589032555058, UAA=0.0, MA=0.0, SBUX=0.11537910959952263, XOM=0.12454764916466751, AMD=0.0, BBY=0.015170285575054735, FB=0.01025113997808897, AMZN=0.011982545409951143, GE=0.03373204830790416, WMT=0.13834266201476678}";
        assertEquals(weight, ret.getData().toString());
    }
    @Test
    void getMinVarianceBLWithCompletelyUncertaintyOmega() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException, DimensionMismatchException, DateFormatException {
        double riskFreeRate = 0.02;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, Constant.ReturnType.common, ConstantForTest.TRADINGDAYS);
        double[] upper = new double[data.size()];
        double[] lower = new double[data.size()];
        double[] init = new double[data.size()];
        Arrays.fill(upper, 1.0);
        Arrays.fill(lower, 0.0);
        Arrays.fill(init, 0.05);
        double[] Q = {0.2, 0.1, 0.05};
        double[] Omega = {1e100, 1e100, 1e100};

        Map<String, double[]> P = generateP();

        Map<String, Double> marketCap = generateMarketCap();

        Result ret = ef.getMinVariance(upper, lower, init, P, marketCap, Q, Omega, tau, marketReturn, marketVariance);
        assertEquals(0.32687, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(0.06096, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.01570, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.027543276088354992, GOOG=0.007943429286408548, AAPL=0.030434738500801275, RRC=0.0, BAC=0.0, GM=0.0, JPM=0.0, SHLD=0.0, PFE=0.19284943871407487, T=0.28800039780774406, UAA=0.0, MA=0.0, SBUX=0.11666452829227382, XOM=0.12521028462813966, AMD=0.0, BBY=0.015154098622321222, FB=0.010479900523676396, AMZN=0.012468145691380168, GE=0.03330332659329855, WMT=0.1399484352515264}";
        assertEquals(weight, ret.getData().toString());
    }

    @Test
    void getMinVariance() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException, DimensionMismatchException {
        double riskFreeRate = 0.02;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, Constant.ReturnType.common, ConstantForTest.TRADINGDAYS);
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
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, Constant.ReturnType.common,ConstantForTest.TRADINGDAYS);
        double[] upper = new double[data.size()];
        double[] lower = new double[data.size()];
        double[] init = new double[data.size()];
        Arrays.fill(upper, 1.0);
        Arrays.fill(lower, 0.0);
        Arrays.fill(init, .05);

        Result ret = ef.getMinVarianceWithTargetReturn(upper, lower, init, targetReturn);
        assertEquals(1.68196, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(targetReturn, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.04086, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.002914354411220618, GOOG=0.0, AAPL=0.0, RRC=0.0, BAC=0.0, GM=0.0, JPM=0.08407050284225125, SHLD=0.0, PFE=0.0, T=0.0, UAA=0.0, MA=0.1966086955408023, SBUX=0.0, XOM=0.0, AMD=0.05829342920634812, BBY=0.14759522295615427, FB=0.028109728423992862, AMZN=0.4714336739886135, GE=0.0, WMT=0.010974392630617008}";
        assertEquals(weight, ret.getData().toString());
    }

    @Test
    void getMinVarianceWithTargetReturnBL() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException, DimensionMismatchException {
        double riskFreeRate = 0.02;
        double targetReturn = 0.06066;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, Constant.ReturnType.common,ConstantForTest.TRADINGDAYS);
        double[] upper = new double[data.size()];
        double[] lower = new double[data.size()];
        double[] init = new double[data.size()];
        Arrays.fill(upper, 1.0);
        Arrays.fill(lower, 0.0);
        Arrays.fill(init, .05);

        double[] Q = {0.2, 0.1, 0.05};
        Map<String, double[]> P = generateP();

        Map<String, Double> marketCap = generateMarketCap();

        Result ret = ef.getMinVarianceWithTargetReturn(upper, lower, init, targetReturn, P, marketCap, Q, tau, marketReturn, marketVariance);
        assertEquals(0.23002, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(targetReturn, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.03124, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.0, GOOG=0.3665328820443815, AAPL=0.16920022317381245, RRC=0.0, BAC=0.0, GM=0.0, JPM=0.0, SHLD=0.0, PFE=0.02461311319930314, T=0.09957771996751164, UAA=0.0, MA=0.0, SBUX=0.0, XOM=0.0, AMD=0.0, BBY=0.0, FB=0.05225792546096015, AMZN=0.20047008082235518, GE=0.0, WMT=0.08734805533167604}";
        assertEquals(weight, ret.getData().toString());
    }

    @Test
    void getMinVarianceWithTargetReturnBLWithExactlyCertaintyOmega() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException, DimensionMismatchException, DateFormatException {
        double riskFreeRate = 0.02;
        double targetReturn = 0.02633;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, Constant.ReturnType.common,ConstantForTest.TRADINGDAYS);
        double[] upper = new double[data.size()];
        double[] lower = new double[data.size()];
        double[] init = new double[data.size()];
        Arrays.fill(upper, 1.0);
        Arrays.fill(lower, 0.0);
        Arrays.fill(init, .05);

        double[] Q = {0.2, 0.1, 0.05};
        Map<String, double[]> P = generateP();
        double[] Omega = {0, 0, 0};

        Map<String, Double> marketCap = generateMarketCap();

        Result ret = ef.getMinVarianceWithTargetReturn(upper, lower, init, targetReturn, P, marketCap, Q, Omega, tau, marketReturn, marketVariance);
        assertEquals(0.03749, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(targetReturn, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.02847, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.0, GOOG=0.4642533490671777, AAPL=4.005345674389752E-4, RRC=0.0, BAC=0.0, GM=0.0, JPM=0.0, SHLD=0.0, PFE=0.0, T=0.2407217623173122, UAA=0.0, MA=0.0, SBUX=0.0, XOM=0.0, AMD=0.0, BBY=0.0, FB=0.0, AMZN=0.1672154842777216, GE=0.0, WMT=0.12740886977034946}";
        assertEquals(weight, ret.getData().toString());
    }

    @Test
    void getMinVarianceWithTargetReturnBLWithCompletelyUncertaintyOmega() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException, DimensionMismatchException, DateFormatException {
        double riskFreeRate = 0.02;
        double targetReturn = 0.06066;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, Constant.ReturnType.common,ConstantForTest.TRADINGDAYS);
        double[] upper = new double[data.size()];
        double[] lower = new double[data.size()];
        double[] init = new double[data.size()];
        Arrays.fill(upper, 1.0);
        Arrays.fill(lower, 0.0);
        Arrays.fill(init, .05);

        double[] Q = {0.2, 0.1, 0.05};
        Map<String, double[]> P = generateP();
        double[] Omega = {1e100, 1e100, 1e100};

        Map<String, Double> marketCap = generateMarketCap();

        Result ret = ef.getMinVarianceWithTargetReturn(upper, lower, init, targetReturn, P, marketCap, Q, Omega, tau, marketReturn, marketVariance);
        assertEquals(0.32409, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(targetReturn, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.01573, ret.getPortfolioVariance(), Constant.EPSILON);
        String weight = "{BABA=0.03374731811047595, GOOG=0.013189978160072946, AAPL=0.010395132414627601, RRC=0.0, BAC=0.0, GM=0.003014406718048024, JPM=0.0, SHLD=0.0, PFE=0.1977799407911898, T=0.27913316435248386, UAA=0.0, MA=0.0, SBUX=0.13139905850363004, XOM=0.12962982943979198, AMD=0.0, BBY=0.014797986235586893, FB=0.017332533953413925, AMZN=0.0, GE=0.03002673169007729, WMT=0.13955391963060165}";
        assertEquals(weight, ret.getData().toString());
    }
}