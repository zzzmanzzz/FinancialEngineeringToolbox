package org.concerto.FinancialEngineeringToolbox.Bond;

public abstract class AbstractBond {
    double couponRate;
    double parValue;
    int period;
    double YTM;

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

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public double getCurrentYield(double price) {
        return parValue * couponRate / price;
    }

    abstract public double getYTM(double marketRate);
    abstract public double getPresentPrice(double marketRate);
    abstract public double getMacaulayDuration(double marketRate);
    abstract public double getModifiedDuration(double marketRate, double marketPrice);


}
