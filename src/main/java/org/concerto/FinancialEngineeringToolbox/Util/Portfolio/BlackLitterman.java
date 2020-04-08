package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;

public class BlackLitterman {


    static public double getMarketImpliedRiskAversion(double marketMeanReturn, double marketVariance, double riskFreeRate, int frequency) {
        return (marketMeanReturn - riskFreeRate / frequency) / marketVariance;
    }

    static public double[] getPriorReturns(double[][] covariance, double riskAversion, double riskFreeRate, double[] marketCap, int frequency) {
        final double sum = Arrays.stream(marketCap).sum();
        double[] marketCapWeight = Arrays.stream(marketCap).map(d -> d / sum).toArray();
        RealMatrix sigma = new Array2DRowRealMatrix(covariance);
        return sigma.multiply(new Array2DRowRealMatrix(marketCapWeight))
                .scalarMultiply(riskAversion)
                .scalarAdd(riskFreeRate / frequency).getColumn(0);
    }

    static public double[] getBLMeanReturn(double[] priorReturns, double[][] covariance, double[] P, double[][] Q, double tau) {
        RealMatrix pi = new Array2DRowRealMatrix(priorReturns);
        RealMatrix p = new Array2DRowRealMatrix(P);
        RealMatrix q = new Array2DRowRealMatrix(Q);
        RealMatrix sigma = new Array2DRowRealMatrix(covariance);

        RealMatrix invTauSigma = MatrixUtils.inverse(sigma.scalarMultiply(tau));
        RealMatrix invOmega = MatrixUtils.inverse(p.transpose().multiply(sigma).multiply(p).scalarMultiply(tau));
        RealMatrix PtOmegaInv = p.transpose().multiply(invOmega);

        RealMatrix left = MatrixUtils.inverse( invOmega.add(PtOmegaInv.multiply(p)));
        RealMatrix right = invTauSigma.multiply(pi).add(PtOmegaInv.multiply(q));
        return left.multiply(right).getColumn(0);
    }

    static public double[][] getBLCovariance(double[][] covariance, double[] P, double tau) {
        RealMatrix sigma = new Array2DRowRealMatrix(covariance);
        RealMatrix p = new Array2DRowRealMatrix(P);

        RealMatrix invTauSigma = MatrixUtils.inverse(sigma.scalarMultiply(tau));
        RealMatrix invOmega = MatrixUtils.inverse(p.transpose().multiply(sigma).multiply(p).scalarMultiply(tau));
        RealMatrix PtOmegaInv = p.transpose().multiply(invOmega);

        return sigma.add(MatrixUtils.inverse(invTauSigma.add(PtOmegaInv.multiply(p)))).getData();
    }
}
