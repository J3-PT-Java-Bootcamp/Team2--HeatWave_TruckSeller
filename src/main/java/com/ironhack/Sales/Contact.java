package com.ironhack.Sales;

import com.ironhack.CRMManager.ScreenManager.InputReader;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import lombok.Data;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.CRMManager.ScreenManager.InputReader.toCamelCase;

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
        return new TextObject(this.id).addText(toCamelCase(String.valueOf(this.name) ))
                .addText(String.valueOf(this.phoneNumber)).addText(InputReader.MAIL.formatOutput(this.mail)).addText(toCamelCase(this.company));
    }

    @Override
    public String shortPrint() {
        return this.name;
    }

    @Override
    public TextObject printFullObject() {

         return new TextObject().addText("ID: "+this.id)
                 .addText("Name: "+toCamelCase(this.name)).addText("Phone: "+this.phoneNumber)
                 .addText("Email: "+InputReader.MAIL.formatOutput(this.mail))
                 .addText("Company: "+toCamelCase(this.company));
    }

    @Override
    public String[] getPrintableAttributes() {
        return new String[]{"ID", "Name", "Phone", "Mail", "Company"};
    }


}

