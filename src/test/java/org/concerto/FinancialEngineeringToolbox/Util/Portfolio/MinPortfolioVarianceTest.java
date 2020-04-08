package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.concerto.FinancialEngineeringToolbox.Util.Portfolio.MinPortfolioVariance;
import org.concerto.FinancialEngineeringToolbox.Util.Portfolio.Result;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MinPortfolioVarianceTest {

    @Test
    void geOptimizeResult() throws ParameterIsNullException, ParameterRangeErrorException, UndefinedParameterValueException {
        Map<String, double[]> data = new HashMap<>();
        //Amazon, Apple, Walmart, HP, NIKE
        double[] aaa = {1926.520020, 1775.069946, 1893.630005, 1866.780029, 1776.290039, 1735.910034, 1776.660034, 1800.800049, 1847.839966, 2008.719971, 1883.750000, 1953.949951, 1918.439941};
        double[] bbb = {198.077362, 172.808105, 196.115219, 211.097366, 206.836563, 222.770889, 247.428162, 265.819183, 292.954712, 308.777191, 272.712769, 298.809998, 289.857910};
        double[] ccc = {101.340942, 99.961349, 109.457817, 109.348846, 113.192604, 118.150047, 118.568169, 118.558212, 118.309326, 114.489998, 107.680000, 115.879997, 113.389999};
        double[] ddd = {54.015625, 45.145321, 47.298336, 46.420021, 35.123363, 38.055256, 35.614479, 37.542408, 44.693581, 39.892681, 36.292011, 37.619999, 35.134998};
        double[] eee = {86.892097, 76.316238, 81.796585, 85.348206, 83.830330, 93.416389, 89.069824, 92.988693, 101.02969, 96.033562, 89.132698, 92.680000, 91.540001};
        data.put("aaa", aaa);
        data.put("bbb", bbb);
        data.put("ccc", ccc);
        data.put("ddd", ddd);
        data.put("eee", eee);
        MinPortfolioVariance mpv = new MinPortfolioVariance();
        Result ret = mpv.getMarkowitzOptimizeResult(data, Constant.ReturnType.common);

        assertEquals(-0.46953, ret.getSharpeRatio(), Constant.EPSILON);
        assertEquals(-0.01301, ret.getWeightedReturns(), Constant.EPSILON);
        assertEquals(0.00076, ret.getPortfolioVariance(), Constant.EPSILON);

        assertEquals(0.81916, ret.getWeight("aaa"), Constant.EPSILON);
        assertEquals(-0.59488, ret.getWeight("bbb"), Constant.EPSILON);
        assertEquals(0.46835, ret.getWeight("ccc"), Constant.EPSILON);
        assertEquals(-0.02195, ret.getWeight("ddd"), Constant.EPSILON);
        assertEquals(0.32932, ret.getWeight("eee"), Constant.EPSILON);
    }
}