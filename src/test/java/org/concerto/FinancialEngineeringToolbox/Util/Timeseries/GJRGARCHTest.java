package org.concerto.FinancialEngineeringToolbox.Util.Timeseries;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;
import org.concerto.FinancialEngineeringToolbox.Util.Statistics.Profile;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GJRGARCHTest {
    private static Logger logger = Logger.getLogger(GJRGARCHTest.class.getName());


    @BeforeAll
    @Test
    public static void testReadFile(){
        File file = new File("src/test/resources/FTSERETURN.json");
        assertTrue(file.exists());
    }

    @Test
    public void testGJRGARCH() throws FileNotFoundException, DimensionMismatchException {

        InputStream file = new FileInputStream("src/test/resources/FTSERETURN.json");

        JSONTokener jt = new JSONTokener(file);
        JSONArray array = new JSONArray(jt);
        double[] data = new double[array.length()];
        for(int i = 0 ; i < array.length() ; i++) {
            data[i] = (double)array.get(i);
        }
        Profile p = new Profile(data);

        double[] low = new double[]{-10 * p.getMean(), Double.MIN_VALUE, 0.0, 0.0, 0.0};
        double[] up = new double[]{10 * p.getMean(), 2 * Math.pow(p.getStdDev(), 2), 1.0, 1.0, 1.0};
        double[] initG = new double[]{p.getMean(), 0.1 * Math.pow(p.getStdDev(), 2), 0.44, 0.44, 0.44};

        GJRGARCH g = new GJRGARCH(data);
        g.fit(up, low, initG);
        double predict = g.predict(Math.pow(p.getStdDev(), 2), data[data.length - 1] - g.getMu());

        assertEquals(0.031253, g.getMu(), ConstantForTest.EPSLION);
        assertEquals(0.017630, g.getOmega(), ConstantForTest.EPSLION);
        assertEquals(0.030200, g.getAlpha(), ConstantForTest.EPSLION);
        assertEquals(0.093758, g.getGamma(), ConstantForTest.EPSLION);
        assertEquals(0.905874, g.getBeta(), ConstantForTest.EPSLION);
        assertEquals(1.177300, predict, ConstantForTest.EPSLION);


/*
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/test/resources/sigma2.json"));
            writer.write(aa.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }



}