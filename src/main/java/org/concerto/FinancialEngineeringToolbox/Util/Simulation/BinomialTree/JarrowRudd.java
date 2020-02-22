package org.concerto.FinancialEngineeringToolbox.Util.Simulation.BinomialTree;

public class JarrowRudd extends AbstractBinomialTree {
    public JarrowRudd(double S0, double K, double sigma, double riskFreeRate, int N, double deltaT) {
        super(S0, K, sigma, riskFreeRate, N, deltaT);
        init();
    }

    @Override
    protected void init() {
        U = Math.exp((riskFreeRate - (sigma * sigma / 2)) * deltaT + sigma * Math.sqrt(deltaT));
        D = Math.exp((riskFreeRate - (sigma * sigma / 2)) *  deltaT - sigma * Math.sqrt(deltaT));
        probabilityUp = 0.5;
        probabilityDown = 1 - probabilityUp;
        discount = Math.exp(-riskFreeRate * deltaT);
    }
}
