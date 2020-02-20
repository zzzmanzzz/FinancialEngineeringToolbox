package org.concerto.FinancialEngineeringToolbox.Util.Simulation;

import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NormalizedGaussianTest {
    int size = 10000;

    @Test
    void nextRandomVectorSizeIsCorrect() {
        NormalizedGaussian ng = new NormalizedGaussian(size);
        double[] random = ng.nextRandomVector();
        assertEquals(size, random.length);
    }

    @Test
    void nextRandomVectorMeanIsCorrect() {
        NormalizedGaussian ng = new NormalizedGaussian(size);
        double[] random = ng.nextRandomVector();
        double sum = 0;
        for(double i : random) {
            sum+=i;
        }
        assertEquals(0, sum/size, ConstantForTest.EPSLION);
    }

    @Test
    void nextRandomVectorStdIsCorrect() {
        NormalizedGaussian ng = new NormalizedGaussian(size);
        double[] random = ng.nextRandomVector();
        double sum = 0;
        for(double i : random) {
            sum+=i;
        }
        double mean = sum / size;
        double squareSum = 0;
        for(double i : random) {
            squareSum += Math.pow((i - mean), 2);
        }
        double stddev = Math.sqrt(squareSum / size);
        assertEquals(1.0, stddev, 1e-4);
    }
}