package org.concerto.FinancialEngineeringToolbox.Util.PortfolioOptimization;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.concerto.FinancialEngineeringToolbox.Constant;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.concerto.FinancialEngineeringToolbox.Util.Returns.Rate;

import java.util.function.Function;

public abstract class AbstractPortfolioOptimization {
    static final private Mean m = new Mean();

    abstract protected Result optimize(String[] symbols, double[][] returns, double riskFreeRate) throws ParameterRangeErrorException;

    final protected double getWeightedReturn(double[] weight, double[] returns) {
        double ret = 0;
        for(int i = 0 ; i < weight.length; i++ ) {
            ret += weight[i] * returns[i];
        }
        return ret;
    }

    final protected double getPortfolioVariance(double[][] cov, double[] weight) {
        RealMatrix w = new Array2DRowRealMatrix(weight);
        RealMatrix r = new Array2DRowRealMatrix(cov);
        return w.transpose().multiply(r).multiply(w).getData()[0][0];
    }

    final protected double[] getMeanReturn(double[][] returns) {
        double[] mean = new double[returns.length];
        for(int i = 0 ; i < returns.length; i++) {
            mean[i] = m.evaluate(returns[i], 0, returns[i].length);
        }
        return mean;
    }

    final protected double[][] getCovariance(double[][] data) {
        double[][] ret = new double[data.length][data.length];
        Covariance covariance = new Covariance();

        for (int i = 0 ; i < data.length; i++ ) {
            ret[i][i] = covariance.covariance(data[i], data[i]);
        }

        for(int i = 0 ; i < data.length; i++ ) {
            for( int j = i + 1 ; j < data.length; j++) {
                ret[i][j] = covariance.covariance(data[i], data[j]);
                ret[j][i] = ret[i][j];
            }
        }
        return ret;
    }

    final protected Function<double[], double[]> getReturnFunction(Constant.ReturnType type) throws UndefinedParameterValueException {
        Function<double[], double[]> funcRef = Rate::getCommonReturn;

        switch (type) {
            case common:
                funcRef = Rate::getCommonReturn;
                break;
            case log:
                funcRef = Rate::getLogReturn;
                break;
            default:
                throw new UndefinedParameterValueException("Unexpected value: " + type, null);
        }
        return funcRef;
    }
}