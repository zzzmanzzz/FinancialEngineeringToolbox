package org.concerto.FinancialEngineeringToolbox.Util;

import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;
import org.concerto.FinancialEngineeringToolbox.Exception.IndexOutOfRangeException;

public class NPV {
    /**
    All dimension of arrays should be the same
    @Param  double[] inflow     Cash inflow
    @Param  double[] outflow    Cash outflow
    @Param  double[] rate       Required rate
    @Param  int from            Net cash flow is discounted to this index
    @Param  int to              Last net cash flow index to discount
     */
    static public double getPresentValue(double[] inflow, double[] outflow, double[] rate, int from, int to) throws DimensionMismatchException, IndexOutOfRangeException {
        if(inflow.length != outflow.length  ||  rate.length != inflow.length) {
            String msg = String.format("Inflow[%d], outflow[%d], rate[%d]", inflow.length, outflow.length, rate.length);
            throw new DimensionMismatchException(msg, null);
        }

        if(from < 0 || from > inflow.length - 1 || to < 0 || to > inflow.length - 1) {
            String msg = String.format("from: %d, to: %d, array length: %d", from, to, inflow.length);
            throw new IndexOutOfRangeException(msg, null);
        }

        double ret = 0;
        for(int i = from ; i <= to ; i ++ ) {
            ret += (inflow[i] - outflow[i]) * Math.pow((1 + rate[i]), -i);
        }
        return ret;
    }

    /**
     All dimension of arrays should be the same
     @Param  double[] inflow     Cash inflow
     @Param  double   outflow    Only initial index has outflow
     @Param  double[] rate       Required rate
     @Param  int from            Net cash flow is discounted to this index
     @Param  int to              Last net cash flow index to discount
     */
    static public double getPresentValue(double[] inflow, double initialOutflow, double[] rate, int from, int to) throws DimensionMismatchException, IndexOutOfRangeException {
        double[] _outflow = new double[inflow.length];
        _outflow[0] = initialOutflow;

        return getPresentValue(inflow, _outflow, rate, from, to);
    }

    /**
     All dimension of arrays should be the same
     @Param  double[] inflow     Cash inflow
     @Param  double   outflow    Only initial index has outflow
     @Param  double   rate       Fix required rate
     @Param  int from            Net cash flow is discounted to this index
     @Param  int to              Last net cash flow index to discount
     */
    static public double getPresentValue(double[] inflow, double initialOutflow, double rate, int from, int to) throws DimensionMismatchException, IndexOutOfRangeException {
        double[] _outflow = new double[inflow.length];
        _outflow[0] = initialOutflow;

        double[] _rate = new double[inflow.length];
        for(int i = 0 ; i <= inflow.length - 1 ; i++) {
            _rate[i] = rate;
        }
        return getPresentValue(inflow, _outflow, _rate, from, to);
    }

    /**
     All dimension of arrays should be the same
     @Param  double[] inflow     Cash inflow
     @Param  double[] outflow    Cash outflow
     @Param  double   rate       Fix required rate
     @Param  int from            Net cash flow is discounted to this index
     @Param  int to              Last net cash flow index to discount
     */
    static public double getPresentValue(double[] inflow, double[] outflow, double rate, int from, int to) throws DimensionMismatchException, IndexOutOfRangeException {
        double[] _rate = new double[inflow.length];
        for(int i = 0 ; i <= inflow.length - 1 ; i++) {
            _rate[i] = rate;
        }
        return getPresentValue(inflow, outflow, _rate, from, to);
    }


}
