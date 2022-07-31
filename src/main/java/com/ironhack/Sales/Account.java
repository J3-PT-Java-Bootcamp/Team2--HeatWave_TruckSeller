package com.ironhack.Sales;

import com.ironhack.Constants.IndustryType;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import lombok.Data;

import java.util.ArrayList;
@Data

public class Account implements Printable{

    private IndustryType industryType;
    private int employeeCount;
    private String city, country;
    private ArrayList<String> contacts, opportunities;
    private String companyName;



    public Account(IndustryType industryType, int employeeCount, String city, String country, String companyName) {
        setIndustryType(industryType);
        setEmployeeCount(employeeCount);
        setCity(city);
        setCountry(country);
        setCompanyName(companyName);
        this.contacts=new ArrayList<>();
        this.opportunities=new ArrayList<>();
    }


    @Override
    public String getId() {
        return companyName;
    }

    //-------------------------------------------------------------------------------------------------------------PRINTABLE
    @Override
    public TextObject toTextObject() {
        return new TextObject(this.companyName).addText(String.valueOf(industryType))
                .addText(String.valueOf(employeeCount)).addText(city).addText(country);
    }

    @Override
    public String shortPrint() {
        return this.companyName;
    }

    @Override
    public TextObject printFullObject() {
        return new TextObject().addText("Company name: "+this.companyName).addText("Industry: "+this.industryType).addText("Employees: "+this.employeeCount).addText("City: "+this.city).addText("Country: "+this.country);
    }

    @Override
    public String[] getPrintableAttributes() {
        return new String[]{"Company Name","Industry", "Employees", "City","Country" };
    }

}


