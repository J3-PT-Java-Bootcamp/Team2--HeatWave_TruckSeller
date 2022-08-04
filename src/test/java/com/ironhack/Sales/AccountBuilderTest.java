package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.Exceptions.NoCompleteObjectException;
import com.ironhack.Constants.IndustryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountBuilderTest {
   @BeforeEach
   @Test
    void constructAccount_test_1() throws NoCompleteObjectException {
       var crm=new CRMManager(false);
       var accountOne = new AccountBuilder();
       accountOne.setCity("Barcelona");
       accountOne.setContacts(new ArrayList<>());
       accountOne.setCountry("spain");
       accountOne.setEmployeeCount(9);
       accountOne.setCompanyName("Cocacola");
       accountOne.setIndustryType(IndustryType.MEDICAL);
       accountOne.setOpportunities(new ArrayList<>());

       var finalAccount= accountOne.constructAccount();
       assertEquals("Barcelona",finalAccount.getCity()); //TODO TEST INDUSTRYTYPE no lagafa


    }
}