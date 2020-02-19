package org.concerto.FinancialEngineeringToolbox.Bond;

import static java.lang.Math.pow;

public class ZeroCoupon extends AbstractBond {

    public ZeroCoupon(double parValue, int N, double marketPrice) {
        this.parValue = parValue;
        this.couponRate = 0;
        this.N = N;
        this.marketPrice = marketPrice;
    }

    @Override
    public double getYTM() {
        return pow((parValue / marketPrice), (1 / N)) - 1;
    }

    @Override
    public double getFairPrice(double marketRate) {
        return parValue * pow( 1 + marketRate, -N);
    }

    @Override
    public double getMacaulayDuration(double marketRate) {
        return N;
    }

    @Override
    public double getModifiedDuration(double marketRate) {
        return N / (1 + marketRate);
    }
}
