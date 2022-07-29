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
        Contact contact = new Contact(name, crmData.getNextID(Contact.class), phoneNumber, mail, company);
        crmData.addContact(contact);

        return contact;

    }

    //TODO CADA UNO UN METODO QUE SE CONSTRUIE GUARDAR EN CMR DATA Y AUMENTE LOS CONTADORES GLOBALES Y DE USER PARA VER LAS ESTADISTICAS


}
