package com.ironhack.Exceptions;

public class WrongInputException extends Exception{
    /* Exception for mismatching an input. Handle by repeating user input.
        It has a ErrorType value assigned to standardize error log.
     */
    private ErrorType errorType;


    public WrongInputException(ErrorType errorType) {
        super(errorType.toString());
        this.errorType = errorType;
    }
}
