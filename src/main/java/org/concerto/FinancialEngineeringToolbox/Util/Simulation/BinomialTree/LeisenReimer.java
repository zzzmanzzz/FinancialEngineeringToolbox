package org.concerto.FinancialEngineeringToolbox.Util.Simulation.BinomialTree;

public class LeisenReimer extends AbstractBinomialTree {

    public LeisenReimer(double S0, double K, double sigma, double riskFreeRate, int N, double deltaT) {
        super(S0, K, sigma, riskFreeRate, N, deltaT);
        init();
    }

    @Override
    protected void init() {
        int n = N % 2 == 0 ? N : N + 1;
        double d1 = (Math.log(S0 / K) + (riskFreeRate + sigma * sigma / 2) * N * deltaT) / (sigma * Math.sqrt(N * deltaT));
        double d2 = d1 - sigma * Math.sqrt(N * deltaT);
        double pbar = inversion(d1, n);
        double p = inversion(d2, n);

        discount = Math.exp(-riskFreeRate * deltaT);
        probabilityUp = p;
        probabilityDown = 1 - p;
        U = 1 / discount * pbar / p;
        D = ( 1 / discount - p * U ) / (1 - p);
    }

    private double inversion(double z, int n) {
        double tmp = 0.25 - 0.25 * Math.exp(
                -Math.pow(z / ( n + (1.0 / 3.0) + (0.1 / (n + 1.0) ) ) , 2) * (n + (1.0 / 6.0))
        );
        return 0.5 + Math.signum(z) * Math.sqrt(tmp);
    }
}
