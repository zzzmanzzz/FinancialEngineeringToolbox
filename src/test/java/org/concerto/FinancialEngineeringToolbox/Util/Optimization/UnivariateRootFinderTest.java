package org.concerto.FinancialEngineeringToolbox.Util.Optimization;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.concerto.FinancialEngineeringToolbox.ConstantForTest;
import org.concerto.FinancialEngineeringToolbox.Exception.ParameterRangeErrorException;
import org.concerto.FinancialEngineeringToolbox.Util.BlackScholes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnivariateRootFinderTest {

    @Test
    void solve() {
        UnivariateFunction u = s -> {
            BlackScholes bs = new BlackScholes(100, 100, s, 0.06, 1, 0);
            return bs.getCallPrice() - 25.91834;
        };

        Solution s = UnivariateRootFinder.solve(u,ConstantForTest.EPSLION,0.001, 10.0);
        assertEquals(0.6, s.getOptimizedParameter()[0], ConstantForTest.EPSLION);

    }
}