package com.ironhack.Commercial;


import com.ironhack.Constants.IndustryType;
import com.ironhack.Exceptions.NoCompleteObjectException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

import static com.ironhack.CRMManager.CRMManager.crmData;

@Data
@NoArgsConstructor

public class AccountBuilder {

    private IndustryType industryType;
    private int employeeCount;
    private String city, country;
    private ArrayList<String> contacts, opportunities;
    private String companyName;

    public static void save(Account accountOne) {
    }


    public Account constructAccount() throws NoCompleteObjectException {

        if(industryType==null || employeeCount ==0 || city==null|| country==null||companyName==null) throw new NoCompleteObjectException();
        Account account = new Account(industryType, employeeCount, city, country, companyName );
        if(contacts!=null) account.setContacts(contacts);
        if(opportunities!=null) account.setOpportunities(opportunities);

        crmData.addAccount(account);

        return account;

    }

    //TODO CADA UNO UN METODO QUE SE CONSTRUIE GUARDAR EN CMR DATA Y AUMENTE LOS CONTADORES GLOBALES Y DE USER PARA VER LAS ESTADISTICAS

}
