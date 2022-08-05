package com.ironhack.Sales;

import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.CRMManager.ScreenManager.InputReader.MAIL;
import static com.ironhack.CRMManager.ScreenManager.InputReader.toCamelCase;

@Data
@AllArgsConstructor
public class Lead implements Printable{

    private String name;
    private String id;
    private String phoneNumber;
    private String mail;
    private String companyName;




    public Lead(String name, String phoneNumber, String mail, String companyName) {
        setName(name);
        setId(crmData.getNextID(Lead.class));
        setPhoneNumber(phoneNumber);
        setMail(mail);
        setCompanyName(companyName);

    }



//-------------------------------------------------------------------------------------------------------------PRINTABLE
    @Override
    public TextObject toTextObject() {
        return new TextObject()
                .addText(getId())
                .addText(toCamelCase(getName()))
                .addText(getPhoneNumber())
                .addText(MAIL.formatOutput(getMail()))
                .addText(toCamelCase(getCompanyName()));
    }

    @Override
    public String shortPrint() {
        return getName()+" from "+getCompanyName();
    }

    @Override
    public TextObject printFullObject() {
        return new TextObject()
                .addText("- Lead ID: "+getId())
                .addText("- Name: "+toCamelCase(getName()))
                .addText("- Phone Number: "+getPhoneNumber())
                .addText("- Mail : "+MAIL.formatOutput(getMail()))
                .addText("- Company: "+toCamelCase(getCompanyName()));
    }

    @Override
    public String[] getPrintableAttributes() {
        return new String[]{"ID", "Name", "Phone", "Mail", "Company"};
    }





}



