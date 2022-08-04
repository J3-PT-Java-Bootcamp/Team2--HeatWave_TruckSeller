package com.ironhack.Sales;

import com.ironhack.CRMManager.Exceptions.NoCompleteObjectException;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.ironhack.CRMManager.CRMManager.crmData;

@Data
@NoArgsConstructor

public class ContactBuilder {

    private String  name;
    private String phoneNumber;
    private String mail;
    private String company;

    public Contact constructContact() throws NoCompleteObjectException {

        if(name==null || phoneNumber==null || mail==null|| company==null) throw new NoCompleteObjectException();
        Contact contact = new Contact(name, phoneNumber, mail, company);
        crmData.addContact(contact);

        return contact;

    }



}
