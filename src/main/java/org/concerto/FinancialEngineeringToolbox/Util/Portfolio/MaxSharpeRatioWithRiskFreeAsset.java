package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;

import java.util.Arrays;
import java.util.Map;

public class MaxSharpeRatioWithRiskFreeAsset extends PortfolioOptimization {


    MaxSharpeRatioWithRiskFreeAsset(Map<String, double[]> data, double riskFreeRate, int frequency) throws ParameterIsNullException {
        super(data, riskFreeRate, frequency);
    }

    public Result getMarkowitzOptimizeResult(Map<String, double[]> data, double riskFreeRate, Constant.ReturnType type, int frequency) throws ParameterIsNullException, UndefinedParameterValueException {
        String[] keys = DataProcessor.getDataKey(data);
        DataProcessor.validateData(data, keys);

        double[][] returns = getReturns(type);
        double[][] cov = getCovariance(returns, frequency);
        double[] mean = getMeanReturn(returns, frequency);
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
