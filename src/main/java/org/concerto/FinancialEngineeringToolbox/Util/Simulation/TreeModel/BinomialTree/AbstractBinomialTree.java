package org.concerto.FinancialEngineeringToolbox.Util.Simulation.TreeModel.BinomialTree;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.concerto.FinancialEngineeringToolbox.Util.Returns.ExecutionReward;
import org.concerto.FinancialEngineeringToolbox.Util.Simulation.TreeModel.Tree;


public abstract class AbstractBinomialTree extends Tree {

    public AbstractBinomialTree(double S0, double K, double sigma, double riskFreeRate, int N, double deltaT) {
        super(S0, K, sigma, riskFreeRate, N, deltaT);
    }

    abstract protected void init();

    @Override
    public double getFairPrice(Constant.OptionType optionType, boolean[] strikeSchedule) throws UndefinedParameterValueException {
        double[] St = new double[N + 1];
        double[] prevC = new double[N + 1];
        double[] currentC;

        St[0] = S0 * Math.pow(D, N);

        for (int i = 1 ; i < N + 1; i++) {
            St[i] = St[i - 1] * U / D;
        }

        for ( int i = 0 ; i < N + 1 ; i++ ) {
            prevC[i]= ExecutionReward.execute(St[i], K, optionType);
        }

        for (int i = N - 1 ; i > 0 ; i--) {
            St = new double[i + 1];
            currentC = new double[i + 1];

            St[0] = S0 * Math.pow(D, i);

            for (int j = 1 ; j < i + 1 ; j++) {
                St[j] = St[j - 1] * U / D;
            }

            for (int j = 0; j <= i ; j++) {
                double tempPrice = discount * (probabilityUp * prevC[j + 1] + probabilityDown * prevC[j]);
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

        double ret = (prevC[0] * probabilityDown + prevC[1] * probabilityUp) * discount;
        return ret;
    }

}
