package org.concerto.FinancialEngineeringToolbox.Util.Optimization;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BrentSolver;
import org.concerto.FinancialEngineeringToolbox.Constant;


public class UnivariateRootFinder {

    public static Solution solve(UnivariateFunction fun, double precision,double lowerBond, double upperBond) {
        Solution ret = new Solution();
        BrentSolver brentSolver = new BrentSolver(precision);
        double result = brentSolver.solve(Constant.MAXTRY, fun, lowerBond, upperBond);
        ret.setOptimizedParameter(new double[]{result});
        return ret;
    }

}
