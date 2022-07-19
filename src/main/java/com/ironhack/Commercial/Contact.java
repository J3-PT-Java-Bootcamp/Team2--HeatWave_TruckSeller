package com.ironhack.Commercial;

public class Contact {
    private String  name;
    private String id;
    private String phoneNumber;
    private String eMail;
    private Account company;

    public Contact(String name, String id, String phoneNumber, String eMail) {
        setName(name);
        setId(id);
        setPhoneNumber(phoneNumber);
        seteMail(eMail);
    }


    public Contact(Account company) {
        setCompany(company);
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

    public Account getCompany() {
        return company;
    }

    public void setCompany(Account company) {
        this.company = company;
    }
}
