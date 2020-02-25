package org.concerto.FinancialEngineeringToolbox.Util;

import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;

public class ExecutionReward {
    static public double execute(double St, double K, String optionType) throws UndefinedParameterValueException {
        double executeReword = 0;
        if(optionType.equals("put")){
            executeReword = Math.max(K - St, 0);
        } else if(optionType.equals("call")) {
            executeReword = Math.max(St - K, 0);
        } else {
            String msg = "unknown optionType value " + optionType;
            throw new UndefinedParameterValueException(msg, null);
        }
        return executeReword;
    }
}
