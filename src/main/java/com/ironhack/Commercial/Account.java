package com.ironhack.Commercial;

import com.ironhack.Constants.IndustryType;

public class Account {
    private com.ironhack.Constants.IndustryType industryType;
    private int id;
    private int employeeCount;
    private String city;
    private String country;
    private Contact contact;
    private Opportunity opportunity;

    public Account(IndustryType industryType, int id, int employeeCount, String city, String country) {
        setIndustryType(industryType);
        setId(id);
        setEmployeeCount(employeeCount);
        setCity(city);
        setCountry(country);
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

    public String getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
