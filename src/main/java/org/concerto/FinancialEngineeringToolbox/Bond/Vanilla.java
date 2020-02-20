package org.concerto.FinancialEngineeringToolbox.Bond;

import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;
import org.concerto.FinancialEngineeringToolbox.Exception.IndexOutOfRangeException;
import org.concerto.FinancialEngineeringToolbox.Util.IRR;
import org.concerto.FinancialEngineeringToolbox.Util.NPV;

import java.util.HashMap;
import java.util.Map;

public class Vanilla extends AbstractBond {

    public Vanilla(double parValue, double couponRate, int N, double marketPrice) {
        this.parValue = parValue;
        this.couponRate = couponRate;
        this.N = N;
        this.marketPrice = marketPrice;
    }

    @Override
    public double getYTM() {
        Map<String, double[]> cashFlow = getCashFlow();
        try {
            return IRR.getIRR(cashFlow.get("inflow"), cashFlow.get("outflow"));
        } catch (DimensionMismatchException e) {
            //this should be internal error
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public double getFairPrice(double requiredYield) {
        Map<String, double[]> cashflow = getCashFlow();
        try {
            return NPV.getPresentValue(cashflow.get("inflow"), 0.0, requiredYield, 0, N);
        } catch (DimensionMismatchException e) {
            //this should be internal error
            throw new RuntimeException(e.getMessage());
        } catch (IndexOutOfRangeException e) {
            //this should be internal error
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public double getMacaulayDuration(double requiredYield) {
        double W = 0;
        double P = getFairPrice(requiredYield);
        for(int i = 0 ; i <= N; i++ ) {
            if (i == 0) {
                W += N * parValue * Math.pow((1 + requiredYield), -N);
            } else {
                W += i * parValue * couponRate * Math.pow((1 + requiredYield), -i);
            }
        }
        return W / P;
    }

    @Override
    public double getModifiedDuration(double requiredYield) {
        return getMacaulayDuration(requiredYield) / (1 + requiredYield);
    }

    public Map<String, double[]> getCashFlow() {

        double[] inflow = new double[N+1];
        double[] outflow = new double[N+1];

        for(int i = 0 ; i < inflow.length ; i++) {
            if(i == 0) {
                inflow[i] = 0;
                outflow[i] = marketPrice;
                continue;
            }

            if(i > 0 && i < N) {
                inflow[i] = couponRate * parValue;
                outflow[i] = 0;
            }

            if(i == N) {
                inflow[i] = (1 + couponRate )* parValue;
                outflow[i] = 0;
            }
        }
        Map<String, double[]> ret = new HashMap<>();
        ret.put("inflow", inflow);
        ret.put("outflow", outflow);
        return ret;
    }
}
