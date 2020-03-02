package org.concerto.FinancialEngineeringToolbox.Util.Returns;

import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;

import java.util.stream.IntStream;

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

    public static double[] getCommonReturn(double[] data) throws ParameterIsNullException {
        if(data == null) {
            String msg = "Input array is null";
            throw new ParameterIsNullException(msg, null);
        }

        return IntStream.range(1, data.length).mapToDouble(
              i ->  data[i] / data[i-1] - 1
        ).toArray();
    }

    public static double[] getLogReturn(double[] data) throws ParameterIsNullException {
        if(data == null) {
            String msg = "Input array is null";
            throw new ParameterIsNullException(msg, null);
        }

        return IntStream.range(1, data.length).mapToDouble(
                i ->  Math.log(data[i] / data[i - 1])
        ).toArray();
    }
}
