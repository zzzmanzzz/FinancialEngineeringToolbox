package org.concerto.FinancialEngineeringToolbox.Util.Simulation.TreeModel.BinomialTree;

import org.concerto.FinancialEngineeringToolbox.Util.BlackScholes;

public class LeisenReimer extends AbstractBinomialTree {

    public LeisenReimer(double S0, double K, double sigma, double riskFreeRate, int N, double deltaT) {
        super(S0, K, sigma, riskFreeRate, N, deltaT);
        init();
    }

    @Override
    protected void init() {
        int odd = N % 2 == 0 ? N + 1 : N;
        double T = deltaT * N;
        BlackScholes b = new BlackScholes(S0, K, sigma, riskFreeRate, T, 0);
        double d1 = b.d_1();
        double d2 = b.d_2();
        double pbar = inversion(d1, odd);
        double p = inversion(d2, odd);
        discount = Math.exp(-riskFreeRate * deltaT);
        probabilityUp = p;
        probabilityDown = 1 - p;
        U = 1 / discount * pbar / p;
        D = ( ( 1 / discount ) - p * U ) / (1 - p) ;
    }

    private double inversion(double z, int n) {
        double tmp = 0.25 - 0.25 * Math.exp(
                -Math.pow(z / ( n + (1.0 / 3.0) + (0.1 / (n + 1.0) ) ) , 2) * (n + (1.0 / 6.0))
        );
        return 0.5 + Math.signum(z) * Math.sqrt(tmp);
    }
}
