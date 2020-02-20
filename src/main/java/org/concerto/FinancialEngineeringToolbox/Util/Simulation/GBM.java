package org.concerto.FinancialEngineeringToolbox.Util.Simulation;

public class GBM {

    public static double[] staticPriceSimulation(double S0, double sigma, double T, double riskFreeRate , int simulationNumber) {
        NormalizedGaussian NG = new NormalizedGaussian(simulationNumber);
        double[] ret = new double[simulationNumber];
        double[] r = NG.nextRandomVector();

        for(int i = 0 ; i < simulationNumber; i++ ) {
            ret[i] = S0 * Math.exp((riskFreeRate - 0.5 * sigma * sigma) * T + sigma * Math.sqrt(T) * r[i]) ;
        }
        return ret;
    }
}
