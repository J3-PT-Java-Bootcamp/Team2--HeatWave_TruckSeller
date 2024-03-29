package com.ironhack.Sales;


import com.ironhack.CRMManager.Exceptions.NoCompleteObjectException;
import com.ironhack.Constants.IndustryType;
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

    public Account constructAccount() throws NoCompleteObjectException {

        if(industryType==null || employeeCount ==0 || city==null|| country==null||companyName==null) throw new NoCompleteObjectException();

        Account account = new Account(industryType, employeeCount, city, country, companyName );
        if(contacts!=null) account.setContacts(contacts);
        if(opportunities!=null) account.setOpportunities(opportunities);

        crmData.addAccount(account);

        return account;

    }


}
