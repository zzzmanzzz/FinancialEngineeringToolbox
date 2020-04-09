package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.concerto.FinancialEngineeringToolbox.Util.Portfolio.PortfolioOptimization;
import org.concerto.FinancialEngineeringToolbox.Util.Portfolio.Result;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public class MaxSharpeRatioWithRiskFreeAsset extends PortfolioOptimization {


    public Result getMarkowitzOptimizeResult(Map<String, double[]> data, double riskFreeRate, Constant.ReturnType type) throws ParameterIsNullException, UndefinedParameterValueException {

        Object[] tmpK = data.keySet().toArray();
        String[] keys = new String[tmpK.length];

        for(int i = 0 ; i < keys.length;i++ ) {
            keys[i] = (String) tmpK[i];
        }

        for(Object k : keys) {
            if(null == data.get(k)) {
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

        returns = dropna(returns);

        double[][] cov = getCovariance(returns);
        double[] mean = getMeanReturn(returns);
        double[] weight = optimize(mean, cov, riskFreeRate);

        double weightedReturn = getWeightedReturn(weight, mean);
        double portfolioVariance = getPortfolioVariance(cov, weight);
        double sharpeRatio = getWeightedSharpeRatio(weight, mean, cov, riskFreeRate);
        return new Result(keys, weight, sharpeRatio, weightedReturn, portfolioVariance);
    }

    protected RealMatrix initA(double[][] cov) {
        return new Array2DRowRealMatrix(cov);
    }

    protected RealMatrix initB(double[] mean, double riskFreeRate) {
        RealMatrix ret = new Array2DRowRealMatrix(mean);
        ret.scalarAdd(-riskFreeRate);
        return ret;
    }

    protected double[] optimize(double[] mean, double[][] cov, double riskFreeRate) {
        RealMatrix A = initA(cov);
        RealMatrix B = initB(mean, riskFreeRate);
        double[] w = MatrixUtils.inverse(A).multiply(B).getColumn(0);
        double sum = Arrays.stream(w).sum();
        double[] weight = new double[mean.length];
        for(int i = 0 ; i < weight.length ; i++ ) {
            weight[i] = w[i] / sum;
        }
        return weight;
    }

}
