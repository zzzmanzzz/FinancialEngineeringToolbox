package org.concerto.FinancialEngineeringToolbox.Util.Timeseries;

import org.json.JSONArray;
import org.json.JSONTokener;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GJRGARCHTest {
    private static Logger logger = Logger.getLogger(GJRGARCH.class.getName());


    @Test
    public void testReadFile(){
        File file = new File("src/test/resources/FTSERETURN.json");
        assertTrue(file.exists());
    }

    @Test
    public void testGJRGARCH() throws FileNotFoundException {

        InputStream file = new FileInputStream("src/test/resources/FTSERETURN.json");

        JSONTokener jt = new JSONTokener(file);
        JSONArray array = new JSONArray(jt);
        double[] data = new double[array.length()];
        for(int i = 0 ; i < array.length() ; i++) {
            data[i] = (double)array.get(i);
        }


        GJRGARCH g = new GJRGARCH(data);
        double[] ans = g.findParameter();
        logger.info(Arrays.toString(ans));

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