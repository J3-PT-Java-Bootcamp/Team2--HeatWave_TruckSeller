package com.ironhack.CRMManager.Exceptions;

public enum ErrorType {
    OK,
    FORMAT_NOK,
    INTEGER_NOK,
    ID_NOK,
    LENGTH_NOK,
    FATAL_ERR,
    MAIL_NOK,
    PHONE_NOK,
    PRODUCT_NOK,

    COMMAND_NOK, EXIT, PASSWORD_NOK, WRONG_PASSWORD,HELP, INDUSTRY_NOK;


    private String printName;

    ErrorType(String printName){
        this.printName=printName;
    }

    ErrorType() {
    }

    @Override
    public String toString() {
        return this.name();
    }

}
