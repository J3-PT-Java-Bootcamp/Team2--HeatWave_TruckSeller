package com.ironhack.CRMManager.Exceptions;

public class ExitException extends CRMException{
    boolean unsavedData;

    public ExitException(boolean unsavedData) {
        super(ErrorType.EXIT);
        this.unsavedData = unsavedData;
    }
}
