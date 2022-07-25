package com.ironhack.Commercial;

import com.ironhack.ScreenManager.Text.TextObject;
import lombok.Data;

@Data
public class Contact implements Printable{
    private String  name;
    private String id;
    private String phoneNumber;
    private String mail;
    private String company;

    public Contact(String name, String id, String phoneNumber, String mail,String company) {
        setName(name);
        setId(id);
        setPhoneNumber(phoneNumber);
        setMail(mail);
        setCompany(company);
    }

//-------------------------------------------------------------------------------------------------------------PRINTABLE
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
