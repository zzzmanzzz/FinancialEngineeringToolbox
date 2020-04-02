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


public class MinPortfolioVariance extends AbstractPortfolioOptimization {

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

        return optimize(keys, returns, 0);
    }


    private double[][] initA(double[][] cov) {
        int size = cov.length + 1;
        double[][] ret = new double[size][size];
        for(double[] a : ret) {
            Arrays.fill(a, 1);
        }
        ret[size - 1][size - 1] = 0;

        for (int i = 0 ; i < cov.length; i++ ) {
            ret[i][i] = cov[i][i] * 2;
        }

        for (int i = 0 ; i < cov.length ; i++) {
            for (int j = i + 1 ; j < cov[0].length ; j++ ) {
                ret[i][j] = 2 * cov[i][j];
                ret[j][i] = ret[i][j];
            }
        }
        return ret;
    }

    private double[] initB(int size) {
        double[] b = new double[size];
        Arrays.fill(b, 0);
        b[b.length - 1] = 1;
        return b;
    }

    @Override
    protected Result optimize(String[] symbols, double[][] returns, double riskFreeRate) throws ParameterRangeErrorException {
        double[][] cov = getCovariance(returns);
        double[] mean = getMeanReturn(returns);
        RealMatrix A = new Array2DRowRealMatrix(initA(cov));
        RealMatrix b = new Array2DRowRealMatrix(initB(cov.length + 1));
        double[] z = MatrixUtils.inverse(A).multiply(b).getColumn(0);

        double[] weight = Arrays.copyOfRange(z, 0, z.length - 1);

        double weightedReturn = getWeightedReturn(weight, mean);
        double portfolioVariance = getPortfolioVariance(cov, weight);
        double sharpeRatio = (weightedReturn- riskFreeRate) / Math.sqrt(portfolioVariance);
        Result ret = new Result(symbols, weight, sharpeRatio, weightedReturn, portfolioVariance);

        return ret;
    }
}
