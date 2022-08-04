package com.ironhack.CRMManager.Exceptions;

public enum ErrorType {
    OK("All good!"),
    FORMAT_NOK("The format is not okay, please enter HELP for more info"),
    INTEGER_NOK("The input is not correct, please input an integer"),
    ID_NOK("The ID is not correct"),
    FATAL_ERR("The has been a fatal error"),
    MAIL_NOK("The email format is not correct"),
    PHONE_NOK("The phone format is not correct"),
    PRODUCT_NOK("The PRODUCT type entered is not correct. Enter FLAT,BOX or HYBRID"),

    COMMAND_NOK("Command not recognised. Enter HELP to check available commands"),
    EXIT("Exit"),
    PASSWORD_NOK("Password does not match or have ilegal characters"),
    WRONG_PASSWORD("Unfortunately, the password is not correct"),
    HELP("Enter HELP to receive further instructions"),
    INDUSTRY_NOK("The industry type entered is not correct, enter PRODUCE,MEDICAL, COMMERCE or OTHER");




    private final String printInfo;

    ErrorType(String printInfo){
        this.printInfo = printInfo;
    }

    @Override
    public String toString() {
        return this.printInfo;
    }
}
