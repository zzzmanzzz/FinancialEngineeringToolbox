package org.concerto.FinancialEngineeringToolbox.Bond;

public abstract class AbstractBond {
    double couponRate;
    double parValue;
    int N;
    double marketPrice;

    public double getCouponRate() {
        return couponRate;
    }

    public void setCouponRate(double couponRate) {
        this.couponRate = couponRate;
    }

    public double getParValue() {
        return parValue;
    }

    public void setParValue(double parValue) {
        this.parValue = parValue;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        this.N = n;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public double getCurrentYield(double price) {
        return parValue * couponRate / price;
    }

    abstract public double getYTM();
    abstract public double getInitialPrice(double requiredYield);
    abstract public double getMacaulayDuration(double requiredYield);
    abstract public double getModifiedDuration(double requiredYield);


}
