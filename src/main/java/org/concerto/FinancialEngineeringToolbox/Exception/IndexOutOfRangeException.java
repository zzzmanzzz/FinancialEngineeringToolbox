package org.concerto.FinancialEngineeringToolbox.Exception;

public class IndexOutOfRangeException extends Exception {
    public IndexOutOfRangeException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
