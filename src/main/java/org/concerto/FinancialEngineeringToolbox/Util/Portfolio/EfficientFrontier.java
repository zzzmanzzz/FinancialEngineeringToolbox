package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class EfficientFrontier extends PortfolioOptimization {
    public enum ObjectiveFunction{MaxSharpeRatio, MinVarianceWithTargetReturn, MinVariance}

    final private Map<String, double[]> data;
    private double[] mean;
    private double[][] cov;
    final private double riskFreeRate;
    final private String[] symbols;
    private double targetReturn;
    final private int frequency;

    EfficientFrontier(Map<String, double[]> data, double riskFreeRate, int frequency) throws ParameterIsNullException {
        this.data = data;
        this.frequency = frequency;
        Set<String> keys = data.keySet();
        symbols = keys.toArray(new String[keys.size()]);
        this.riskFreeRate = riskFreeRate;

        for(String s : symbols) {
            if(data.get(s) == null) {
                String msg = String.format("key(%s) has null value", s);
                throw new ParameterIsNullException(msg, null);
            }
        }
    }

    private void init(Constant.ReturnType type) throws UndefinedParameterValueException {
        Function<double[], double[]> funcRef = getReturnFunction(type);
        double[][] returns = new double[symbols.length][];

        for(int i = 0 ; i < symbols.length ; i++) {
            double[] tmp = data.get(symbols[i]);
            returns[i] = funcRef.apply(tmp);
        }

        returns = dropna(returns);

        mean = getMeanReturn(returns, frequency);
        cov = getCovariance(returns, frequency);
    }

    private Result getResult(double[] bestWeight) {
        double weightedReturns = getWeightedReturn(bestWeight, mean);
        double bestSharpeRatio = getWeightedSharpeRatio(bestWeight, mean, cov, riskFreeRate);
        double variance = Math.pow(((weightedReturns - riskFreeRate) / bestSharpeRatio), 2);
        return new Result(symbols, bestWeight, bestSharpeRatio, weightedReturns, variance);
    }

    public Result getMaxSharpeRatio(double[] upperBound, double[] lowerBound, double[] initGuess, Constant.ReturnType type) throws UndefinedParameterValueException, ParameterRangeErrorException, DimensionMismatchException {
        init(type);
        double[] bestWeight = optimize(upperBound, lowerBound, initGuess, getObjectiveFunction(ObjectiveFunction.MaxSharpeRatio), GoalType.MAXIMIZE);
        return getResult(bestWeight);
    }

    public Result getMinVarianceWithTargetReturn(double[] upperBound, double[] lowerBound, double[] initGuess, double targetReturn, Constant.ReturnType type) throws UndefinedParameterValueException, ParameterRangeErrorException, DimensionMismatchException {
        init(type);
        this.targetReturn = targetReturn;
        double[] bestWeight = optimize(upperBound, lowerBound, initGuess,getObjectiveFunction(ObjectiveFunction.MinVarianceWithTargetReturn), GoalType.MINIMIZE);
        return getResult(bestWeight);
    }

    public Result getMinVariance(double[] upperBound, double[] lowerBound, double[] initGuess,Constant.ReturnType type) throws UndefinedParameterValueException, ParameterRangeErrorException, DimensionMismatchException {
        init(type);
        double[] bestWeight = optimize(upperBound, lowerBound, initGuess, getObjectiveFunction(ObjectiveFunction.MinVariance), GoalType.MINIMIZE);
        return getResult(bestWeight);
    }

    private MultivariateFunction getObjectiveFunction(ObjectiveFunction obj) throws UndefinedParameterValueException {
        class MaxSharpeRatio implements MultivariateFunction, Serializable {
            @Override
            public double value(double[] weight) {
                weight = normalizeWeight(weight);
                //Use log due to poor optimize result
                return Math.log(getWeightedSharpeRatio(weight, mean, cov, riskFreeRate));
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

    protected CMAESOptimizer getOptimizer() {
        boolean isActiveCMA = true;
        int diagonalOnly = 1;
        int checkFeasibleCount = 0;
        boolean generateStatistics = false;
        RandomGenerator rg = new MersenneTwister(Constant.RANDOMSEED);
        SimpleValueChecker svc = new SimpleValueChecker(1e-8, 1e-15);

        return new CMAESOptimizer(
                Constant.MAXTRY,
                Constant.EPSILON,
                isActiveCMA,
                diagonalOnly,
                checkFeasibleCount,
                rg,
                generateStatistics,
                svc);
    }


    protected double[] optimize(double[] upperBound, double[] lowerBound, double[] initGuess, MultivariateFunction fun, GoalType goal) throws ParameterRangeErrorException, DimensionMismatchException {

        final int size = symbols.length;
        if(upperBound.length != size) {
            throw new DimensionMismatchException("upperBond length should be " + size, null);
        }
        if(lowerBound.length != size) {
            throw new DimensionMismatchException("lowerBond length should be " + size, null);
        }
        if(initGuess.length != size) {
            throw new DimensionMismatchException("initialGuess length should be " + size, null);
        }

        CMAESOptimizer optimizer = getOptimizer();

        double[] s = new double[size];

        for(int i = 0 ; i < size ; i++ ) {
            s[i] = ( upperBound[i] - lowerBound[i] ) / 3;
        }


        OptimizationData delta = new CMAESOptimizer.Sigma(s);
        OptimizationData popSize = new CMAESOptimizer.PopulationSize((int) (4 + Math.floor(3 * Math.log(size))));
        SimpleBounds bounds = new SimpleBounds(lowerBound, upperBound);
        MaxEval maxEval = new MaxEval(Constant.MAXTRY);

        PointValuePair solution =
                optimizer.optimize(
                        new InitialGuess(initGuess),
                        new org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction(fun),
                        goal,
                        bounds,
                        delta,
                        popSize,
                        maxEval
                );

        return normalizeWeight(solution.getPoint());
    }


}