package com.ironhack.Commercial;

import com.ironhack.ScreenManager.Text.TextObject;

public class Contact implements Printable{
    private String  name;
    private String id;
    private String phoneNumber;
    private String eMail;
    private String company;

    public Contact(String name, String id, String phoneNumber, String eMail,String company) {
        setName(name);
        setId(id);
        setPhoneNumber(phoneNumber);
        seteMail(eMail);
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public TextObject toTextObject() {
        return new TextObject();
    }

    @Override
    public String shortPrint() {
        return null;
    }

    @Override
    public TextObject printFullObject() {
        return null;
    }
}
