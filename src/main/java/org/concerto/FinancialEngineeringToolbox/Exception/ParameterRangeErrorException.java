package org.concerto.FinancialEngineeringToolbox.Exception;

public class ParameterRangeErrorException extends Exception {
    public ParameterRangeErrorException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
