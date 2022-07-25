package com.ironhack.Commercial;

import com.ironhack.Constants.IndustryType;
import com.ironhack.ScreenManager.Text.TextObject;
import lombok.Data;

import java.util.ArrayList;
@Data
public class Account implements Printable{

    private IndustryType industryType;
    private int employeeCount;
    private String city, country;
    private ArrayList<String> contacts, opportunities;
    private String companyName;



    public Account(IndustryType industryType, int employeeCount, String city, String country,String companyName) {
        setIndustryType(industryType);
        setEmployeeCount(employeeCount);
        setCity(city);
        setCountry(country);
        setCompanyName(companyName);
    }

//-------------------------------------------------------------------------------------------------------------PRINTABLE
    @Override
    public TextObject toTextObject() {
        return new TextObject();
    }

    @Override
    public String shortPrint() {
        return this.companyName;
    }

    @Override
    public TextObject printFullObject() {
        return null;
    }
}
