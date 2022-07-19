package com.ironhack.Commercial;

public class Lead {

    private String name;
    private String id;
    private String phoneNumber;
    private String eMail;
    private String companyName;

    public Lead(String name, String id, String phoneNumber, String eMail, String companyName) {
        setName(name);
        setId(id);
        setPhoneNumber(phoneNumber);
        seteMail(eMail);
        setCompanyName(companyName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Opportunity convertToOpp() {
        return new Opportunity();
    }
}
