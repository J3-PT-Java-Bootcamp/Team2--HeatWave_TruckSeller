package com.ironhack.Exceptions;

public class LogoutException extends CRMException{
    public LogoutException(com.ironhack.Exceptions.ErrorType errorType) {
        super(errorType);
    }

    public LogoutException(String toString) {
        super(toString);
    }
}
