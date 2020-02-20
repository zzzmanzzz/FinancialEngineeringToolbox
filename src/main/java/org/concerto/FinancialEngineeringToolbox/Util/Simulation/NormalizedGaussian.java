package org.concerto.FinancialEngineeringToolbox.Util.Simulation;

import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.NormalizedRandomGenerator;
import org.apache.commons.math3.random.UncorrelatedRandomVectorGenerator;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class NormalizedGaussian {
    private UncorrelatedRandomVectorGenerator random;

    public NormalizedGaussian(int dimension) {
        NormalizedRandomGenerator nr = new GaussianRandomGenerator(new JDKRandomGenerator()) ;
        random = new UncorrelatedRandomVectorGenerator(dimension, nr);
    }

    public double[] nextRandomVector() {
        double[] ret = random.nextVector();
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (double i : ret) {
           stats.addValue(i);
        }

        for(int i = 0 ; i < ret.length; i++ ) {
            ret[i] = (ret[i] - stats.getMean()) / stats.getStandardDeviation();
        }
        return ret;
    }
}
