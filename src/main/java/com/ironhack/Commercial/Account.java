package com.ironhack.Commercial;

import com.ironhack.Constants.IndustryType;
import com.ironhack.ScreenManager.Text.TextObject;

public class Account implements Printable{
    private IndustryType industryType;
    private int employeeCount;
    private String city;
    private String country;
    private Contact contact;
    private Opportunity opportunity;
    private String companyName;

    public Account(IndustryType industryType, int employeeCount, String city, String country,String companyName) {
        setIndustryType(industryType);
        setEmployeeCount(employeeCount);
        setCity(city);
        setCountry(country);
        setCompanyName(companyName);
    }

    public Account(Contact contact, Opportunity opportunity) {
        setContact(contact);
        setOpportunity(opportunity);
    }

    public IndustryType getIndustryType() {
        return industryType;
    }

    public void setIndustryType(IndustryType industryType) {
        this.industryType = industryType;
    }

    public int getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(int employeeCount) {
        this.employeeCount = employeeCount;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Opportunity getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(Opportunity opportunity) {
        this.opportunity = opportunity;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public TextObject toTextObject() {
        return new TextObject();
    }
}
