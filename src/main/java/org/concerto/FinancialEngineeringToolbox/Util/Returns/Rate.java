package org.concerto.FinancialEngineeringToolbox.Util.Returns;

import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;

public class  Rate {

    public static double getDiscount(double faceValue, double price) {
         return ((faceValue - price) / faceValue);
    }

    public static double getBankDiscountYield(double faceValue, double price, int days) throws ParameterRangeErrorException {
        if(days < 1) {
            String msg = String.format("Days(%d) should > 0", days);
            throw new ParameterRangeErrorException(msg, null);
        }
        return getDiscount(faceValue, price) * 360 / days;
    }

    public static double getMoneyMarketYield(double faceValue, double price, int days) throws ParameterRangeErrorException {
        if(days < 1) {
            String msg = String.format("Days(%d) should > 0", days);
            throw new ParameterRangeErrorException(msg, null);
        }
        return ((faceValue - price) / price * 360 / days);
    }

    public static double getBondEquivalentYield(double faceValue, double price, int days) throws ParameterRangeErrorException {
        if(days < 1) {
            String msg = String.format("Days(%d) should > 0", days);
            throw new ParameterRangeErrorException(msg, null);
        }
        return ((faceValue - price) / price * 365 / days);
    }

    public static double getEffectiveAnnualRate(double nominalAnnualRate, int frequency) throws ParameterRangeErrorException {
        if(frequency < 1) {
            String msg = String.format("frequency(%d) should > 0", frequency);
            throw new ParameterRangeErrorException(msg, null);
        }
        return Math.pow((1 + nominalAnnualRate / frequency), frequency) - 1;
    }

    public static double[] getCommonReturn(double[] data) {
        double[] ret = new double[data.length - 1];

        for(int i = 1 ; i < data.length ; i++) {
            if(Double.compare(0.0, data[i - 1])>= 0) {
                ret[i - 1] = 0;
                continue;
            }
            ret[i - 1] = data[i] / data[i - 1] - 1;
        }
        return ret;
    }

    public static double[] getLogReturn(double[] data) {
        double[] ret = new double[data.length - 1];

        for(int i = 1 ; i < data.length ; i++) {
            if(Double.compare(0.0, data[i - 1])>= 0) {
                ret[i - 1] = 0;
                continue;
            }
            ret[i - 1] = Math.log(data[i] / data[i - 1]);
        }
        return ret;
    }
}
