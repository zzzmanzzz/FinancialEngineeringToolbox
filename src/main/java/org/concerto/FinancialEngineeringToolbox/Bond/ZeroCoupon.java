package org.concerto.FinancialEngineeringToolbox.Bond;

public class ZeroCoupon extends AbstractBond {
    @Override
    public double getYTM(double marketRate) {
        return 0;
    }

    @Override
    public double getPresentPrice(double marketRate) {
        return 0;
    }

    @Override
    public double getMacaulayDuration(double marketRate) {
        return 0;
    }

    @Override
    public double getModifiedDuration(double marketRate, double marketPrice) {
        return 0;
    }
}
