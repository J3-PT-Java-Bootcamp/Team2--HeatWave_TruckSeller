package com.ironhack.Commercial;

import com.ironhack.ScreenManager.Text.TextObject;
import lombok.Data;

@Data
public class Lead implements Printable{

    private String name;
    private String id;
    private String phoneNumber;
    private String mail;
    private String companyName;

    public Lead(String name, String id, String phoneNumber, String mail, String companyName) {
        setName(name);
        setId(id);
        setPhoneNumber(phoneNumber);
        setMail(mail);
        setCompanyName(companyName);
    }



//-------------------------------------------------------------------------------------------------------------PRINTABLE
    @Override
    public TextObject toTextObject() {
        return new TextObject()
                .addText(getId())
                .addText(getName())
                .addText(getPhoneNumber())
                .addText(getMail())
                .addText(getCompanyName());
    }

    @Override
    public String shortPrint() {
        return null;
    }

    @Override
    public TextObject printFullObject() {
        return new TextObject()
                .addText("- Lead ID: "+getId())
                .addText("- Name: "+getName())
                .addText("- Phone Number: "+getPhoneNumber())
                .addText("- Mail : "+getMail())
                .addText("- Company: "+getCompanyName());
    }

    @Override
    public String[] getPrintableAttributes() {
        return new String[]{"ID", "Name", "Phone", "Mail", "Company"};
    }
}
