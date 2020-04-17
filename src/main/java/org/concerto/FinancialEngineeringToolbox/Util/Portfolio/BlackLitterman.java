package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.concerto.FinancialEngineeringToolbox.Exception.DimensionMismatchException;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class BlackLitterman {

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

    static public double[] getPriorReturns(double[][] covariance, double riskAversion, double riskFreeRate, double[] marketCap) {
        final double sum = Arrays.stream(marketCap).sum();
        double[] marketCapWeight = Arrays.stream(marketCap).map(d -> d / sum).toArray();
        RealMatrix sigma = new Array2DRowRealMatrix(covariance);
        return sigma.multiply(new Array2DRowRealMatrix(marketCapWeight))
                .scalarMultiply(riskAversion)
                .scalarAdd(riskFreeRate).getColumn(0);
    }

    static public double[][] getOmega(double[][] covariance, double[][] P, double tau) {
        RealMatrix sigma = new Array2DRowRealMatrix(covariance);
        RealMatrix p = new Array2DRowRealMatrix(P);
        RealMatrix Omega = p
            .multiply(sigma)
            .multiply(p.transpose())
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
     * @param P
     * @param tau
     * @return
     */
    static public double[] getBLMeanReturn(double[] priorReturns, double[][] covariance, double[] Q, double[][] P, double[][] Omega, double tau) {
        RealMatrix pi = new Array2DRowRealMatrix(priorReturns);
        RealMatrix q = new Array2DRowRealMatrix(Q);
        RealMatrix p = new Array2DRowRealMatrix(P);
        RealMatrix omega = new Array2DRowRealMatrix(Omega);
        RealMatrix sigma = new Array2DRowRealMatrix(covariance);

        RealMatrix tauSigmaP = sigma.scalarMultiply(tau).multiply(p.transpose());
        RealMatrix invPtauSigmaPplusOmega = MatrixUtils.inverse(p.multiply(tauSigmaP).add(omega));

        RealMatrix left = tauSigmaP.multiply(invPtauSigmaPplusOmega);
        RealMatrix right = q.add(p.multiply(pi).scalarMultiply(-1));
        return pi.add(left.multiply(right)).getColumn(0);
    }

    /**
     *
     * Meucci, A. ”The Black-Litterman Approach: Original Model and Extensions”, Bloomberg Alpha Research & Education Paper, No. 1 (2008)
     * @param covariance
     * @param P
     * @param Omega
     * @param tau
     * @return
     */
    static public double[][] getBLCovariance(double[][] covariance, double[][] P, double[][] Omega, double tau) {
        RealMatrix sigma = new Array2DRowRealMatrix(covariance);
        RealMatrix p = new Array2DRowRealMatrix(P);
        RealMatrix omega = new Array2DRowRealMatrix(Omega);

        RealMatrix tauSigmaP = sigma.scalarMultiply(tau).multiply(p.transpose());
        RealMatrix invPtauSigmaQplusOmega = MatrixUtils.inverse(p.multiply(tauSigmaP).add(omega));

        RealMatrix left = sigma.scalarMultiply(1 + tau);
        RealMatrix right = tauSigmaP.scalarMultiply(tau).multiply(invPtauSigmaQplusOmega).multiply(p).multiply(sigma);

        return left.add(right.scalarMultiply(-1)).getData();
    }

    static protected double[][] parseP(Map<String, double[]> P, double[] Q, Map<String, double[]> data) throws DimensionMismatchException {
        Set<String> Pkey = P.keySet();
        Set<String> Dkey = data.keySet();
        if(Pkey.size() != Dkey.size() || !Pkey.containsAll(Dkey)) {
            String msg = String.format("Key mismatch P: %s, data: %s", Arrays.toString(Pkey.toArray()), Arrays.toString(Dkey.toArray()));
            throw new DimensionMismatchException(msg, null);
        }

        String[] symbols = Pkey.toArray(new String[Pkey.size()]);
        double[][] ret = new double[symbols.length][];

        int size = Q.length;
        for(int i = 0; i < symbols.length; i++) {
            String s = symbols[i];
            if(P.get(s).length != size) {
                String msg = String.format("P view length mismatch, key: %s, actual length: %d, expect length: %d", s, P.get(s).length, size);
                throw new DimensionMismatchException(msg, null);
            }
            ret[i] = P.get(s);
        }
        RealMatrix tmp = new Array2DRowRealMatrix(ret);
       return tmp.transpose().getData();
    }

}
