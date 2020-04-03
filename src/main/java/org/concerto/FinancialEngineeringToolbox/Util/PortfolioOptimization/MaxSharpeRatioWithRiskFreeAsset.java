package org.concerto.FinancialEngineeringToolbox.Util.PortfolioOptimization;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public class MaxSharpeRatioWithRiskFreeAsset extends AbstractPortfolioOptimization{

    public  Result geOptimizeResult(Map<String, double[]> data, double riskFreeRate, Constant.ReturnType type) throws ParameterIsNullException, UndefinedParameterValueException, ParameterRangeErrorException {

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

        return optimize(keys, returns, riskFreeRate);
    }

    private RealMatrix initA(double[][] cov) {
        return new Array2DRowRealMatrix(cov);
    }

    private RealMatrix initB(double[] mean, double riskFreeRate) {
        RealMatrix ret = new Array2DRowRealMatrix(mean);
        ret.scalarAdd(-riskFreeRate);
        return ret;
    }

    @Override
    protected Result optimize(String[] symbols, double[][] returns, double riskFreeRate) throws ParameterRangeErrorException {
        double[][] cov = getCovariance(returns);
        double[] mean = getMeanReturn(returns);
        RealMatrix A = initA(cov);
        RealMatrix B = initB(mean, riskFreeRate);
        double[] w = MatrixUtils.inverse(A).multiply(B).getColumn(0);

        double sum = Arrays.stream(w).sum();
        double[] weight = new double[mean.length];

        for(int i = 0 ; i < weight.length ; i++ ) {
            weight[i] = w[i] / sum;
        }

        double weightedReturn = getWeightedReturn(weight, mean);
        double portfolioVariance = getPortfolioVariance(cov, weight);
        double sharpeRatio = (weightedReturn- riskFreeRate) / Math.sqrt(portfolioVariance);
        Result ret = new Result(symbols, weight, sharpeRatio, weightedReturn, portfolioVariance);

        return ret;
    }
}
