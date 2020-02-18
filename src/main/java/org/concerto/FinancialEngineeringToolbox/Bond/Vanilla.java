package org.concerto.FinancialEngineeringToolbox.Bond;

public class Vanilla extends AbstractBond {
    final static double EPSILON = 1e-10;
    final static int MAXTRY = 2000;

    public Vanilla(double parValue, double couponRate, int N, double marketPrice) {
        this.parValue = parValue;
        this.couponRate = couponRate;
        this.N = N;
        this.marketPrice = marketPrice;
    }

    @Override
    public double getYTM() {
        double prevYTM = 0.5;
       for(int i = 0 ; i < MAXTRY ; i++ ) {
            double temp = prevYTM - ((getInitialPrice(prevYTM) - marketPrice ) / derivative(prevYTM));
            if(Math.abs(marketPrice - getInitialPrice(temp) ) < EPSILON) {
                prevYTM = temp;
                break;
            }
            prevYTM = temp;
        }
        return prevYTM;
    }

    @Override
    public double getInitialPrice(double requiredYield) {
        double ret = 0;
        for(int i = 0 ; i <= N; i ++ ) {
            if (i == 0) {
                ret += parValue * Math.pow((1 + requiredYield), -N);
            } else {
                ret += parValue * couponRate * Math.pow((1 + requiredYield), -i);
            }
        }
        return ret;
    }

    @Override
    public double getMacaulayDuration(double requiredYield) {
        double W = 0;
        double P = getInitialPrice(requiredYield);
        for(int i = 0 ; i <= N; i++ ) {
            if (i == 0) {
                W += N * parValue * Math.pow((1 + requiredYield), -N);
            } else {
                W += i * parValue * couponRate * Math.pow((1 + requiredYield), -i);
            }
        }
        return W / P;
    }

    @Override
    public double getModifiedDuration(double requiredYield) {
        return getMacaulayDuration(requiredYield) / (1 + requiredYield);
    }

    private double derivative(double requiredYield) {
        double ret = 0;
        for(int i = 0 ; i <= N; i++ ) {
            if (i == 0) {
                ret += -N * parValue * Math.pow((1 + requiredYield), -(N + 1));
            } else {
                ret += -i * parValue * couponRate * Math.pow((1 + requiredYield), -(i + 1));
            }
        }
        return ret;
    }
}
