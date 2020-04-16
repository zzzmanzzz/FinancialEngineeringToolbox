package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;


import java.util.logging.Logger;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;
import org.apache.commons.math3.linear.SingularMatrixException;

public class BlackLitterman {
    protected static Logger logger = Logger.getLogger(BlackLitterman.class.getName());

    /**
     * Get risk aversion
     * Three rates should be in the same period and correspond to the frequency of portfolio returns.
     * @param marketMeanReturn market returns
     * @param marketVariance Variance of market returns
     * @param riskFreeRate Risk free rate
     * @return risk aversion
     */
    static public double getMarketImpliedRiskAversion(double marketMeanReturn, double marketVariance, double riskFreeRate) {
        return (marketMeanReturn - riskFreeRate) / (marketVariance);
    }

    static public double[] getPriorReturns(double[][] covariance, double riskAversion, double riskFreeRate, double[] marketCap, int frequency) {
        final double sum = Arrays.stream(marketCap).sum();
        double[] marketCapWeight = Arrays.stream(marketCap).map(d -> d / sum).toArray();
        RealMatrix sigma = new Array2DRowRealMatrix(covariance);
        return sigma.multiply(new Array2DRowRealMatrix(marketCapWeight))
                .scalarMultiply(riskAversion)
                .scalarAdd(riskFreeRate).getColumn(0);
    }

    static public double[][] getOmega(double[][] covariance, double[][] Q, double tau) {
        RealMatrix sigma = new Array2DRowRealMatrix(covariance);
        RealMatrix q = new Array2DRowRealMatrix(Q);
        RealMatrix Omega = q
            .multiply(sigma)
            .multiply(q.transpose())
            .scalarMultiply(tau);
        double[][] ret = new double[Omega.getRowDimension()][Omega.getColumnDimension()];
        for(int i = 0 ; i < ret.length ; i++ ) {
            ret[i][i] = Omega.getEntry(i, i);
        }
        return ret;
    }


    /**
     *
     * Meucci, A. ”The Black-Litterman Approach: Original Model and Extensions”, Bloomberg Alpha Research & Education Paper, No. 1 (2008)
     * @param priorReturns
     * @param covariance
     * @param P
     * @param Q
     * @param tau
     * @return
     */
    static public double[] getBLMeanReturn(double[] priorReturns, double[][] covariance, double[] P, double[][] Q, double[][] Omega, double tau) {
        RealMatrix pi = new Array2DRowRealMatrix(priorReturns);
        RealMatrix p = new Array2DRowRealMatrix(P);
        RealMatrix q = new Array2DRowRealMatrix(Q);
        RealMatrix omega = new Array2DRowRealMatrix(Omega);
        RealMatrix sigma = new Array2DRowRealMatrix(covariance);

        RealMatrix tauSigmaQ = sigma.scalarMultiply(tau).multiply(q.transpose());
        RealMatrix invQtauSigmaQplusOmega = MatrixUtils.inverse(q.multiply(tauSigmaQ).add(omega));

        RealMatrix left = tauSigmaQ.multiply(invQtauSigmaQplusOmega);
        RealMatrix right = p.add(q.multiply(pi).scalarMultiply(-1));
        return pi.add(left.multiply(right)).getColumn(0);
    }

    /**
     * 
     * Meucci, A. ”The Black-Litterman Approach: Original Model and Extensions”, Bloomberg Alpha Research & Education Paper, No. 1 (2008)
     * @param covariance
     * @param Q
     * @param Omega
     * @param tau
     * @return
     */
    static public double[][] getBLCovariance(double[][] covariance, double[][] Q, double[][] Omega, double tau) {
        RealMatrix sigma = new Array2DRowRealMatrix(covariance);
        RealMatrix q = new Array2DRowRealMatrix(Q);
        RealMatrix omega = new Array2DRowRealMatrix(Omega);

        RealMatrix tauSigmaQ = sigma.scalarMultiply(tau).multiply(q.transpose());
        RealMatrix invQtauSigmaQplusOmega = MatrixUtils.inverse(q.multiply(tauSigmaQ).add(omega));

        RealMatrix left = sigma.scalarMultiply(1 + tau);
        RealMatrix right = tauSigmaQ.scalarMultiply(tau).multiply(invQtauSigmaQplusOmega).multiply(q).multiply(sigma);

        return left.add(right.scalarMultiply(-1)).getData();
    }
}
