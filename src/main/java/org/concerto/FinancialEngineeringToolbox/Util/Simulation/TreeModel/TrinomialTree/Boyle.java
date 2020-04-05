package org.concerto.FinancialEngineeringToolbox.Util.Simulation.TreeModel.TrinomialTree;

public class Boyle extends AbstractTrinomialTree {
    public Boyle(double S0, double K, double sigma, double riskFreeRate, int N, double deltaT) {
        super(S0, K, sigma, riskFreeRate, N, deltaT);
        init();
    }

    @Override
    protected void init() {
        double dividend = 0; // TODO add dividend for all tree model
        U = Math.exp(sigma * Math.sqrt(2 * deltaT));
        D = 1 / U;
        M = 1;

        double a0 = Math.exp(deltaT * (riskFreeRate - dividend) / 2);
        double a1 = Math.exp(sigma * Math.sqrt(deltaT / 2));
        double denominator = a1 - 1 / a1;

        probabilityUp = Math.pow((a0 - 1 / a1) / denominator, 2);
        probabilityDown = Math.pow((-a0 + a1 ) / denominator, 2);
        probabilityMiddle = 1 - probabilityDown - probabilityUp;
        discount = Math.exp(-riskFreeRate * deltaT);
    }

}
