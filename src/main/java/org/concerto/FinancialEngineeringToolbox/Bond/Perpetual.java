package org.concerto.FinancialEngineeringToolbox.Bond;

public class Perpetual extends AbstractBond {

    public Perpetual(double couponRate, double parValue) {
        this.parValue = parValue;
        this.couponRate = couponRate;
        this.period = Integer.MAX_VALUE;
    }

    @Override
    public double getPresentPrice(double marketRate) {
        return couponRate * parValue / marketRate;
    }

    @Override
    public double getYTM(double markePrice) {
        return couponRate * parValue / markePrice;
    }

    @Override
    public double getMacaulayDuration(double marketRate) {
        return 1 + 1 / marketRate;
    }

    @Override
    public double getModifiedDuration(double marketRate, double marketPrice) {
        return getMacaulayDuration(marketRate) / (1 + getYTM(marketPrice));
    }
}
