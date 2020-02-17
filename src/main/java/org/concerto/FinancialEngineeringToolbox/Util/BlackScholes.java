package org.concerto.FinancialEngineeringToolbox.Util;
import org.apache.commons.math3.distribution.NormalDistribution;

import static java.lang.Math.*;

public class BlackScholes {
    private double S;
    private double K;
    private double sigma;
    private double r; // continuously compounded risk-free interest rate
    private double t;
    private double y; // continuously compounded dividend yield
    private NormalDistribution n;
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
        this.tradingDays = 252;
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
        return ( log( S / K ) + ( r - y + 0.5 * sigma * sigma ) * t ) / ( sigma * sqrt(t));
    }

    public double d_2() {
        return d_1 - sigma * sqrt(t);
    }

    public double getCallPrice() {
        return S * n.cumulativeProbability(d_1) * exp(-y * t) - K * n.cumulativeProbability(d_2) * exp(-r * t);
    }

    public double getPutPrice() {
        return -S * n.cumulativeProbability(-d_1) * exp(-y * t) + K * n.cumulativeProbability(-d_2) * exp(-r * t);
    }

    public double getCallDelta() {
        return exp(-y * t) * n.cumulativeProbability(d_1);
    }

    public double getPutDelta() {
        return exp(-y * t) * n.cumulativeProbability(-d_1);
    }

    public double getGamma() {
        return (exp(-(y * t )) / (sqrt(t) * S * sigma )) * n.density(d_1);
    }

    public double getCallRho() {
        return K * t * exp(-r * t) * n.cumulativeProbability(d_2);
    }

    public double getPutRho() {
        return -K * t * exp(-r * t) * n.cumulativeProbability(-d_2);
    }

    public double getVega() {
        return (S * exp(-y * t) * sqrt(t) / sqrt(2 * PI)) * exp(-d_1 * d_1 / 2);
    }

    private double getThetaCommonPart() {
        return -(S * sigma * exp(-y * t) * exp(-d_1 * d_1 /2) / sqrt(8 * PI * t));
    }

    public double getCallTheta() {
        double A = getThetaCommonPart();
        return (-A - r * K * exp(-r * t) * n.cumulativeProbability(d_2) + y * S * exp(-y * t) * n.cumulativeProbability(d_1)) / tradingDays;
    }

    public double getPutTheta() {
        double A = getThetaCommonPart();
        return (-A + r * K * exp(-r * t) * n.cumulativeProbability(-d_2) - y * S * exp(-y * t) * n.cumulativeProbability(-d_1)) / tradingDays;
    }

}
