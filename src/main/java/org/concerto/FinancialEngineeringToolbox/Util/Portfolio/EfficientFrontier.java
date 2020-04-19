package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionGradient;
import org.apache.commons.math3.optim.nonlinear.scalar.gradient.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BOBYQAOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.*;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Find optimized portfolio weight via Markowitz and Black-Litterman theory
 */
public class EfficientFrontier extends PortfolioOptimization {
    protected static Logger logger = Logger.getLogger(EfficientFrontier.class.getName());
    private double[][] returns;
    private double[][] cov;
    private double[] mean;

    public enum ObjectiveFunction{MaxSharpeRatio, MinVarianceWithTargetReturn, MinVariance}

    /**
     *
     * @param data Price data of portfolio elements
     * @param riskFreeRate Risk free rate
     * @param type return type, log return or percentage return
     * @param frequency frequency of return, this value should correspond to risk free rate and market return,
     *                  you can use 252 trading days for yearly risk free rate and daily return.
     * @throws ParameterIsNullException Any parameter is null
     * @throws UndefinedParameterValueException When type is not common or log.
     */
    EfficientFrontier(Map<String, double[]> data, double riskFreeRate, Constant.ReturnType type, int frequency) throws ParameterIsNullException, UndefinedParameterValueException {
        super(data, riskFreeRate, frequency);
        returns = getReturns(type);
        mean = getMeanReturn(returns, frequency);
        cov = getCovariance(returns, frequency);
    }

    /**
     * Get covariance
     * @return covariance
     */
    public double[][] getCovariance() {
        return cov;
    }

    /**
     * Generate final result
     * @param mean mean return of portfolio
     * @param cov covariance of portfolio
     * @param bestWeight Optimized portfolio weight
     * @return Result POJO
     */
    private Result getResult(double[]mean, double[][] cov, double[] bestWeight) {
        double weightedReturns = getWeightedReturn(bestWeight, mean);
        double bestSharpeRatio = getWeightedSharpeRatio(bestWeight, mean, cov, riskFreeRate);
        double variance = Math.pow(((weightedReturns - riskFreeRate) / bestSharpeRatio), 2);
        return new Result(symbols, bestWeight, bestSharpeRatio, weightedReturns, variance);
    }

    /**
     * Get optimized portfolio weight at max Sharpe ratio via Markowitz theory
     * @param upperBound Weight upper bond
     * @param lowerBound Weight lower bond
     *                   The two bonds will be normalized to [0, 1]
     * @param initGuess Initialize Guessing weight
     * @return Result POJO
     * @throws UndefinedParameterValueException
     */
    public Result getMaxSharpeRatio(double[] upperBound, double[] lowerBound, double[] initGuess) throws UndefinedParameterValueException {
        double[] bestWeight = BOBYQAOptimize(upperBound, lowerBound, initGuess, getObjectiveFunction(mean, cov, ObjectiveFunction.MaxSharpeRatio), GoalType.MAXIMIZE);
        return getResult(mean, cov, bestWeight);
    }

    /**
     * Get optimized portfolio weight at max Sharpe ratio via Black-Litterman theory.
     * @param upperBound Weight upper bond
     * @param lowerBound Weight lower bond
     *                   The two bonds will be normalized to [0, 1]
     * @param initGuess Initialize Guessing weight
     * @param P
     * @param marketCap
     * @param Q
     * @param Omega Customized certainty
     * @param tau
     * @param marketMeanReturn
     * @param marketVariance
     * @return
     * @throws UndefinedParameterValueException
     * @throws DateFormatException
     * @throws ParameterRangeErrorException
     * @throws DimensionMismatchException
     * @throws ParameterIsNullException
     */
    public Result getMaxSharpeRatio(double[] upperBound, double[] lowerBound, double[] initGuess, Map<String, double[]> P, Map<String, Double> marketCap, double[] Q, double[] Omega, double tau, double marketMeanReturn, double marketVariance) throws UndefinedParameterValueException, DateFormatException, ParameterRangeErrorException, DimensionMismatchException, ParameterIsNullException {
        DataProcessor.validateOmega(Q, Omega);

        double[][] p = DataProcessor.parseP(P, Q, data);

        double[][] BLcov = getBLCovariance(cov, p, Omega, tau);
        double[] BLmean = getBLmean(cov, p, Omega, marketCap, Q, tau, marketMeanReturn, marketVariance);
        double[] bestWeight = BOBYQAOptimize(upperBound, lowerBound, initGuess, getObjectiveFunction(BLmean, BLcov, ObjectiveFunction.MaxSharpeRatio), GoalType.MAXIMIZE);
        return getResult(BLmean, BLcov, bestWeight);
    }

