package com.ironhack.Sales;

import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Constants.IndustryType;
import lombok.Data;

import java.util.ArrayList;

import static com.ironhack.CRMManager.ScreenManager.InputReader.toCamelCase;

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
        return new TextObject(toCamelCase(this.companyName)).addText(industryType.toString())
                .addText(String.valueOf(employeeCount)).addText(toCamelCase(city)).addText(toCamelCase(country));
    }

    @Override
    public String shortPrint() {
        return toCamelCase(this.companyName)+" "+toCamelCase(city);
    }

    @Override
    public TextObject printFullObject() {
        return new TextObject().addText("Company name: "+toCamelCase(this.companyName))
                .addText("Industry: "+this.industryType.toString()).addText("Employees: "+this.employeeCount)
                .addText("City: "+toCamelCase(this.city))
                .addText("Country: "+toCamelCase(this.country));
    }

    @Override
    public String[] getPrintableAttributes() {
        return new String[]{"Company Name","Industry", "Employees", "City","Country" };
    }

}


