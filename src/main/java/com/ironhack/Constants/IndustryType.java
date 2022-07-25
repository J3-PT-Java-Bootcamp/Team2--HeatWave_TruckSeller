package com.ironhack.Constants;

public enum IndustryType {
    PRODUCE("Producer"),
    ECOMMERCE("e-Commerce"),
    MANUFACTURING("Manufacturing"),
    MEDICAL("Medical"),
    OTHER("Other Industries");

    private String printName;

    IndustryType(String printName){
        this.printName=printName;
    }

    @Override
    public String toString() {
        return this.printName;
    }

}
