package com.ironhack.Sales;

import com.ironhack.CRMManager.Exceptions.NoCompleteObjectException;
import com.ironhack.Constants.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.ironhack.CRMManager.CRMManager.crmData;

@Data
@NoArgsConstructor
public class OpportunityBuilder {
    private Product product;
    private int quantity;
    private String owner;//Saves only username as it is the key in the user map

    public Opportunity constructOpportunity(String accountName, ContactBuilder contactBuilder) throws NoCompleteObjectException {

        if (product == null || quantity == 0 || owner == null)
            throw new NoCompleteObjectException();
        var account = crmData.getAccount(accountName);
        contactBuilder.setCompany(account.getCompanyName());
        var contact= contactBuilder.constructContact();
        Opportunity opportunity = new Opportunity(product,quantity, contact.getId(),owner,accountName);//TODO pasar parametres
        crmData.addOpportunity(opportunity);
        account.getOpportunities().add(opportunity.getId());
        return opportunity;

    }

}
