package org.concerto.FinancialEngineeringToolbox.Util;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.concerto.FinancialEngineeringToolbox.Constant;


public class BlackScholes {
    private double S;
    private double K;
    private double sigma;
    private double r; // continuously compounded risk-free interest rate
    private double t;
    private double y; // continuously compounded dividend yield
    private final NormalDistribution n;
    private double d_1;
    private double d_2;
    private double tradingDays; // default 252

    public BlackScholes(double S, double K, double sigma, double r, double t, double y) {
        this.S = S;
        this.K = K;
        this.sigma = sigma;
        this.r = r;
        this.t = t;
        this.y = y;
        this.tradingDays = Constant.TRADINGDAYS;
        this.n = new NormalDistribution(0.0, 1.0);
        this.d_1 = d_1();
        this.d_2 = d_2();
    }

    public double getTradingDays() {
        return tradingDays;
    }

    public void setTradingDays(double tradingDays) {
        this.tradingDays = tradingDays;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
        this.d_1 = d_1();
        this.d_2 = d_2();
    }

    public double getS() {
        return S;
    }

    public void setS(double s) {
        S = s;
        this.d_1 = d_1();
        this.d_2 = d_2();
    }

    public double getK() {
        return K;
    }

    public void setK(double k) {
        K = k;
        this.d_1 = d_1();
        this.d_2 = d_2();
    }

    public double getSigma() {
        return sigma;
    }

    public void setSigma(double sigma) {
        this.sigma = sigma;
        this.d_1 = d_1();
        this.d_2 = d_2();
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
        this.d_1 = d_1();
        this.d_2 = d_2();
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
        this.d_1 = d_1();
        this.d_2 = d_2();
    }

    public double d_1() {
        return ( Math.log( S / K ) + ( r - y + 0.5 * sigma * sigma ) * t ) / ( sigma * Math.sqrt(t));
    }

    public double d_2() {
        return d_1 - sigma * Math.sqrt(t);
    }

    public double getCallPrice() {
        return S * n.cumulativeProbability(d_1) * Math.exp(-y * t) - K * n.cumulativeProbability(d_2) * Math.exp(-r * t);
    }

    public double getPutPrice() {
        return -S * n.cumulativeProbability(-d_1) * Math.exp(-y * t) + K * n.cumulativeProbability(-d_2) * Math.exp(-r * t);
    }

    public double getCallDelta() {
        return Math.exp(-y * t) * n.cumulativeProbability(d_1);
    }

    public double getPutDelta() {
        return Math.exp(-y * t) * n.cumulativeProbability(-d_1);
    }

    public double getGamma() {
        return (Math.exp(-(y * t )) * n.density(d_1)) / (Math.sqrt(t) * S * sigma );
    }

    public double getCallRho() {
        return K * t * Math.exp(-r * t) * n.cumulativeProbability(d_2);
    }

    public double getPutRho() {
        return -K * t * Math.exp(-r * t) * n.cumulativeProbability(-d_2);
    }

    public double getVega() {
        return (S * Math.exp(-y * t) * Math.sqrt(t) / Math.sqrt(2 * Math.PI)) * Math.exp(-d_1 * d_1 / 2);
    }

    public double getCallTheta() {
        double A = (-Math.exp(-y * t) * n.density(d_1) * S * sigma ) / (2 * Math.sqrt(t));
        return (A - r * K * Math.exp(-r * t) * n.cumulativeProbability(d_2) + y * S * Math.exp(-y * t) * n.cumulativeProbability(d_1)) / tradingDays;
    }

    public double getPutTheta() {
        double A = (-Math.exp(-y * t) * n.density(-d_1) * S * sigma ) / (2 * Math.sqrt(t));
        return (A + r * K * Math.exp(-r * t)* n.cumulativeProbability(-d_2) - y * S * Math.exp(-y * t) * n.cumulativeProbability(-d_1)) / tradingDays;
    }

}
