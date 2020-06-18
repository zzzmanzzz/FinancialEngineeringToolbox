package org.concerto.FinancialEngineeringToolbox.Util.Returns;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;

import static org.concerto.FinancialEngineeringToolbox.Constant.OptionType.call;
import static org.concerto.FinancialEngineeringToolbox.Constant.OptionType.put;

public class ExecutionReward {
    static public double execute(double St, double K, Constant.OptionType optionType) throws UndefinedParameterValueException {
        double executeReword;
        if(optionType.equals(put)){
            executeReword = Math.max(K - St, 0);
        } else if(optionType.equals(call)) {
            executeReword = Math.max(St - K, 0);
        } else {
            String msg = "unknown optionType value " + optionType;
            throw new UndefinedParameterValueException(msg, null);
        }
        return executeReword;
    }
}
