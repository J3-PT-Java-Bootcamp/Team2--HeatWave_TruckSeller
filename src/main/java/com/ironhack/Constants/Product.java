package com.ironhack.Constants;

import com.ironhack.CRMManager.Exceptions.WrongInputException;

import static com.ironhack.CRMManager.Exceptions.ErrorType.PRODUCT_NOK;

public enum Product {
    HYBRID("Hybrid", "HYBRID TRUCK", "HYBT", "HYBRID"),
    FLATBED("Flatbed", "FLATBED", "FLBT", "FLAT"),
    BOX("Box","BOXED", "BOXT", "BOX");

    private final String[] keywords;
    private final String printName;

    Product(String printName, String... keywords){
        this.printName=printName;
        this.keywords=keywords;
    }
    private boolean containsKeyword(String input){
        for(String key: keywords) if (input.equalsIgnoreCase(key)) return true;
        return input.equalsIgnoreCase(printName);
    }
    @Override
    public String toString() {
        return this.printName+" Truck";
    }

    static public Product checkAllProducts(String input) throws WrongInputException {
        var pInput= input.trim().toUpperCase();
        for (Product val :Product.values() ) if (val.containsKeyword(input)) return val;
        throw new WrongInputException(PRODUCT_NOK);
    }



}
