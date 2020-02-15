package org.concerto.FinancialEngineeringToolbox.Bond;

public class Vanilla extends AbstractBond{
    @Override
    public double getYTM() {
        return 0;
    }

    @Override
    public double getInitialPrice(double requiredYield) {
        return 0;
    }

    @Override
    public double getMacaulayDuration(double requiredYield) {
        return 0;
    }

    @Override
    public double getModifiedDuration(double requiredYield) {
        return getMacaulayDuration(requiredYield) / (1 + requiredYield);
    }
}
