package org.concerto.FinancialEngineeringToolbox.Util.Timeseries;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.CMAESOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;
import org.concerto.FinancialEngineeringToolbox.Util.Statistics.Profile;

import java.io.Serializable;
import java.util.Arrays;

public class GJRGARCH {
    //private static Logger logger = Logger.getLogger(GJRGARCH.class.getName());

    private double[] data;
    private double variance;
    private double mu;
    private double omega;
    private double alpha;
    private double gamma;
    private double beta;

    public double getMu() {
        return mu;
    }

    public double getOmega() {
        return omega;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getGamma() {
        return gamma;
    }

    public double getBeta() {
        return beta;
    }

    GJRGARCH(double[] data) {
        this.data = data;
        Profile p = new Profile(data);
        variance = Math.pow(p.getStdDev(), 2);
    }

    private double GJRG(double omega, double alpha, double gamma, double beta, double epsilon, double sigma2) {
        double indicator = epsilon < 0 ? 1 : 0;
        return omega + alpha * Math.pow(epsilon, 2)
                + gamma * Math.pow(epsilon, 2) * indicator + beta * sigma2;
    }

    /**
     *
     * @param mu The parameter &mu; of the GJR GARCH model
     * @param omega  The parameter &omega; of the GJR GARCH model
     * @param alpha The parameter &alpha; of the GJR GARCH model
     * @param gamma The parameter &gamma; of the GJR GARCH model
     * @param beta The parameter &beta; of the GJR GARCH model
     * @param sideEffectLogliks output for logliks
     *
     * @return double log likelihood
     */
    private double logLikelihood(double mu, double omega, double alpha, double gamma, double beta, double[] sideEffectLogliks) {

        int size = data.length;
        double[] sigma2 = new double[size];
        sigma2[0] = variance;

        double[] epsilon = Arrays.stream(data).map(i -> i - mu).toArray();

        for(int i = 1 ; i < size ; i++) {
            sigma2[i] = GJRG(omega, alpha, gamma, beta, epsilon[i-1], sigma2[i-1]);
        }

        double[] logliks = new double[size];
        for (int i = 0 ; i < size ; i++ ) {
           logliks[i] = -0.5 * (Math.log(2 * Math.PI) + Math.log(sigma2[i]) + Math.pow(epsilon[i], 2) / sigma2[i]);
        }

        sideEffectLogliks = logliks;
        return Arrays.stream(logliks).sum();
    }

    private CMAESOptimizer getOptimizer() {
        boolean isActiveCMA = true;
        int diagonalOnly = 0;
        int checkFeasibleCount = 0;
        boolean generateStatistics = false;
        RandomGenerator rg = new MersenneTwister(Constant.RANDOMSEED);
        SimpleValueChecker svc = new SimpleValueChecker(1e-6, 1e-10);

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

    private MultivariateFunction getObjFunction() {
        class Obj implements MultivariateFunction, Serializable {

            @Override
            public double value(double[] variables) {
                final double mu	= variables[0];
                final double omega = variables[1];
                final double alpha = variables[2];
                final double gamma = variables[3];
                final double beta = variables[4];

                double constrain = 1 - alpha - gamma / 2 - beta;

                if(constrain < 0) {
                    return -Double.MAX_VALUE;
                }

                double[] dummy = null;
                return logLikelihood(mu, omega,alpha, gamma, beta, dummy);
            }
        }

        return new Obj();
    }

    public void fit(double[] upperBond, double[] lowerBond, double[] initialGuess) throws DimensionMismatchException {
        final int size = 5;// 5 parameters
        if(upperBond.length != size) {
            throw new DimensionMismatchException("upperBond length should be " + size, null);
        }
        if(lowerBond.length != size) {
            throw new DimensionMismatchException("lowerBond length should be " + size, null);
        }
        if(initialGuess.length != size) {
            throw new DimensionMismatchException("initialGuess length should be " + size, null);
        }

        CMAESOptimizer optimizer = getOptimizer();

        // mu, omega, alpha,  gamma,  beta
        double[] s = new double[size];

        for(int i = 0 ; i < size ; i++ ) {
            s[i] = ( upperBond[i] - lowerBond[i] ) / 10000;
        }


        OptimizationData delta = new CMAESOptimizer.Sigma(s);
        OptimizationData popSize = new CMAESOptimizer.PopulationSize((int) (4 + Math.floor(3 * Math.log(size))));
        SimpleBounds bounds = new SimpleBounds(lowerBond, upperBond);
        MaxEval maxEval = new MaxEval(Constant.MAXTRY);

        PointValuePair solution =
                optimizer.optimize(
                        new InitialGuess(initialGuess),
                        new ObjectiveFunction(getObjFunction()),
                        GoalType.MAXIMIZE,
                        bounds,
                        delta,
                        popSize,
                        maxEval
                );

        double[] ret =  solution.getPoint();
        mu = ret[0];
        omega = ret[1];
        alpha = ret[2];
        gamma = ret[3];
        beta = ret[4];
    }

    public double predict(double variance, double epsilon) {
        return GJRG(omega, alpha, gamma, beta, epsilon, variance);
    }
}