    /**
     * Get optimized portfolio weight at max Sharpe ratio via Black-Litterman theory.
     * @param upperBound Weight upper bond
     * @param lowerBound Weight lower bond
     *                   The two bonds will be normalized to [0, 1]
     * @param initGuess Initialize Guessing weight
     * @param P
     * @param marketCap
     * @param Q
     * @param tau
     * @param marketMeanReturn
     * @param marketVariance
     * @return
     * @throws UndefinedParameterValueException
     * @throws DateFormatException
     * @throws ParameterRangeErrorException
     * @throws DimensionMismatchException
     * @throws ParameterIsNullException
     */
    public Result getMaxSharpeRatio(double[] upperBound, double[] lowerBound, double[] initGuess, Map<String, double[]> P, Map<String, Double> marketCap, double[] Q, double tau, double marketMeanReturn, double marketVariance) throws UndefinedParameterValueException, DateFormatException, ParameterRangeErrorException, DimensionMismatchException, ParameterIsNullException {
        double[][] p = DataProcessor.parseP(P, Q, data);
        double[] Omega = BlackLitterman.getOmega(cov, p, tau);
        double[][] BLcov = getBLCovariance(cov, p, Omega, tau);
        double[] BLmean = getBLmean(cov, p, Omega, marketCap, Q, tau, marketMeanReturn, marketVariance);
        double[] bestWeight = BOBYQAOptimize(upperBound, lowerBound, initGuess, getObjectiveFunction(BLmean, BLcov, ObjectiveFunction.MaxSharpeRatio), GoalType.MAXIMIZE);
        return getResult(BLmean, BLcov, bestWeight);
    }

    /**
     * Get optimized portfolio weight at min variance with target return via Black-Litterman theory.
     * @param upperBound Weight upper bond
     * @param lowerBound Weight lower bond
     *                   The two bonds will be normalized to [0, 1]
     * @param initGuess Initialize Guessing weight
     * @param targetReturn Target return of portfolio, the actual optimized return will near this value
     * @param P
     * @param marketCap
     * @param Q
     * @param Omega
     * @param tau
     * @param marketMeanReturn
     * @param marketVariance
     * @return
     * @throws UndefinedParameterValueException
     * @throws ParameterRangeErrorException
     * @throws DimensionMismatchException
     * @throws DateFormatException
     * @throws ParameterIsNullException
     */
    public Result getMinVarianceWithTargetReturn(double[] upperBound, double[] lowerBound, double[] initGuess, double targetReturn, Map<String, double[]> P, Map<String, Double> marketCap, double[] Q, double[] Omega, double tau, double marketMeanReturn, double marketVariance) throws UndefinedParameterValueException, ParameterRangeErrorException, DimensionMismatchException, DateFormatException, ParameterIsNullException {
        this.targetReturn = targetReturn;
        DataProcessor.validateOmega(Q, Omega);

        double[][] p = DataProcessor.parseP(P, Q, data);

        double[][] BLcov = getBLCovariance(cov, p, Omega, tau);
        double[] BLmean = getBLmean(cov, p, Omega, marketCap, Q, tau, marketMeanReturn, marketVariance);
        double[] bestWeight = optimize(upperBound, lowerBound, initGuess,getObjectiveFunction(BLmean, BLcov, ObjectiveFunction.MinVarianceWithTargetReturn), GoalType.MINIMIZE);
        return getResult(BLmean, BLcov, bestWeight);
    }

