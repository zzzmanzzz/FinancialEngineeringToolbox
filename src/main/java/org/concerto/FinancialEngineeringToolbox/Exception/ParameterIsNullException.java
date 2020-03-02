package org.concerto.FinancialEngineeringToolbox.Exception;

public class ParameterIsNullException extends Exception {
    public ParameterIsNullException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
