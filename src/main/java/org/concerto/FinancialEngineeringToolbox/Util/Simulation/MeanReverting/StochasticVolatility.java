package org.concerto.FinancialEngineeringToolbox.Util.Simulation.MeanReverting;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Util.Simulation.RandomGenerator.CorrelatedGaussianVector;
import org.concerto.FinancialEngineeringToolbox.Util.Simulation.Result;

import java.util.Arrays;

/**
 * A Closed-Form Solution for Options with Stochastic Volatility with Applications to Bond and Currency Options
 * Steven L. Heston (1993)
 *
 *
 *
 *
 *
 */
public class StochasticVolatility {
    public static Result dynamicSimulate(double S0, double V0, double sigma, double riskFreeRate, double kappa, double theta, double rho, double deltaT, int steps, int simulationNumber, int randomSeed) throws ParameterRangeErrorException {

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

        double[][] cov = {{1, rho}, {rho, 1}};

        CorrelatedGaussianVector CG = new CorrelatedGaussianVector(cov, Constant.RANDOMSEED);

        double[][] indexLevel = new double[steps][simulationNumber];
        double[] indexLevelInit = new double[simulationNumber];

        double[][] volatility = new double[steps][simulationNumber];
        double[] volatilityInit = new double[simulationNumber];

        Arrays.fill(indexLevelInit, S0);
        indexLevel[0] = indexLevelInit;

        Arrays.fill(volatilityInit, V0);
        volatility[0] = volatilityInit;

        for(int i = 1 ; i < steps; i++) {
            double[][] tmp = CG.nextRandomVector(simulationNumber);
            for(int j = 0 ; j < simulationNumber; j++) {
                double v =  volatility[i - 1][j] + kappa * (theta - Math.max(volatility[i-1][j], 0)) * deltaT + sigma * Math.sqrt(Math.max(volatility[i-1][j], 0)) * Math.sqrt(deltaT) * tmp[0][j];
                v = Math.max(v, 0);
                volatility[i][j] = v;
                indexLevel[i][j] = indexLevel[i - 1][j] * Math.exp((riskFreeRate - 0.5 * v) * deltaT + Math.sqrt(v) * tmp[1][j] * Math.sqrt(deltaT));
            }
        }

        Result ret = new Result();
        ret.setIndexLevel(indexLevel);
        ret.setVolatility(volatility);
        return ret;
    }
}
