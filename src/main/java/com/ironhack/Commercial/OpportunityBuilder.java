package com.ironhack.Commercial;

import com.ironhack.Constants.OpportunityStatus;
import com.ironhack.Constants.Product;
import com.ironhack.Exceptions.NoCompleteObjectException;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.ironhack.CRMManager.CRMManager.crmData;

@Data
@NoArgsConstructor
public class OpportunityBuilder {

    private String id;
    private Product product;
    private int quantity;
    private String decisionMakerID;
    private OpportunityStatus status;
    private String account_companyName;//Saves only the companyName as it is the key in the account map
    private String owner;//Saves only username as it is the key in the user map

    public Opportunity constructOpportunity(String accountName, ContactBuilder contactBuilder) throws NoCompleteObjectException {

        if (id == null || product == null || quantity == 0 || decisionMakerID == null || status == null || account_companyName == null || owner == null)
            throw new NoCompleteObjectException();

        Opportunity opportunity = new Opportunity();//TODO pasar parametres

        var account = crmData.getAccount(accountName);
        account.getOpportunities().add(opportunity.getId());

        contactBuilder.setCompany(account.getCompanyName());
        contactBuilder.constructContact();

        crmData.addOpportunity(opportunity);

        return opportunity;

    }
}
