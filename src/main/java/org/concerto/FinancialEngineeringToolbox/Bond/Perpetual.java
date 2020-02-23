package org.concerto.FinancialEngineeringToolbox.Bond;

public class Perpetual extends AbstractBond {

    public Perpetual(double couponRate, double parValue, double marketPrice) {
        this.parValue = parValue;
        this.couponRate = couponRate;
        this.N = Integer.MAX_VALUE;
        this.marketPrice = marketPrice;
    }

    @Override
    public double getFairPrice(double marketRate) {
        return (couponRate * parValue) / (marketRate);
    }

    @Override
    public double getYTM() {
        return (couponRate * parValue) / (marketPrice);
    }

    @Override
    public double getMacaulayDuration(double requiredYield) {
        return 1 + 1 / requiredYield;
    }

    @Override
    public double getModifiedDuration(double requiredYield) {
        return getMacaulayDuration(requiredYield) / (1 + requiredYield);
    }

    @Override
    public double getConvexity(double requiredYield) {
        return getModifiedDuration(requiredYield);
    }

}
