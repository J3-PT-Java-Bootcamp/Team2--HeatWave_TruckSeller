package com.ironhack.Exceptions;

import static com.ironhack.Exceptions.ErrorType.*;

public class ExitException extends CRMException{
    boolean unsavedData;

    public ExitException(boolean unsavedData) {
        super(EXIT);
        this.unsavedData = unsavedData;
    }
}
