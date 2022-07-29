package com.ironhack.CRMManager.Exceptions;

public abstract class CRMException extends Exception {
    ErrorType errorType;
    public CRMException(ErrorType errorType) {
        super();
        this.errorType=errorType;
    }
    public CRMException(String toString) {
        super(toString);
    }

    public ErrorType getErrorType() {
        return this.errorType;
    }
}
