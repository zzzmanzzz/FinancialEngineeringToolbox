package org.concerto.FinancialEngineeringToolbox.Util.Portfolio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import org.concerto.FinancialEngineeringToolbox.Constant.ReturnType;
import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterIsNullException;
import org.concerto.FinancialEngineeringToolbox.Exception.UndefinedParameterValueException;
import org.junit.jupiter.api.Test;


class BlackLittermanTest extends LoadData {
    @Test
    void getOmega() throws ParameterIsNullException, UndefinedParameterValueException {
        double riskFreeRate = 0.02;
        EfficientFrontier ef = new EfficientFrontier(data, riskFreeRate, ConstantForTest.TRADINGDAYS);
        double[][] cov = ef.getCovariance(ReturnType.common);
        double[] P = new double[cov.length];
        Arrays.fill(P, 0.1);
        double tau = 0.05;
        double[][] Omega = BlackLitterman.getOmega(cov, P, tau);
        assertEquals(0.00512, Omega[0][0], ConstantForTest.EPSLION) ;
    }

    @Test
    void getBLMeanReturn() {

    }

    @Test
    void getBLCovariance() {
    }
}