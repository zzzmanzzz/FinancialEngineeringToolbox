package org.concerto.FinancialEngineeringToolbox.Util.Timeseries;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;
import org.concerto.FinancialEngineeringToolbox.Util.Statistics.Profile;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GJRGARCHTest {
    private static Logger logger = Logger.getLogger(GJRGARCH.class.getName());


    @Test
    public void testReadFile(){
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
        double[] initG = new double[]{p.getMean(), 0.1 * Math.pow(p.getStdDev(), 2), 0.03, 0.09, 0.9};

        GJRGARCH g = new GJRGARCH(data);
        double[] ans = g.fit(up, low, initG);
        //logger.info(Arrays.toString(ans));


        assertEquals(0.034439, ans[0], ConstantForTest.EPSLION); // mu
        assertEquals(0.017989, ans[1], ConstantForTest.EPSLION); // omega
        assertEquals(0.032059, ans[2], ConstantForTest.EPSLION); // alpha
        assertEquals(0.094136, ans[3], ConstantForTest.EPSLION); // gamma
        assertEquals(0.903523, ans[4], ConstantForTest.EPSLION); // beta


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