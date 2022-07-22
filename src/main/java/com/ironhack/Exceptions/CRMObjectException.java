package com.ironhack.Exceptions;

public abstract class CRMObjectException extends CRMException {
    public CRMObjectException(com.ironhack.Exceptions.ErrorType errorType) {
        super(errorType);
    }

}
