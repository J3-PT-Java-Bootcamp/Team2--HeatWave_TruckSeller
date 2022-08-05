package com.ironhack.Sales;

import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import lombok.Data;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.CRMManager.ScreenManager.InputReader.*;

@Data
public class Contact implements Printable{
    private String  name;
    private String id;
    private String phoneNumber;
    private String mail;
    private String company;

    public Contact(String name, String phoneNumber, String mail,String company) {
        setName(name);
        setId(crmData.getNextID(getClass()));
        setPhoneNumber(phoneNumber);
        setMail(mail);
        setCompany(company);
    }

//-------------------------------------------------------------------------------------------------------------PRINTABLE
    @Override
    public TextObject toTextObject() {
        return new TextObject(this.id).addText(OPEN.formatOutput(String.valueOf(this.name) ))
                .addText(String.valueOf(this.phoneNumber)).addText(MAIL.formatOutput(this.mail)).addText(OPEN.formatOutput(this.company));
    }

    @Override
    public String shortPrint() {
        return OPEN.formatOutput(this.name);
    }

    @Override
    public TextObject printFullObject() {

         return new TextObject().addText("ID: "+this.id)
                 .addText("Name: "+OPEN.formatOutput(this.name)).addText("Phone: "+this.phoneNumber)
                 .addText("Email: "+MAIL.formatOutput(this.mail))
                 .addText("Company: "+OPEN.formatOutput(this.company));
    }

    @Override
    public String[] getPrintableAttributes() {
        return new String[]{"ID", "Name", "Phone", "Mail", "Company"};
    }


}

