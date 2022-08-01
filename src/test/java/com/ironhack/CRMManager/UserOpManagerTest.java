package com.ironhack.CRMManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.CRMManager.CRMManager.userOpManager;
import static com.ironhack.Constants.OpportunityStatus.CLOSE_WON;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserOpManagerTest {

    CRMManager crm;
    @BeforeEach
    void setUp() {
        crm = new CRMManager(false);
        crm.setCurrentUser(crmData.getUser("USER"));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void closeOpportunity() {
        System.setIn(new ByteArrayInputStream("HELP\n".getBytes()));
         userOpManager.closeOpportunity(crm.getCurrentUser(),new String[]{"CLOSE","WON","PFFE"});
         assertEquals(CLOSE_WON,crmData.getOpportunity("PFFE").getStatus());
    }

    @Test
    void createNewAccount() {

    }

    @Test
    void convertLeadToOpp() {
    }

    @Test
    void viewObject() {
    }
}