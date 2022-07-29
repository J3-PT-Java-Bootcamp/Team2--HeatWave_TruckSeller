package com.ironhack.CRMManager.Exceptions;

public class LogoutException extends CRMException{
    public LogoutException(ErrorType errorType) {
        super(errorType);
    }

    public LogoutException(String toString) {
        super(toString);
    }
}
