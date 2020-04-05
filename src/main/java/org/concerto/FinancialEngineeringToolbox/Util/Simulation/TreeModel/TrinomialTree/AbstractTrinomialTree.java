package org.concerto.FinancialEngineeringToolbox.Util.Simulation.TreeModel.TrinomialTree;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.concerto.FinancialEngineeringToolbox.Util.Returns.ExecutionReward;
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
        int size = 2 * N + 1;
        double[] St = new double[size];
        double[] prevC = new double[size];
        double[] currentC;

        St[N] = S0;
        for (int i = 0 ; i < N; i++) {
            St[i] = S0 * Math.pow(D, N - i);
            St[size - 1 - i] = S0 * Math.pow(U, N - i);
        }

        for ( int i = 0 ; i < size ; i++ ) {
            prevC[i]= ExecutionReward.execute(St[i], K, optionType);
        }

        for (int i = N - 1 ; i > 0 ; i--) {
            size = 2 * i + 1;
            St = new double[size];
            currentC = new double[size];

            St[0] = S0 * Math.pow(D, i);
            St[i] = S0;
            for (int z = 0 ; z < i; z++) {
                St[z] = S0 * Math.pow(D, i - z);
                St[size - 1 - z] = S0 * Math.pow(U, i - z);
            }

            for (int j = 0; j < size ; j++) {
                double tempPrice = discount * (probabilityUp * prevC[j + 2] + probabilityMiddle * prevC[j + 1] + probabilityDown * prevC[j]);
                double executeReword;

                if(!strikeSchedule[i - 1]) {
                    currentC[j] = tempPrice;
                } else {
                    executeReword = ExecutionReward.execute(St[j], K, optionType);
                    currentC[j] = Math.max(executeReword, tempPrice);
                }
            }
            prevC = currentC;
        }

        return (prevC[0] * probabilityDown + prevC[1] * probabilityMiddle + prevC[2] * probabilityUp) * discount;
    }
}
