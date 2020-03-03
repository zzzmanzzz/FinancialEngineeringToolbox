package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.random.SobolSequenceGenerator;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.concerto.FinancialEngineeringToolbox.Util.Returns.Rate;

import java.util.Map;
import java.util.function.Function;

public class EfficientFrontier {
    static final private Mean m = new Mean();

    public static Result getEfficientFrontier(Map<String, double[]> data, double riskFreeRate, Constant.ReturnType type) throws ParameterIsNullException, UndefinedParameterValueException, ParameterRangeErrorException {
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

    private static Result optimize(String[] symbols, double[][] returns, double riskFreeRate) throws ParameterRangeErrorException {
        RealMatrix r = new Array2DRowRealMatrix(getCovariance(returns));
        double[] mean = new double[returns.length];
        for(int i = 0 ; i < returns.length; i++) {
            mean[i] = m.evaluate(returns[i], 0, returns[i].length);
        }

        Function<double[], Double> WeightedSharpeRatio = (double[] weight) -> {
            RealMatrix w = new Array2DRowRealMatrix(weight);
            double varP = w.transpose().multiply(r).multiply(w).getData()[0][0];
            double weightMean = 0;
            for(int i = 0 ; i < weight.length; i++) {
                weightMean += weight[i] * mean[i];
            }
            return ((weightMean - riskFreeRate) / Math.sqrt(varP));
        };

        double[] bestWeight = null;
        double bestSharpeRatio = 0;

        SobolSequenceGenerator g = new SobolSequenceGenerator(returns.length);
        for(int i = 0 ; i < Constant.MAXTRY ; i++) {
            double[] w = g.nextVector();
            double sum = 0;
            for (int j = 0 ; j < w.length; j++) {
                sum += w[j];
            }

            for (int j = 0 ; j < w.length; j++) {
                w[j] /= sum;
            }

            double tmp = WeightedSharpeRatio.apply(w);
            if(tmp > bestSharpeRatio) {
                bestSharpeRatio = tmp;
                bestWeight = w;
            }
        }

        double weightedReturns = 0;
        for(int i = 0 ; i < mean.length; i++) {
            weightedReturns += mean[i] * bestWeight[i];
        }

        return new Result(symbols, bestWeight, bestSharpeRatio, weightedReturns);

    }

    private static double[][] getCovariance(double[][] data) {
        double[][] ret = new double[data.length][data.length];
        Covariance covariance = new Covariance();

        for (int i = 0 ; i < data.length; i++ ) {
            ret[i][i] = 1;
        }

        for(int i = 0 ; i < data.length; i++ ) {
            for( int j = i + 1 ; j < data.length; j++) {
                ret[i][j] = covariance.covariance(data[i], data[j]);
                ret[j][i] = ret[i][j];
            }
        }
        return ret;
    }

    private static Function<double[], double[]> getReturnFunction(Constant.ReturnType type) throws UndefinedParameterValueException {
        Function<double[], double[]> funcRef = Rate::getCommonReturn;

        switch (type) {
            case common:
                funcRef = Rate::getCommonReturn;
                break;
            case log:
                funcRef = Rate::getLogReturn;
                break;
            default:
                throw new UndefinedParameterValueException("Unexpected value: " + type, null);
        }

        return funcRef;
    }

}
