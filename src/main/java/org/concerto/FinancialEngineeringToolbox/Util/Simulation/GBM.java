package org.concerto.FinancialEngineeringToolbox.Util.Simulation;

import org.concerto.FinancialEngineeringToolbox.Constant;

public class GBM {

    public static double[] staticPriceSimulation(double S0, double sigma, double T, double riskFreeRate , int simulationNumber) {
        return staticPriceSimulation(S0, sigma, T, riskFreeRate, simulationNumber, Constant.RANDOMSEED);
    }

    public static double[] staticPriceSimulation(double S0, double sigma, double T, double riskFreeRate , int simulationNumber, int randomSeed) {
        NormalizedGaussian NG = new NormalizedGaussian(simulationNumber, randomSeed);
        double[] ret = new double[simulationNumber];
        double[] r = NG.nextRandomVector();

        for(int i = 0 ; i < simulationNumber; i++ ) {
            ret[i] = S0 * Math.exp((riskFreeRate - 0.5 * sigma * sigma) * T + sigma * Math.sqrt(T) * r[i]) ;
        }
        return ret;
    }
}
