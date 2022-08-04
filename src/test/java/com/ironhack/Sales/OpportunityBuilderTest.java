package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.Exceptions.NoCompleteObjectException;
import com.ironhack.Constants.Product;
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
        opportunityOne.setOwner("USER");
        var contBuilder= new ContactBuilder();
        contBuilder.setMail("sadf@sdf.sadf");
        contBuilder.setName("GILBERTO");
        contBuilder.setPhoneNumber("93456456");
        var finalOpportunity= opportunityOne.constructOpportunity("ACC",contBuilder);
        assertEquals("USER",finalOpportunity.getOwner());

    }
}