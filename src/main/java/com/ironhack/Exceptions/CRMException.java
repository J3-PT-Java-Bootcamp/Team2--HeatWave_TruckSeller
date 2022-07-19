package com.ironhack.Exceptions;

public abstract class CRMException extends Exception {
    ErrorType errorType;
    public CRMException(ErrorType errorType) {
        super();
        this.errorType=errorType;
    }
    public CRMException(String toString) {
        super(toString);
    }

    public com.ironhack.Exceptions.ErrorType getErrorType() {
        return this.errorType;
    }
}
