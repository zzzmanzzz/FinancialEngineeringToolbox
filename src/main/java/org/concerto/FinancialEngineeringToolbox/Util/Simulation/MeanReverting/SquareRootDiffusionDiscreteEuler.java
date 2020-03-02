package org.concerto.FinancialEngineeringToolbox.Util.Simulation.MeanReverting;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Util.Simulation.RandomGenerator.NormalizedGaussian;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 *
 *
 *
 *
 *
 *
 */

public class SquareRootDiffusionDiscreteEuler {
    public static double[][] dynamicSimulate(double S0, double sigma, double kappa, double theta, double deltaT, int steps, int simulationNumber, int randomSeed) throws ParameterRangeErrorException {
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

        NormalizedGaussian NG = new NormalizedGaussian(simulationNumber, randomSeed);
        double[][] ret = new double[steps][simulationNumber];
        double[] init = new double[simulationNumber];

        Arrays.fill(init, S0);
        ret[0] = init;

        for(int i = 1 ; i < steps; i++) {
            double[] tmp = NG.nextRandomVector();
            int finalI = i;
            ret[i] = IntStream.range( 0,  simulationNumber).mapToDouble(
                    j -> {
                        double t = ret[finalI -1][j] + kappa * (theta - Math.max(ret[finalI -1][j], 0)) * deltaT + sigma * Math.sqrt(Math.max(ret[finalI -1][j], 0)) * Math.sqrt(deltaT) * tmp[j];
                        return Math.max(t, 0);
                    }).toArray();
        }
        return ret;
    }
    public static double[][] dynamicSimulate(double S0, double sigma, double kappa, double theta, double deltaT, int steps, int simulationNumber) throws ParameterRangeErrorException {
        return dynamicSimulate(S0, sigma, kappa, theta, deltaT, steps, simulationNumber, Constant.RANDOMSEED);
    }

}
