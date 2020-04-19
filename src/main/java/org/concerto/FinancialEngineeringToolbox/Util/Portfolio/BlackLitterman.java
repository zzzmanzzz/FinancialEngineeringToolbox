package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.DiagonalMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;

/**
 * Get parameters of Black-Litterman theory
 * Reference:
 * Meucci, A. ”The Black-Litterman Approach: Original Model and Extensions”, Bloomberg Alpha Research & Education Paper, No. 1 (2008)
 */
public class BlackLitterman {

    /**
     * Get risk aversion parameter
     * Three rates should be in the same period and correspond to the frequency of portfolio returns.
     * @param marketMeanReturn market returns
     * @param marketVariance Variance of market returns
     * @param riskFreeRate Risk free rate
     * @return risk aversion
     */
    static public double getMarketImpliedRiskAversion(double marketMeanReturn, double marketVariance, double riskFreeRate) {
        return (marketMeanReturn - riskFreeRate) / (marketVariance);
    }

    /**
     * Get prior return according to market data
     * @param covariance Portfolio covariance
     * @param riskAversion Risk aversion parameter
     * @param riskFreeRate Risk free rate
     * @param marketCap Market capability of portfolio elements
     * @return Prior return array
     */
    static public double[] getPriorReturns(double[][] covariance, double riskAversion, double riskFreeRate, double[] marketCap) {
        final double sum = Arrays.stream(marketCap).sum();
        double[] marketCapWeight = Arrays.stream(marketCap).map(d -> d / sum).toArray();
        RealMatrix sigma = new Array2DRowRealMatrix(covariance);
        return sigma.multiply(new Array2DRowRealMatrix(marketCapWeight))
                .scalarMultiply(riskAversion)
                .scalarAdd(riskFreeRate).getColumn(0);
    }

    /**
     * Get Omega matrix
     * @param covariance Portfolio covariance
     * @param P View matrix
     * @param tau
     * @return Diagonal elements of omega matrix
     */
    static public double[] getOmega(double[][] covariance, double[][] P, double tau) {
        RealMatrix sigma = new Array2DRowRealMatrix(covariance);
        RealMatrix p = new Array2DRowRealMatrix(P);
        RealMatrix Omega = p
            .multiply(sigma)
            .multiply(p.transpose())
            .scalarMultiply(tau);
        double[] ret = new double[Omega.getRowDimension()];
        for(int i = 0 ; i < ret.length ; i++ ) {
            ret[i] = Omega.getEntry(i, i);
        }
        return ret;
    }

    /**
     * Get Black-Litterman Return
     * @param priorReturns Prior return array
     * @param covariance Portfolio covariance
     * @param Q
     * @param P View matrix
     * @param tau
     * @return Black-Litterman Return
     */
    static public double[] getBLMeanReturn(double[] priorReturns, double[][] covariance, double[] Q, double[][] P, double[] Omega, double tau) {
        RealMatrix pi = new Array2DRowRealMatrix(priorReturns);
        RealMatrix q = new Array2DRowRealMatrix(Q);
        RealMatrix p = new Array2DRowRealMatrix(P);
        RealMatrix omega = new DiagonalMatrix(Omega);
        RealMatrix sigma = new Array2DRowRealMatrix(covariance);

        RealMatrix tauSigmaP = sigma.scalarMultiply(tau).multiply(p.transpose());
        RealMatrix invPtauSigmaPplusOmega = MatrixUtils.inverse(p.multiply(tauSigmaP).add(omega));

        RealMatrix left = tauSigmaP.multiply(invPtauSigmaPplusOmega);
        RealMatrix right = q.add(p.multiply(pi).scalarMultiply(-1));
        return pi.add(left.multiply(right)).getColumn(0);
    }

    /**
     * Get Black-Litterman Covariance
     * @param covariance Portfolio covariance
     * @param P View matrix
     * @param Omega
     * @param tau
     * @return Black-Litterman Covariance
     */
    static public double[][] getBLCovariance(double[][] covariance, double[][] P, double[] Omega, double tau) {
        RealMatrix sigma = new Array2DRowRealMatrix(covariance);
        RealMatrix p = new Array2DRowRealMatrix(P);
        RealMatrix omega = new DiagonalMatrix(Omega);

        RealMatrix tauSigmaP = sigma.scalarMultiply(tau).multiply(p.transpose());
        RealMatrix invPtauSigmaQplusOmega = MatrixUtils.inverse(p.multiply(tauSigmaP).add(omega));

        RealMatrix left = sigma.scalarMultiply(1 + tau);
        RealMatrix right = tauSigmaP.scalarMultiply(tau).multiply(invPtauSigmaQplusOmega).multiply(p).multiply(sigma);

        return left.add(right.scalarMultiply(-1)).getData();
    }
}
