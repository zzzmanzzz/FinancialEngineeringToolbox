package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.concerto.FinancialEngineeringToolbox.Exception.DateFormatException;
import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;

import java.util.*;

class DataProcessor {

    static String[] getDataKey(Map<String, double[]> data) {
        Object[] tmpK = data.keySet().toArray();
        String[] keys = new String[tmpK.length];

        for(int i = 0 ; i < keys.length;i++ ) {
            keys[i] = (String) tmpK[i];
        }
        return keys;
    }


    static double[][] dropna(double[][] in) {
        Set<Long> skipLine = new HashSet<>();

        for (double[] doubles : in) {
            for (int j = 0; j < doubles.length; j++) {
                if (Double.isNaN(doubles[j])) {
                    skipLine.add(Long.valueOf(j));
                }
            }
        }

        double[][] ret = new double[in.length][];

        for (int i = 0 ; i < in.length; i++) {
            List<Double> tmp = new LinkedList<>();
            for(int j = 0 ; j < in[i].length ; j++ ) {
                if(!skipLine.contains(Long.valueOf(j))) {
                    tmp.add(in[i][j]);
                }
            }
            ret[i] = tmp.stream().mapToDouble(Double::doubleValue).toArray();
        }
        return ret;
    }

    static protected double[] parseMarketCap(Map<String, Double> marketCap, Map<String, double[]> data) throws DimensionMismatchException, ParameterIsNullException {
        Set<String> Mkey = marketCap.keySet();
        Set<String> Dkey = data.keySet();
        if(Mkey.size() != Dkey.size() || !Mkey.containsAll(Dkey)) {
            String msg = String.format("Key mismatch MarketCap: %s, data: %s", Arrays.toString(Mkey.toArray()), Arrays.toString(Dkey.toArray()));
            throw new DimensionMismatchException(msg, null);
        }
        String[] symbols = Mkey.toArray(new String[0]);
        double[] ret = new double[symbols.length];

        for(int i = 0; i < symbols.length; i++) {
            String s = symbols[i];
            if(marketCap.get(s) == null) {
                String msg = String.format("marketCap has null data, key: %s", s);
                throw new ParameterIsNullException(msg, null);
            }
            ret[i] = marketCap.get(s);
        }

        return ret;

    }

    static protected void validateOmega(double[] Q, double[] Omega) throws DateFormatException, ParameterRangeErrorException {
        //check size
        if(Omega.length != Q.length) {
            String msg = String.format("Omega size(%d) not equal to Q(%d)",Omega.length, Q.length);
            throw new DateFormatException(msg, null);
        }

        for(double o : Omega) {
            if(Double.compare(0.0, o) > 0) {
                String msg = "Elements in Omega should >= 0";
                throw new ParameterRangeErrorException(msg, null);
            }
        }

    }

    static protected void validateData(Map<String, double[]> data, String[] dataKeys) throws ParameterIsNullException {
        for(Object k : dataKeys) {
            if(data.get(k) == null) {
                String msg = String.format("key(%s) has null value", k);
                throw new ParameterIsNullException(msg, null);
            }
        }
    }


    static protected double[][] parseP(Map<String, double[]> P, double[] Q, Map<String, double[]> data) throws DimensionMismatchException, ParameterIsNullException {
        Set<String> Pkey = P.keySet();
        Set<String> Dkey = data.keySet();
        if(Pkey.size() != Dkey.size() || !Pkey.containsAll(Dkey)) {
            String msg = String.format("Key mismatch P: %s, data: %s", Arrays.toString(Pkey.toArray()), Arrays.toString(Dkey.toArray()));
            throw new DimensionMismatchException(msg, null);
        }

        String[] symbols = Pkey.toArray(new String[0]);
        double[][] ret = new double[symbols.length][];

        int size = Q.length;
        for(int i = 0; i < symbols.length; i++) {
            String s = symbols[i];
            if(P.get(s) == null) {
                String msg = String.format("P has null data, key: %s", s);
                throw new ParameterIsNullException(msg, null);
            }

            if(P.get(s).length != size) {
                String msg = String.format("P length mismatch, key: %s, actual length: %d, expect length: %d", s, P.get(s).length, size);
                throw new DimensionMismatchException(msg, null);
            }
            ret[i] = P.get(s);
        }
        RealMatrix tmp = new Array2DRowRealMatrix(ret);
        return tmp.transpose().getData();
    }
}
