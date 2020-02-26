package org.concerto.FinancialEngineeringToolbox.Util.Simulation;

public class Result {
    double[][] indexLevel;
    double[][] volatility;

    public double[][] getIndexLevel() {
        return indexLevel;
    }

    public void setIndexLevel(double[][] indexLevel) {
        this.indexLevel = indexLevel;
    }

    public double[][] getVolatility() {
        return volatility;
    }

    public void setVolatility(double[][] volatility) {
        this.volatility = volatility;
    }
}
