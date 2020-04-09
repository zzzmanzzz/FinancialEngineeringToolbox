package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.apache.commons.math3.random.SobolSequenceGenerator;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;

import java.util.Map;
import java.util.function.Function;

public class EfficientFrontier extends PortfolioOptimization {
    static final private Mean m = new Mean();

    public Result getEfficientFrontier(Map<String, double[]> data, double riskFreeRate, Constant.ReturnType type) throws ParameterIsNullException, UndefinedParameterValueException, ParameterRangeErrorException {
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

        returns = dropna(returns);

        double[] mean = getMeanReturn(returns);
        double[][] cov = getCovariance(returns);

        double[] bestWeight = optimize(mean, cov, riskFreeRate);

        double weightedReturns = 0;

        for(int i = 0 ; i < mean.length; i++) {
            weightedReturns += mean[i] * bestWeight[i];
        }

        double bestSharpeRatio = getWeightedSharpeRatio(bestWeight, mean, cov, riskFreeRate);
        double variance = Math.pow(((weightedReturns - riskFreeRate) / bestSharpeRatio), 2);
        return new Result(keys, bestWeight, bestSharpeRatio, weightedReturns, variance);
    }


    protected double[] optimize(double[] mean, double[][] covariance, double riskFreeRate) throws ParameterRangeErrorException {

        double[] bestWeight = null;
        double bestSharpeRatio = 0;

        SobolSequenceGenerator g = new SobolSequenceGenerator(mean.length);
        for(int i = 0 ; i < Constant.MAXTRY ; i++) {
            double[] w = g.nextVector();
            double sum = 0;
            for (int j = 0 ; j < w.length; j++) {
                sum += w[j];
            }

            for (int j = 0 ; j < w.length; j++) {
                w[j] /= sum;
            }

            double tmp = getWeightedSharpeRatio(w, mean, covariance, riskFreeRate);
            if(tmp > bestSharpeRatio) {
                bestSharpeRatio = tmp;
                bestWeight = w;
            }
        }

        return bestWeight;

    }


}