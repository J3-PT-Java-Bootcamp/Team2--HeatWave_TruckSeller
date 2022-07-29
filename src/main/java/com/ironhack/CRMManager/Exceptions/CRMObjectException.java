package com.ironhack.CRMManager.Exceptions;

public abstract class CRMObjectException extends CRMException {
    public CRMObjectException(ErrorType errorType) {
        super(errorType);
    }

}
