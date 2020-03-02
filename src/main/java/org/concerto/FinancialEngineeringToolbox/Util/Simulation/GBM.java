package org.concerto.FinancialEngineeringToolbox.Util.Simulation;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Util.Simulation.RandomGenerator.NormalizedGaussian;

import java.util.Arrays;
import java.util.stream.IntStream;

public class GBM {

    public static double[] staticSimulate(double S0, double sigma, double T, double riskFreeRate , int simulationNumber) throws ParameterRangeErrorException {
        return staticSimulate(S0, sigma, T, riskFreeRate, simulationNumber, Constant.RANDOMSEED);
    }

    public static double[] staticSimulate(double S0, double sigma, double T, double riskFreeRate , int simulationNumber, int randomSeed) throws ParameterRangeErrorException {
        if(simulationNumber <= 0) {
            String msg = String.format("simulationNumber(%d) should > 0", simulationNumber);
            throw new ParameterRangeErrorException(msg, null);
        }
        NormalizedGaussian NG = new NormalizedGaussian(simulationNumber, randomSeed);
        double[] r = NG.nextRandomVector();

        return IntStream.range(0, simulationNumber).parallel().mapToDouble(
            i -> S0 * Math.exp((riskFreeRate - 0.5 * sigma * sigma) * T + sigma * Math.sqrt(T) * r[i])
        ).toArray();
    }

    public static double[][] dynamicSimulate(double S0, double sigma, double riskFreeRate , int simulationNumber, double deltaT, int steps, int randomSeed) throws ParameterRangeErrorException {
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
            ret[i] = IntStream.range(0, simulationNumber).mapToDouble(
                    j -> ret[finalI - 1][j] * Math.exp((riskFreeRate - 0.5 * sigma * sigma) * deltaT + sigma * tmp[j] * Math.sqrt(deltaT))
            ).toArray();
        }

        return ret;
    }

    public static double[][] dynamicSimulate(double S0, double sigma, double riskFreeRate , int simulationNumber, double deltaT, int steps) throws ParameterRangeErrorException {
        return dynamicSimulate(S0, sigma, riskFreeRate , simulationNumber, deltaT, steps, Constant.RANDOMSEED);
    }
}
