package com.ironhack.Commercial;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Constants.Product;
import com.ironhack.Exceptions.NoCompleteObjectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpportunityBuilderTest {
    @BeforeEach
    @Test
    void constructOpportunity() throws NoCompleteObjectException {
        var crm=new CRMManager(false);
        var opportunityOne = new OpportunityBuilder();
        opportunityOne.setProduct(Product.FLATBED);
        opportunityOne.setQuantity(232);
        opportunityOne.setOwner("Yep");

        var finalopportunity= opportunityOne.constructOpportunity("TEST",new ContactBuilder());
        assertEquals("12345FP",finalopportunity.getId()); //TODO TEST INDUSTRYTYPE no lagafa

    }
}