package com.ironhack.CRMManager;

import com.ironhack.CRMManager.Exceptions.GoBackException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.CRMManager.CRMManager.userOpManager;
import static com.ironhack.Constants.OpportunityStatus.CLOSE_WON;
import static org.junit.jupiter.api.Assertions.*;

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
    void closeOpportunity_test_ok() {
         userOpManager.closeOpportunity(crm.getCurrentUser(),new String[]{"CLOSE","WON","PFFE"});
         assertEquals(CLOSE_WON,crmData.getOpportunity("PFFE").getStatus());
    }

    @Test
    void createNewAccount_exceptionTest_ok()  {
        System.setIn(new ByteArrayInputStream("back\n".getBytes()));
        assertThrows(GoBackException.class,()->userOpManager.createNewAccount(crm.getCurrentUser()));

    }

    @Test
    void convertLeadToOpp_exceptionTest_ok() {
        System.setIn(new ByteArrayInputStream("menu\n".getBytes()));
        assertDoesNotThrow(()->userOpManager.convertLeadToOpp(crmData.getUser("USER"),new String[]{"CONVERT","LFFE"}));
    }

    @Test
    void viewObject_exceptionTest_ok() {
        System.setIn(new ByteArrayInputStream("back\n".getBytes()));
        assertThrows(GoBackException.class,()->userOpManager.viewObject(crm.getCurrentUser(),
                new String[]{"VIEW","LFFE"}));
    }
}