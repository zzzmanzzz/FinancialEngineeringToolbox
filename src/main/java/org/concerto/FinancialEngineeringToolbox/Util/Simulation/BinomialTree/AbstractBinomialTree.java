package org.concerto.FinancialEngineeringToolbox.Util.Simulation.BinomialTree;

public abstract class AbstractBinomialTree {
    protected double S0;
    protected double K;
    protected double sigma;
    protected double riskFreeRate;
    protected double deltaT;
    protected int N;

    protected double probabilityUp;
    protected double probabilityDown;
    protected double discount;
    protected double U;
    protected double D;


    public AbstractBinomialTree(double S0, double K, double sigma, double riskFreeRate, int N, double deltaT) {
        this.S0 = S0;
        this.K = K;
        this.sigma = sigma;
        this.riskFreeRate = riskFreeRate;
        this.deltaT = deltaT;
        this.N = N;
    }

    abstract protected void init();

    public double getS0() {
        return S0;
    }

    public void setS0(double s0) {
        S0 = s0;
    }

    public double getK() {
        return K;
    }

    public void setK(double k) {
        K = k;
        init();
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
        init();
    }

    public double getRiskFreeRate() {
        return riskFreeRate;
    }

    public void setRiskFreeRate(double riskFreeRate) {
        this.riskFreeRate = riskFreeRate;
        init();
    }

    public double getDeltaT() {
        return deltaT;
    }

    public void setDeltaT(double deltaT) {
        this.deltaT = deltaT;
        init();
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
        init();
    }

    public double getProbabilityUp() {
        return probabilityUp;
    }

    public double getProbabilityDown() {
        return probabilityDown;
    }

    public double getDiscount() {
        return discount;
    }

    public double getU() {
        return U;
    }

    public double getD() {
        return D;
    }

    public double getFairPrice(String optionType, boolean[] strikeSchedule) {

        double[] St = new double[N+1];
        double[] C = new double[N+1];

        St[0] = S0 * Math.pow(D, N);

        for (int i = 1 ; i < N + 1 ; i++) {
            St[i] = St[i - 1] * U / D;
        }

        for ( int i = 1 ; i < N + 1; i++ ) {
            if(optionType.equals("put")){
                C[i] = Math.max(K - St[i], 0);
            } else if(optionType.equals("call")) {
                C[i] = Math.max(St[i] - K, 0);
            }
        }

        for (int i = N ; i > 0 ; i--) {
            for (int j = 0; j < i; j++) {
                C[j] = discount * (probabilityUp * C[j + 1] + probabilityDown * C[j]);
            }
        }

        return C[0];
    }

}