    /**
     * Get optimized portfolio weight at min variance with target return via Black-Litterman theory.
     * @param upperBound Weight upper bond
     * @param lowerBound Weight lower bond
     *                   The two bonds will be normalized to [0, 1]
     * @param initGuess Initialize Guessing weight
     * @param targetReturn Target return of portfolio, the actual optimized return will near this value
     * @param P
     * @param marketCap
     * @param Q
     * @param tau
     * @param marketMeanReturn
     * @param marketVariance
     * @return
     * @throws UndefinedParameterValueException
     * @throws ParameterRangeErrorException
     * @throws DimensionMismatchException
     */
    public Result getMinVarianceWithTargetReturn(double[] upperBound, double[] lowerBound, double[] initGuess, double targetReturn, Map<String, double[]> P, Map<String, Double> marketCap, double[] Q, double tau, double marketMeanReturn, double marketVariance) throws UndefinedParameterValueException, ParameterRangeErrorException, DimensionMismatchException, ParameterIsNullException {
        this.targetReturn = targetReturn;
        double[][] p = DataProcessor.parseP(P, Q, data);
        double[] Omega = BlackLitterman.getOmega(cov, p, tau);
        double[][] BLcov = getBLCovariance(cov, p, Omega, tau);
        double[] BLmean = getBLmean(cov, p, Omega, marketCap, Q, tau, marketMeanReturn, marketVariance);
        double[] bestWeight = optimize(upperBound, lowerBound, initGuess,getObjectiveFunction(BLmean, BLcov, ObjectiveFunction.MinVarianceWithTargetReturn), GoalType.MINIMIZE);
        return getResult(BLmean, BLcov, bestWeight);
    }

    /**
     * Get optimized portfolio weight at min variance with target return via Markowitz theory.
     * @param upperBound Weight upper bond
     * @param lowerBound Weight lower bond
     *                   The two bonds will be normalized to [0, 1]
     * @param initGuess Initialize Guessing weight
     * @param targetReturn Target return of portfolio, the actual optimized return will near this value
     * @return
     * @throws UndefinedParameterValueException
     * @throws ParameterRangeErrorException
     * @throws DimensionMismatchException
     */
    public Result getMinVarianceWithTargetReturn(double[] upperBound, double[] lowerBound, double[] initGuess, double targetReturn) throws UndefinedParameterValueException, ParameterRangeErrorException, DimensionMismatchException {
        this.targetReturn = targetReturn;
        double[] bestWeight = optimize(upperBound, lowerBound, initGuess,getObjectiveFunction(mean, cov, ObjectiveFunction.MinVarianceWithTargetReturn), GoalType.MINIMIZE);
        return getResult(mean, cov, bestWeight);
    }

    /**
     * Get optimized portfolio weight at min variance with target return via Black Litterman theory.
     * @param upperBound Weight upper bond
     * @param lowerBound Weight lower bond
     *                   The two bonds will be normalized to [0, 1]
     * @param initGuess Initialize Guessing weight
     * @param P
     * @param marketCap
     * @param Q
     * @param Omega
     * @param tau
     * @param marketMeanReturn
     * @param marketVariance
     * @return
     * @throws UndefinedParameterValueException
     * @throws DateFormatException
     * @throws ParameterRangeErrorException
     * @throws DimensionMismatchException
     * @throws ParameterIsNullException
     */
    public Result getMinVariance(double[] upperBound, double[] lowerBound, double[] initGuess, Map<String, double[]> P, Map<String, Double> marketCap, double[] Q, double[] Omega, double tau, double marketMeanReturn, double marketVariance) throws UndefinedParameterValueException, DateFormatException, ParameterRangeErrorException, DimensionMismatchException, ParameterIsNullException {
        DataProcessor.validateOmega(Q, Omega);
        double[][] p = DataProcessor.parseP(P, Q, data);
        double[][] BLcov = getBLCovariance(cov, p, Omega, tau);
        double[] BLmean = getBLmean(cov, p, Omega, marketCap, Q, tau, marketMeanReturn, marketVariance);
        double[] bestWeight = optimize(upperBound, lowerBound, initGuess, getObjectiveFunction(BLmean, BLcov, ObjectiveFunction.MinVariance), GoalType.MINIMIZE);
        return getResult(BLmean, BLcov, bestWeight);
    }

