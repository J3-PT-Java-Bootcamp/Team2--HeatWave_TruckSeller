package com.ironhack.Constants;

import com.ironhack.CRMManager.Exceptions.WrongInputException;

import static com.ironhack.CRMManager.Exceptions.ErrorType.INDUSTRY_NOK;

public enum IndustryType {
    PRODUCE("Producer","PRODUCE","PRODUCER","PROD","MAKER","FABRIC","INDUSTRY"),
    ECOMMERCE("e-Commerce", "ECOMMERCE","COMMERCE","COMM","EC","E","E COMMERCE","ECOMM","ECOM"),
    MANUFACTURING("Manufacturing","MANUFACTURING","MANU","ARTISAN"),
    MEDICAL("Medical","MEDICAL","PHARMA","HEALTH","MED"),
    OTHER("Other Industries","OTHER","NO");

    private final String[] keywords;
    private final String printName;

    IndustryType(String printName, String... keywords){
        this.printName=printName;
        this.keywords=keywords;

    }
    private boolean containsKeyword(String input){
        for(String key: this.keywords) if (input.equalsIgnoreCase(key)) return true;
        return input.equalsIgnoreCase(printName);
    }
    @Override
    public String toString() {
        return this.printName;
    }

    static public IndustryType checkAllIndustries(String input) throws WrongInputException {
        var pInput= input.trim().toUpperCase();
        for (IndustryType val :IndustryType.values() ) if (val.containsKeyword(pInput)) return val;
        throw new WrongInputException(INDUSTRY_NOK);
    }



}
