package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;

import java.util.Arrays;
import java.util.Map;

public class MinPortfolioVarianceWithTargetReturn extends PortfolioOptimization {
    private double targetReturn;

    MinPortfolioVarianceWithTargetReturn(Map<String, double[]> data, double riskFreeRate, int frequency) throws ParameterIsNullException {
        super(data, riskFreeRate, frequency);
    }

    final public Result getMarkowitzOptimizeResult(Map<String, double[]> data, double targetReturn, Constant.ReturnType type, double riskFreeRate, int frequency) throws ParameterIsNullException, UndefinedParameterValueException {
        this.targetReturn = targetReturn;
        String[] keys = DataProcessor.getDataKey(data);
        DataProcessor.validateData(data, keys);

        double[][] returns = getReturns(type);
        double[][] cov = getCovariance(returns, frequency);
        double[] mean = getMeanReturn(returns, frequency);
        double[] weight = optimize(mean, cov);

        double weightedReturn = getWeightedReturn(weight, mean);
        double portfolioVariance = getPortfolioVariance(cov, weight);
        double sharpeRatio = getWeightedSharpeRatio(weight, mean, cov, riskFreeRate);
        return new Result(keys, weight, sharpeRatio, weightedReturn, portfolioVariance);
    }

    protected RealMatrix initA(double[][] cov, double[] meanReturn) {
        int size = cov.length + 2;
        RealMatrix ret = new Array2DRowRealMatrix(size, size);
        RealMatrix tmp = new Array2DRowRealMatrix(cov);
        ret.setSubMatrix(tmp.scalarMultiply(2).getData(), 0, 0);
        double[] mu = new double[size];
        double[] one = new double[size];

        Arrays.fill(mu, 0);
        Arrays.fill(one, 0);
        for(int i = 0 ; i < meanReturn.length ; i++ ) {
            mu[i] = meanReturn[i];
            one[i] = 1;
        }

        ret.setColumn(size - 2, mu);
        ret.setRow(size - 2, mu);
        ret.setColumn(size - 1, one);
        ret.setRow(size - 1, one);
        return ret;
    }

    protected RealMatrix initB(int size) {
        double[] tmp = new double[size];
        Arrays.fill(tmp, 0);
        tmp[size - 2] = targetReturn;
        tmp[size - 1] = 1;
        return new Array2DRowRealMatrix(tmp);
    }

    protected double[] optimize(double[] mean, double[][] cov) {
        RealMatrix A = initA(cov, mean);
        RealMatrix B = initB(cov.length + 2);
        double[] w = MatrixUtils.inverse(A).multiply(B).getColumn(0);
        return Arrays.copyOfRange(w, 0, w.length - 2);
    }
}