    /**
     * Get optimized portfolio weight at min variance with target return via Black Litterman theory.
     * @param upperBound Weight upper bond
     * @param lowerBound Weight lower bond
     *                   The two bonds will be normalized to [0, 1]
     * @param initGuess Initialize Guessing weight
     * @param P
     * @param marketCap
     * @param Q
     * @param tau
     * @param marketMeanReturn
     * @param marketVariance
     * @return
     * @throws UndefinedParameterValueException
     * @throws DateFormatException
     * @throws ParameterRangeErrorException
     * @throws DimensionMismatchException
     * @throws ParameterIsNullException
     */
    public Result getMinVariance(double[] upperBound, double[] lowerBound, double[] initGuess, Map<String, double[]> P, Map<String, Double> marketCap, double[] Q, double tau, double marketMeanReturn, double marketVariance) throws UndefinedParameterValueException, DateFormatException, ParameterRangeErrorException, DimensionMismatchException, ParameterIsNullException {
        double[][] p = DataProcessor.parseP(P, Q, data);
        double[] Omega = BlackLitterman.getOmega(cov, p, tau);
        double[][] BLcov = getBLCovariance(cov, p, Omega, tau);
        double[] BLmean = getBLmean(cov, p, Omega, marketCap, Q, tau, marketMeanReturn, marketVariance);
        double[] bestWeight = optimize(upperBound, lowerBound, initGuess, getObjectiveFunction(BLmean, BLcov, ObjectiveFunction.MinVariance), GoalType.MINIMIZE);
        return getResult(BLmean, BLcov, bestWeight);
    }

    /**
     * Get optimized portfolio weight at min variance via Markowitz theory.
     * @param upperBound Weight upper bond
     * @param lowerBound Weight lower bond
     *                   The two bonds will be normalized to [0, 1]
     * @param initGuess Initialize Guessing weight
     * @param upperBound
     * @param lowerBound
     * @param initGuess
     * @param common
     * @return
     * @throws UndefinedParameterValueException
     * @throws ParameterRangeErrorException
     * @throws DimensionMismatchException
     */
    public Result getMinVariance(double[] upperBound, double[] lowerBound, double[] initGuess, Constant.ReturnType common) throws UndefinedParameterValueException, ParameterRangeErrorException, DimensionMismatchException {
        double[] bestWeight = optimize(upperBound, lowerBound, initGuess, getObjectiveFunction(mean, cov, ObjectiveFunction.MinVariance), GoalType.MINIMIZE);
        return getResult(mean, cov, bestWeight);
    }

    protected NonLinearConjugateGradientOptimizer getAnotherOptimizer() {
        return new NonLinearConjugateGradientOptimizer(
                NonLinearConjugateGradientOptimizer.Formula.POLAK_RIBIERE,
            new SimpleValueChecker(1e-8, 1e-15));
    }

    protected BOBYQAOptimizer getBOBYQAOptimizer(int dim) {
        final int numIterpolationPoints = 2 * dim + 1;
        return new BOBYQAOptimizer(numIterpolationPoints);
    }

    protected CMAESOptimizer getCMAESOptimizer() {
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

    protected double[] BOBYQAOptimize(double[] upperBound, double[] lowerBound, double[] initGuess, MultivariateFunction fun, GoalType goal) {
        BOBYQAOptimizer optimizer = getBOBYQAOptimizer(initGuess.length);
        MaxEval maxEval = new MaxEval(Constant.MAXTRY);
        SimpleBounds bounds = new SimpleBounds(lowerBound, upperBound);
        PointValuePair solution = optimizer.optimize(
            new InitialGuess(initGuess),
            new org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction(fun),
            goal,
            bounds,
            maxEval
        );
        return normalizeWeight(solution.getPoint());
    }

    protected double[] optimize(double[] initGuess, MultivariateFunction fun, MultivariateVectorFunction gfun, GoalType goal) throws DimensionMismatchException {
        final int size = symbols.length;
        if(initGuess.length != size) {
            throw new DimensionMismatchException("initialGuess length should be " + size, null);
        }

        NonLinearConjugateGradientOptimizer optimizer = getAnotherOptimizer();
        MaxEval maxEval = new MaxEval(Constant.MAXTRY);

        PointValuePair solution =
            optimizer.optimize(
                maxEval,
                new InitialGuess(initGuess),
                new org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction(fun),
                new ObjectiveFunctionGradient(gfun),
                goal
            );

        return normalizeWeight(solution.getPoint());
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

        CMAESOptimizer optimizer = getCMAESOptimizer();

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