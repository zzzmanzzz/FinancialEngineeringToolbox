package org.concerto.FinancialEngineeringToolbox.Util.Simulation.TreeModel.TrinomialTree;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.concerto.FinancialEngineeringToolbox.Util.Simulation.TreeModel.Tree;

public abstract class AbstractTrinomialTree extends Tree {
    protected double probabilityMiddle;
    protected double M;

    public double getProbabilityMiddle() { return probabilityMiddle; }

    public double getM() { return M; }

    public AbstractTrinomialTree(double S0, double K, double sigma, double riskFreeRate, int N, double deltaT) {
        super(S0, K, sigma, riskFreeRate, N, deltaT);
    }

    @Override
    public double getFairPrice(Constant.OptionType optionType, boolean[] strikeSchedule) throws UndefinedParameterValueException {
        return 0;
    }
}
