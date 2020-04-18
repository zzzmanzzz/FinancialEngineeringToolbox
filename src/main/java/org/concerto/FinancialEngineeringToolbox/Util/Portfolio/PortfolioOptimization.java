package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.concerto.FinancialEngineeringToolbox.Util.Returns.Rate;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

abstract class PortfolioOptimization {
    static final protected Mean m = new Mean();
    protected Map<String, double[]> data;
    protected String[] symbols;
    protected int frequency;

    protected double riskFreeRate;
    protected double targetReturn;


    PortfolioOptimization(Map<String, double[]> data, double riskFreeRate, int frequency) throws ParameterIsNullException {
        this.riskFreeRate = riskFreeRate;
        this.frequency = frequency;
        this.data = data;
        Set<String> keys = data.keySet();
        this.symbols = keys.toArray(new String[keys.size()]);
        for(String s : symbols) {
            if(data.get(s) == null) {
                String msg = String.format("key(%s) has null value", s);
                throw new ParameterIsNullException(msg, null);
            }
        }
    }

    final protected double[][] getReturns(Constant.ReturnType type) throws UndefinedParameterValueException {
        Function<double[], double[]> funcRef = getReturnFunction(type);
        double[][] returns = new double[symbols.length][];

        for(int i = 0 ; i < symbols.length ; i++) {
            double[] tmp = data.get(symbols[i]);
            returns[i] = funcRef.apply(tmp);
        }
        return DataProcessor.dropna(returns);
    }

    final protected double[] getMeanReturn(double[][] returns, int frequency) {
        double[] mean = new double[returns.length];
        for(int i = 0 ; i < returns.length; i++) {
            mean[i] = m.evaluate(returns[i], 0, returns[i].length) * frequency;
        }
        return mean;
    }

    final protected double[][] getCovariance(double[][] data, int frequency) {
        double[][] ret = new double[data.length][data.length];
        Covariance covariance = new Covariance();

        for (int i = 0 ; i < data.length; i++ ) {
            ret[i][i] = covariance.covariance(data[i], data[i]) * frequency;
        }

        for(int i = 0 ; i < data.length; i++ ) {
            for( int j = i + 1 ; j < data.length; j++) {
                ret[i][j] = covariance.covariance(data[i], data[j]) * frequency;
                ret[j][i] = ret[i][j];
            }
        }
        return ret;
    }

    final protected double getWeightedReturn(double[] weight, double[] returns) {
        double ret = 0;
        for(int i = 0 ; i < weight.length; i++ ) {
            ret += weight[i] * returns[i];
        }
        return ret;
    }

    final protected double getPortfolioVariance(double[][] cov, double[] weight) {
        RealMatrix w = new Array2DRowRealMatrix(weight);
        RealMatrix r = new Array2DRowRealMatrix(cov);
        return w.transpose().multiply(r).multiply(w).getData()[0][0];
    }

    public double getWeightedSharpeRatio(double[] weight, double[] mean, double[][] cov, double riskFreeRate) {
        double varP = getPortfolioVariance(cov, weight);
        double weightMean = getWeightedReturn(weight, mean);
        return ((weightMean - riskFreeRate) / Math.sqrt(varP));
    }

    final protected Function<double[], double[]> getReturnFunction(Constant.ReturnType type) throws UndefinedParameterValueException {
        Function<double[], double[]> funcRef;

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


    protected MultivariateFunction getObjectiveFunction(double[] mean, double[][] cov, EfficientFrontier.ObjectiveFunction obj) throws UndefinedParameterValueException {
        class MaxSharpeRatio implements MultivariateFunction, Serializable {
            @Override
            public double value(double[] weight) {
                weight = normalizeWeight(weight);
                return getWeightedSharpeRatio(weight, mean, cov, riskFreeRate);
            }
        }

        class MinVarianceWithTargetReturn implements MultivariateFunction, Serializable {
            @Override
            public double value(double[] weight) {
                weight = normalizeWeight(weight);
                //  Alternative barrier method
                return getPortfolioVariance(cov, weight) + Math.abs(targetReturn - getWeightedReturn(weight, mean));
            }
        }

        class MinVariance implements MultivariateFunction, Serializable {
            @Override
            public double value(double[] weight) {
                weight = normalizeWeight(weight);
                return getPortfolioVariance(cov, weight);
            }
        }

        MultivariateFunction ret;
        switch (obj) {
            case MinVariance:
                ret = new MinVariance();
                break;
            case MaxSharpeRatio:
                ret = new MaxSharpeRatio();
                break;
            case MinVarianceWithTargetReturn:
                ret = new MinVarianceWithTargetReturn();
                break;
            default:
                throw new UndefinedParameterValueException("Unexpected value: " + obj, null);
        }
        return ret;
    }

    protected double[] normalizeWeight(double[] w) {
        double sum = Arrays.stream(w).map(Math::abs).sum();
        return Arrays.stream(w).map(e -> Math.abs(e) / sum).toArray();
    }
}
