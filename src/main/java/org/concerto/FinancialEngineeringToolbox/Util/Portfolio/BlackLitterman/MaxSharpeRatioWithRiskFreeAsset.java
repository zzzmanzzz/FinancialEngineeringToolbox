package org.concerto.FinancialEngineeringToolbox.Util.Portfolio.BlackLitterman;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.concerto.FinancialEngineeringToolbox.Util.Portfolio.Result;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;


public class MaxSharpeRatioWithRiskFreeAsset extends org.concerto.FinancialEngineeringToolbox.Util.Portfolio.Markowitz.MinPortfolioVariance {

    public Result geOptimizeResult(Map<String, double[]> data, Constant.ReturnType type) throws ParameterIsNullException, UndefinedParameterValueException, ParameterRangeErrorException {
        Object[] tmpK = data.keySet().toArray();
        String[] keys = new String[tmpK.length];

        for(int i = 0 ; i < keys.length;i++ ) {
            keys[i] = (String) tmpK[i];
        }


        for(Object k : keys) {
            if(data.get(k) == null) {
                String msg = String.format("key(%s) has null value", k);
                throw new ParameterIsNullException(msg, null);
            }
        }

        Function<double[], double[]> funcRef = getReturnFunction(type);
        double[][] returns = new double[keys.length][];


        for(int i = 0 ; i < keys.length ; i++) {
            double[] tmp = data.get(keys[i]);
            returns[i] = funcRef.apply(tmp);
        }
/*
        double[][] cov = BlackLitterman.getBLCovariance(getCovariance(returns));
        double[] mean = BlackLitterman.getBLMeanReturn(getMeanReturn(returns));
        double[] weight =  optimize(mean, cov, 0);
        double weightedReturn = getWeightedReturn(weight, mean);
        double portfolioVariance = getPortfolioVariance(cov, weight);
        double sharpeRatio = getWeightedSharpeRatio(weight, mean, cov, 0);
        return new Result(keys, weight, sharpeRatio, weightedReturn, portfolioVariance);

 */
        return  null;
    }

}
