package org.concerto.FinancialEngineeringToolbox.Exception;

public class DimensionMismatchException extends Exception {
    public DimensionMismatchException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
