package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.DateFormatException;
import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;

import java.util.Arrays;
import java.util.Map;


public class MinPortfolioVariance extends PortfolioOptimization {

    public MinPortfolioVariance(Map<String, double[]> data, double riskFreeRate, int frequency) throws ParameterIsNullException {
        super(data, riskFreeRate, frequency);
    }

    final public Result getMarkowitzOptimizeResult(Map<String, double[]> data, Map<String, double[]> P, Map<String, Double> marketCap, double[] Q, double[][] Omega, double tau, double marketMeanReturn, double marketVariance,Constant.ReturnType type, double riskFreeRate, int frequency) throws ParameterIsNullException, UndefinedParameterValueException, DimensionMismatchException, DateFormatException {
        String[] keys = DataProcessor.getDataKey(data);
        DataProcessor.validateData(data, keys);
        DataProcessor.validateOmega(Q, Omega);

        double[][] returns = getReturns(type);
        double[][] p = DataProcessor.parseP(P, Q, data);
        double[][] cov = getCovariance(returns, frequency);
        double[][] omega = Omega;
        double[] marketC = DataProcessor.parseMarketCap(marketCap, data);
        double riskAversion = BlackLitterman.getMarketImpliedRiskAversion(marketMeanReturn, marketVariance, riskFreeRate);
        double[] priorReturns = BlackLitterman.getPriorReturns(cov, riskAversion,  riskFreeRate, marketC);

        double[][] BLcov = BlackLitterman.getBLCovariance(cov, p, omega, tau);
        double[] BLmean = BlackLitterman.getBLMeanReturn(priorReturns, cov, Q, p, omega, tau);

        double[] weight = optimize(BLcov);
        double weightedReturn = getWeightedReturn(weight, BLmean);
        double portfolioVariance = getPortfolioVariance(BLcov, weight);
        double sharpeRatio = getWeightedSharpeRatio(weight, BLmean, BLcov, riskFreeRate);
        return new Result(keys, weight, sharpeRatio, weightedReturn, portfolioVariance);
    }
    final public Result getMarkowitzOptimizeResult(Map<String, double[]> data, Map<String, double[]> P, Map<String, Double> marketCap, double[] Q, double tau, double marketMeanReturn, double marketVariance,Constant.ReturnType type, double riskFreeRate, int frequency) throws ParameterIsNullException, UndefinedParameterValueException, DimensionMismatchException {
        String[] keys = DataProcessor.getDataKey(data);
        DataProcessor.validateData(data, keys);

        double[][] returns = getReturns(type);
        double[][] p = DataProcessor.parseP(P, Q, data);
        double[][] cov = getCovariance(returns, frequency);
        double[][] omega = BlackLitterman.getOmega(cov, p, tau);
        double[] marketC = DataProcessor.parseMarketCap(marketCap, data);
        double riskAversion = BlackLitterman.getMarketImpliedRiskAversion(marketMeanReturn, marketVariance, riskFreeRate);
        double[] priorReturns = BlackLitterman.getPriorReturns(cov, riskAversion,  riskFreeRate, marketC);

        double[][] BLcov = BlackLitterman.getBLCovariance(cov, p, omega, tau);
        double[] BLmean = BlackLitterman.getBLMeanReturn(priorReturns, cov, Q, p, omega, tau);


        double[] weight = optimize(BLcov);
        double weightedReturn = getWeightedReturn(weight, BLmean);
        double portfolioVariance = getPortfolioVariance(BLcov, weight);
        double sharpeRatio = getWeightedSharpeRatio(weight, BLmean, BLcov, riskFreeRate);
        return new Result(keys, weight, sharpeRatio, weightedReturn, portfolioVariance);
    }

    final public Result getMarkowitzOptimizeResult(Constant.ReturnType type) throws ParameterIsNullException, UndefinedParameterValueException {
        String[] keys = DataProcessor.getDataKey(data);
        DataProcessor.validateData(data, keys);

        double[][] returns = getReturns(type);

        double[][] cov = getCovariance(returns, frequency);
        double[] mean = getMeanReturn(returns, frequency);
        double[] weight = optimize(cov);
        double weightedReturn = getWeightedReturn(weight, mean);
        double portfolioVariance = getPortfolioVariance(cov, weight);
        double sharpeRatio = getWeightedSharpeRatio(weight, mean, cov, riskFreeRate);
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

    protected double[] optimize(double[][] cov) {
        RealMatrix A = initA(cov);
        RealMatrix b = initB(cov.length + 1);
        double[] z = MatrixUtils.inverse(A).multiply(b).getColumn(0);
        return Arrays.copyOfRange(z, 0, z.length - 1);
    }

}
