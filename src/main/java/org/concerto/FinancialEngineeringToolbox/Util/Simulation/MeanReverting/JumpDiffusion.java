package org.concerto.FinancialEngineeringToolbox.Util.Simulation.MeanReverting;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Util.Simulation.RandomGenerator.Generator;
import org.concerto.FinancialEngineeringToolbox.Util.Simulation.RandomGenerator.NormalizedGaussian;
import org.concerto.FinancialEngineeringToolbox.Util.Simulation.RandomGenerator.NormalizedPoisson;

import java.util.Arrays;

/**
 * Merton’s Jump-Diffusion Model
 *
 *
 *
 */
public class JumpDiffusion {

    public static double[][] dynamicSimulate(double S0, double sigma, double lambda, double riskFreeRate, double mu, double delta, double deltaT, int steps, int simulationNumber, int randomSeed) throws ParameterRangeErrorException {

        if(simulationNumber <= 0) {
            String msg = String.format("simulationNumber(%d) should > 0", simulationNumber);
            throw new ParameterRangeErrorException(msg, null);
        }

        if( steps <= 0 ) {
            String msg = String.format("steps(%d) should > 0", steps);
            throw new ParameterRangeErrorException(msg, null);
        }

        if( deltaT < 0.0 ) {
            String msg = String.format("deltaT(%f) should > 0", deltaT);
            throw new ParameterRangeErrorException(msg, null);
        }

        double poissonMean = lambda * deltaT;
        Generator NG = new NormalizedGaussian(simulationNumber, randomSeed);
        Generator NP = new NormalizedPoisson(poissonMean, simulationNumber, randomSeed);

        double rj = lambda * (Math.exp(mu + 0.5 * delta * delta) - 1);
        double[][] ret = new double[steps][simulationNumber];
        double[] init = new double[simulationNumber];
        Arrays.fill(init, S0);
        ret[0] = init;

        for(int i = 1 ; i < steps; i++) {
            double[] g1 = NG.nextRandomVector();
            double[] g2 = NG.nextRandomVector();
            double[] p = NP.nextRandomVector();

            for(int j = 0 ; j < simulationNumber; j++) {
                double t = ret[i-1][j] * (Math.exp((riskFreeRate - rj - 0.5 * sigma * sigma) * deltaT + sigma * Math.sqrt(deltaT) * g1[j]) + (Math.exp(mu + delta * g2[j]) - 1) * p[j]);
                ret[i][j] = Math.max(t, 0);
            }
        }
        return ret;
    }
    public static double[][] dynamicSimulate(double S0, double sigma, double lambda, double riskFreeRate, double mu, double delta, double deltaT, int steps, int simulationNumber) throws ParameterRangeErrorException {
        return dynamicSimulate(S0, sigma, lambda, riskFreeRate, mu, delta, deltaT, steps, simulationNumber, Constant.RANDOMSEED);
    }
}
