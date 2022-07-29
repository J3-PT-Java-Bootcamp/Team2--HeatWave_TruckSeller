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



    protected Account(IndustryType industryType, int employeeCount, String city, String country, String companyName) {
        setIndustryType(industryType);
        setEmployeeCount(employeeCount);
        setCity(city);
        setCountry(country);
        setCompanyName(companyName);
        this.contacts=new ArrayList<>();
        this.opportunities=new ArrayList<>();
    }




    //-------------------------------------------------------------------------------------------------------------PRINTABLE
    @Override
    public TextObject toTextObject() {
        return new TextObject(this.companyName).addText("Provisional account")
                .addText(" DELETE ME!").addText("ONLY FOR TESTS").addText("????");
    }

    @Override
    public String shortPrint() {
        return this.companyName;
    }

    @Override
    public TextObject printFullObject() {
        return new TextObject("Provisional account").addText(this.companyName+" DELETE ME!").addText("ONLY FOR TESTS");
    }

    @Override
    public String[] getPrintableAttributes() {
        return new String[]{"Company Name","Industry", "Employees", "City","Country" };
    }

}


