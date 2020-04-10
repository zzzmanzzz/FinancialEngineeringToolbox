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

public class LoadData {
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
}
