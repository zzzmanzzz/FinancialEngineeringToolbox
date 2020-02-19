package org.concerto.FinancialEngineeringToolbox.Util;

import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;

public class NPV {
    /*
    All dimension should be the same
     */
    static double getPresentValue(double[] inflow, double[] outflow, double[] rate, int N) throws DimensionMismatchException {
        //TODO: modify interface N to from and to is more flexible
        if(inflow.length != N + 1 || outflow.length != N + 1 || rate.length != N + 1) {
            String msg = String.format("Inflow[%d], outflow[%d], rate[%d], N+1 = %d", inflow.length, outflow.length, rate.length, N + 1);
            throw new DimensionMismatchException(msg, null);
        }
        double ret = 0;
        for(int i = 0 ; i <= N ; i ++ ) {
            ret += (inflow[i] - outflow[i]) * Math.pow((1 + rate[i]), -i);
        }
        return ret;
    }

    /*
    Only initial state has outflow
     */
    static double getPresentValue(double[] inflow, double outflow, double[] rate, int N) throws DimensionMismatchException {
        if(inflow.length != N + 1 || rate.length != N + 1) {
            String msg = String.format("Inflow[%d], rate[%d], N+1 = %d", inflow.length, rate.length, N + 1);
            throw new DimensionMismatchException(msg, null);
        }
        double[] _outflow = new double[N+1];
        _outflow[0] = outflow;

        return getPresentValue(inflow, _outflow, rate, N);
    }

    /*
    Only initial state has outflow, fix rate
     */
    static double getPresentValue(double[] inflow, double outflow, double rate, int N) throws DimensionMismatchException {
        if(inflow.length != N + 1) {
            String msg = String.format("Inflow[%d], N+1 = %d", inflow.length, N + 1);
            throw new DimensionMismatchException(msg, null);
        }
        double[] _outflow = new double[N+1];
        _outflow[0] = outflow;

        double[] _rate = new double[N+1];
        for(int i = 0 ; i <=N ; i++) {
            _rate[i] = rate;
        }
        return getPresentValue(inflow, _outflow, _rate, N);
    }

    /*
    Fix rate
     */
    static double getPresentValue(double[] inflow, double[] outflow, double rate, int N) throws DimensionMismatchException {
        if(inflow.length != N + 1 || outflow.length != N + 1) {
            String msg = String.format("Inflow[%d], Outflow[%d], N+1 = %d", inflow.length, outflow.length, N + 1);
            throw new DimensionMismatchException(msg, null);
        }

        double[] _rate = new double[N+1];
        for(int i = 0 ; i <=N ; i++) {
            _rate[i] = rate;
        }
        return getPresentValue(inflow, outflow, _rate, N);
    }


}
