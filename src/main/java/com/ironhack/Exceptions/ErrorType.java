package com.ironhack.Exceptions;

public enum ErrorType {
    OK("All good!"),
    FORMAT_NOK("The format is not okay, please try a different format"),
    INTEGER_NOK("The input is not correct, please input an integer"),
    ID_NOK("The ID is not correct"),
    LENGTH_NOK("The length is not correct"),
    FATAL_ERR("The has been a fatal error"),
    MAIL_NOK("The email is not correct"),
    PHONE_NOK("The phone is not correct"),
    COMMAND_NOK("The command is not correct"),
    EXIT("Exit"),
    PASSWORD_NOK("Unfortunately, the password is not correct"),
    WRONG_PASSWORD("Unfortunately, the password is not correct"),
    HELP("Enter HELP to receive further instructions"),
    INDUSTRY_NOK("The industry type entered is not correct");

    ErrorType(String s) {

    }

    @Override
    public String toString() {
        return this.name();
    }
}
