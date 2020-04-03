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

public class MinPortfolioVarianceWithTargetReturn extends AbstractPortfolioOptimization {
    private double targetReturn;

    public  Result geOptimizeResult(Map<String, double[]> data, double targetReturn, Constant.ReturnType type) throws ParameterIsNullException, UndefinedParameterValueException, ParameterRangeErrorException {
        this.targetReturn = targetReturn;

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

        return optimize(keys, returns, 0);
    }

    private RealMatrix initA(double[][] cov, double[] meanReturn) {
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

    private RealMatrix initB(int size) {
        double[] tmp = new double[size];
        Arrays.fill(tmp, 0);
        tmp[size - 2] = targetReturn;
        tmp[size - 1] = 1;
        return new Array2DRowRealMatrix(tmp);
    }

    @Override
    protected Result optimize(String[] symbols, double[][] returns, double riskFreeRate) throws ParameterRangeErrorException {
        double[][] cov = getCovariance(returns);
        double[] mean = getMeanReturn(returns);
        RealMatrix A = initA(cov, mean);
        RealMatrix B = initB(cov.length + 2);
        double[] w = MatrixUtils.inverse(A).multiply(B).getColumn(0);
        double[] weight = Arrays.copyOfRange(w, 0, w.length - 2);

        double weightedReturn = getWeightedReturn(weight, mean);
        double portfolioVariance = getPortfolioVariance(cov, weight);
        double sharpeRatio = (weightedReturn- riskFreeRate) / Math.sqrt(portfolioVariance);
        Result ret = new Result(symbols, weight, sharpeRatio, weightedReturn, portfolioVariance);

        return ret;
    }
}
