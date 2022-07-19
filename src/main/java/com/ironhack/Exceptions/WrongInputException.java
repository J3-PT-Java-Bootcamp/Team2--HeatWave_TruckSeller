package com.ironhack.Exceptions;

public class WrongInputException extends CRMException{
    /* Exception for mismatching an input. Handle by repeating user input.
        It has a ErrorType value assigned to standardize error log.
     */


    public WrongInputException(ErrorType errorType) {
        super(errorType.toString());
        this.errorType = errorType;
    }
}
