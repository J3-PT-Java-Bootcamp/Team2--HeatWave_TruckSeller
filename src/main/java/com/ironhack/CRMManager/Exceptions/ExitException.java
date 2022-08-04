package com.ironhack.CRMManager.Exceptions;

public class ExitException extends CRMException{

    public ExitException() {
        super(ErrorType.EXIT);
    }
}
