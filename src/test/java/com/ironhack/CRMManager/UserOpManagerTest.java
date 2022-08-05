package com.ironhack.CRMManager;

import com.ironhack.CRMManager.Exceptions.GoBackException;
import com.ironhack.Constants.Product;
import com.ironhack.Sales.Contact;
import com.ironhack.Sales.Opportunity;
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


    @Test
    void discardObject_test_ok() {
        crmData.addUser(new User("TEST","TEST",false));

        System.setIn(new ByteArrayInputStream("yes\n".getBytes()));
        userOpManager.discardObject(crmData.getUser("ADMIN"),crmData.getUser("TEST"));
        assertNull(crmData.getUser("TEST"));
    }

    @Test
    void addToFavourites() {
        System.setIn(new ByteArrayInputStream("yes\n".getBytes()));
        var cont = new Contact("ANTONIO","93456956","ANTONIO@APPLE.COM","ACC");
        crmData.addContact(cont);
        var op = new Opportunity(Product.BOX, 12, cont.getId(), "USER", "ACC");
        crmData.addOpportunity(op);
        userOpManager.addToFavourites(crmData.getUser("USER"),new String[]{"FAV",op.getId()});
        assertTrue(crmData.getUser("USER").getFavourites().contains(op.getId()));

    }
    @Test
    void convertLeadToOpp_test_nullInput() {
        assertNull(userOpManager.convertLeadToOpp(crm.getCurrentUser(),null));
    }

    @Test
    void testToString() {
        assertDoesNotThrow(userOpManager::toString);
    }
}