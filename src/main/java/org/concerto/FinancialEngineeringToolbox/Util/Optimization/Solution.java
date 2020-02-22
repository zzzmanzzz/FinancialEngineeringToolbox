package org.concerto.FinancialEngineeringToolbox.Util.Optimization;

public class Solution {
    double value;
    double[] optimizedParameter;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double[] getOptimizedParameter() {
        return optimizedParameter;
    }

    public void setOptimizedParameter(double[] optimizedParameter) {
        this.optimizedParameter = optimizedParameter;
    }
}
