package org.concerto.FinancialEngineeringToolbox.Exception;

public class UndefinedParameterValueException extends Exception {
    public UndefinedParameterValueException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
