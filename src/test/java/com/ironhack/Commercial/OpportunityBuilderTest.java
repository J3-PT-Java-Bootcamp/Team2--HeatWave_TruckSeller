package com.ironhack.Commercial;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Constants.OpportunityStatus;
import com.ironhack.Constants.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpportunityBuilderTest {
    @BeforeEach
    @Test
    void constructOpportunity() {
        var crm=new CRMManager(false);
        var opportunityOne = new OpportunityBuilder();
        opportunityOne.setId("12345FP");
        opportunityOne.setProduct();
        opportunityOne.setQuantity(232);
        opportunityOne.setStatus(OpportunityStatus.OPEN);
        opportunityOne.setAccount_companyName("Cocacola");
        opportunityOne.setOwner("Yep");

        var finalopportunity= opportunityOne.constructOpportunity();
        assertEquals("12345FP",finalopportunity.getId()); //TODO TEST INDUSTRYTYPE no lagafa

    }
}