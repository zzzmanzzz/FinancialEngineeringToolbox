package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
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

public abstract class PortfolioOptimization {
    static final protected Mean m = new Mean();
    protected Map<String, double[]> data;
    protected String[] symbols;
    protected int frequency;

    protected double riskFreeRate;
    protected double[] mean;
    protected double[][] cov;
    protected double targetReturn;


    PortfolioOptimization() {}

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

    final protected double[][] dropna(double[][] in) {
        Set<Long> skipLine = new HashSet<>();

        for (double[] doubles : in) {
            for (int j = 0; j < doubles.length; j++) {
                if (Double.isNaN(doubles[j])) {
                    skipLine.add(new Long(j));
                }
            }
        }

       double[][] ret = new double[in.length][];

       for (int i = 0 ; i < in.length; i++) {
            List<Double> tmp = new LinkedList<>();
           for(int j = 0 ; j < in[i].length ; j++ ) {
               if(!skipLine.contains(new Long(j))) {
                   tmp.add(in[i][j]);
               }
           }
           ret[i] = tmp.stream().mapToDouble(Double::doubleValue).toArray();
        }
        return ret;
    }

    protected MultivariateFunction getObjectiveFunction(EfficientFrontier.ObjectiveFunction obj) throws UndefinedParameterValueException {
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

    protected MultivariateVectorFunction getObjectiveFunctionGradient(EfficientFrontier.ObjectiveFunction obj) throws UndefinedParameterValueException {
        class MaxSharpeRatio implements MultivariateVectorFunction, Serializable {
            @Override
            public double[] value(double[] weight) {
                weight = normalizeWeight(weight);
                RealMatrix mcov = new Array2DRowRealMatrix(cov);
                RealMatrix m = new Array2DRowRealMatrix(mean);
                RealMatrix w = new Array2DRowRealMatrix(weight);

                double wcov = getPortfolioVariance(cov, weight);
                double riskPremium = getWeightedReturn(weight, mean) - riskFreeRate;
                RealMatrix right = mcov.multiply(w).scalarMultiply(riskPremium);
                RealMatrix ret = m.scalarMultiply(wcov).add(right.scalarMultiply(-1)).scalarMultiply(Math.pow(wcov, -2));
                return ret.getColumn(0);
            }
        }

        class MinVarianceWithTargetReturn implements MultivariateVectorFunction, Serializable {
            @Override
            public double[] value(double[] weight) {
                weight = normalizeWeight(weight);
                RealMatrix mcov = new Array2DRowRealMatrix(cov);
                RealMatrix m = new Array2DRowRealMatrix(mean);
                RealMatrix w = new Array2DRowRealMatrix(weight);
                double delta = targetReturn - getWeightedReturn(weight, mean);
                double[] ret;
                int sign = Double.compare(delta, 0.0) > 0 ?  -1 : 1;
                ret = mcov.multiply(w).add(m.scalarMultiply(sign)).getColumn(0);

                return ret;
            }
        }

        class MinVariance implements MultivariateVectorFunction, Serializable {
            @Override
            public double[] value(double[] weight) {
                weight = normalizeWeight(weight);
                RealMatrix mcov = new Array2DRowRealMatrix(cov);
                RealMatrix w = new Array2DRowRealMatrix(weight);
                return mcov.multiply(w.transpose()).getColumn(0);
            }
        }

        MultivariateVectorFunction ret;
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
