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


public class MinPortfolioVariance extends PortfolioOptimization {

    public Result geOptimizeResult(Map<String, double[]> data, Constant.ReturnType type, Constant.PortfolioType portfolioType) throws ParameterIsNullException, UndefinedParameterValueException, ParameterRangeErrorException {
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

        double[][] cov = getCovariance(returns);
        double[] mean = getMeanReturn(returns);
        double[] weight =  optimize(mean, cov, 0);
        double weightedReturn = getWeightedReturn(weight, mean);
        double portfolioVariance = getPortfolioVariance(cov, weight);
        double sharpeRatio = getWeightedSharpeRatio(weight, mean, cov, 0);
        return new Result(keys, weight, sharpeRatio, weightedReturn, portfolioVariance);
    }


    protected RealMatrix initA(double[][] cov) {
        int size = cov.length + 1;
        RealMatrix ret = new Array2DRowRealMatrix(size, size);
        RealMatrix tmp = new Array2DRowRealMatrix(cov);
        double[] one = new double[size];
        Arrays.fill(one, 1);
        one[one.length - 1] = 0;

        ret.setSubMatrix(tmp.scalarMultiply(2).getData(), 0, 0);
        ret.setColumn(size - 1, one);
        ret.setRow(size - 1, one);

        return ret;
    }

    protected RealMatrix initB(int size) {
        double[] b = new double[size];
        Arrays.fill(b, 0);
        b[b.length - 1] = 1;
        return new Array2DRowRealMatrix(b);
    }

    @Override
    protected double[] optimize(double[] mean, double[][] cov, double riskFreeRate) {
        RealMatrix A = initA(cov);
        RealMatrix b = initB(cov.length + 1);
        double[] z = MatrixUtils.inverse(A).multiply(b).getColumn(0);
        return Arrays.copyOfRange(z, 0, z.length - 1);
    }

}