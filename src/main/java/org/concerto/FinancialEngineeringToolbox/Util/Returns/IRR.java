package org.concerto.FinancialEngineeringToolbox.Util.Returns;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;

public class IRR {

    public static double getIRR(double[] inflow, double[] outflow) throws DimensionMismatchException {
        if(inflow.length != outflow.length) {
            String msg = String.format("inflow[%d] and outflow[%d] size mismatch", inflow.length, outflow.length);
            throw new DimensionMismatchException(msg,null);
        }

        double[] netflow = new double[inflow.length];
        for(int i = 0 ; i < inflow.length; i++ ) {
            netflow[i] = inflow[i] - outflow[i];
        }

        double prevIRR = 0.5;
        for(int i = 0; i < Constant.MAXTRY ; i++ ) {
            double temp = prevIRR - getFairPrice(netflow, prevIRR) / derivative(netflow, prevIRR);
            if(Math.abs(getFairPrice(netflow, temp) ) < Constant.EPSILON) {
                prevIRR = temp;
                break;
            }
            prevIRR = temp;
        }
        return prevIRR;
    }

    private static double getFairPrice(double[] netflow, double requiredRate) {
        double ret = 0;
        int N = netflow.length - 1;
        for(int i = 0 ; i <= N; i ++ ) {
            ret += netflow[i] * Math.pow((1 + requiredRate), -i);
        }
        return ret;
    }

    private static double derivative(double[] netflow, double requiredYield) {
        double ret = 0;
        int N = netflow.length - 1;
        for(int i = 0 ; i <= N; i++ ) {
            ret += -i * netflow[i] * Math.pow((1 + requiredYield), -(i + 1));
        }
        return ret;
    }
}
