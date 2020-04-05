package org.concerto.FinancialEngineeringToolbox.Util.Simulation.TreeModel.BinomialTree;
public class CoxRossRubinstein extends AbstractBinomialTree {

    public CoxRossRubinstein(double S0, double K, double sigma, double riskFreeRate, int N, double deltaT) {
        super(S0, K, sigma, riskFreeRate, N, deltaT);
        init();
    }

    @Override
    protected void init() {
        U = Math.exp(sigma * Math.sqrt(deltaT));
        D = Math.exp(-sigma * Math.sqrt(deltaT));
        probabilityUp = ((Math.exp(riskFreeRate * deltaT)) - D ) / (U - D);
        probabilityDown = 1 - probabilityUp;
        discount = Math.exp(-riskFreeRate * deltaT);
    }
}
