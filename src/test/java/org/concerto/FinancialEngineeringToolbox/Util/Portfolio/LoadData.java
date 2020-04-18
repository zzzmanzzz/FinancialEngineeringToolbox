package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

class LoadData {
    protected static Logger logger = Logger.getLogger(LoadData.class.getName());
    protected static Map<String, double[]> data;
    protected static List<String> symbles;

    @BeforeAll
    public static void init() throws IOException {
        Reader reader = Files.newBufferedReader(Paths.get("src/test/resources/stock_prices.csv"));
        CSVParser csvParser = new CSVParser(reader,
                CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim());
        symbles = csvParser.getHeaderNames();

        Map<String, List<Double>> csv = new HashMap<>();
        data = new HashMap<>();

        for (CSVRecord csvRecord : csvParser) {
            for (int i = 1; i < symbles.size(); i++) {
                String s = symbles.get(i);
                if (!csv.containsKey(s)) {
                    csv.put(s, new LinkedList<Double>());
                }
                String n = csvRecord.get(s);
                double num = 0;
                if( n.isEmpty() || n.equals("") ) {
                    num = Double.NaN;
                } else {
                    num =Double.valueOf(n);
                }
                csv.get(s).add(num);
            }
        }

        reader.close();

        csv.forEach((K, V) ->
               data.put(K, V.stream().mapToDouble(Double::doubleValue).toArray()));
    }

    Map<String, double[]> generateP() {
        //BABA, GOOG, AAPL, RRC, BAC, GM, JPM, SHLD, PFE, T, UAA, MA, SBUX, XOM, AMD, BBY, FB, AMZN, GE, WMT";
        //GM drop 20%
        //GOOG outperforms BABA by 10%
        //AMZN and AAPL will outperform T and UAA 5%
        String[] symbols = {"BABA", "GOOG", "AAPL", "RRC", "BAC", "GM", "JPM", "SHLD", "PFE", "T", "UAA", "MA", "SBUX", "XOM", "AMD", "BBY", "FB", "AMZN", "GE", "WMT"};
        double[][] P = {
                {0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {-1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0.5, 0, 0, 0, 0, 0, 0, -0.5, -0.5, 0, 0, 0, 0, 0, 0, 0.5, 0, 0},
        };
        Map<String, double[]> p = new HashMap<>();
        for(int i = 0 ; i < symbols.length; i++) {
            double[] tmp = new double[P.length];
            for (int j = 0 ; j < P.length; j++ ) {
                tmp[j] = P[j][i];
            }
            p.put(symbols[i], tmp);
        }
        return p;
    }

    Map<String, Double> generateMarketCap() {
        String[] symbols = {"BABA", "GOOG", "AAPL", "RRC", "BAC", "GM", "JPM", "SHLD", "PFE", "T", "UAA", "MA", "SBUX", "XOM", "AMD", "BBY", "FB", "AMZN", "GE", "WMT"};
        double[] marketCap = {533e9, 927e9, 1.19e12, 1e9, 301e9, 51e9, 422e9, 0, 212e9, 61e9, 78e9, 288e9, 102e9, 295e9, 43e9, 22e9, 574e9, 867e9, 96e9, 339e9};
        Map<String, Double> ret = new HashMap<>();
        for(int i = 0 ; i < symbols.length; i++) {
            ret.put(symbols[i], marketCap[i]);
        }
        return ret;
    }
}
