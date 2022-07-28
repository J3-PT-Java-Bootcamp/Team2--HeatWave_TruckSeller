package com.ironhack.CRMManager;

import com.ironhack.Commercial.Account;
import com.ironhack.Commercial.Opportunity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CRMDataTest {
    CRMData crmData;
    @BeforeEach
    void setUp() {
        crmData = new CRMData();

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void loadCRMData() {
    }

    @Test
    void getNextID() {
        assertEquals("OPFFE",crmData.getNextID(Opportunity.class));
    }


    @Test
    void addToUserList() {
    }

    @Test
    void addLead() {
    }

    @Test
    void removeLead() {
    }

    @Test
    void getLead() {
    }

    @Test
    void getOpportunity() {
    }

    @Test
    void addOpportunity() {
    }

    @Test
    void removeOpportunity() {
    }

    @Test
    void getAccount() {
    }

    @Test
    void addAccount() {
    }

    @Test
    void removeAccount() {
    }

    @Test
    void getContact() {
    }

    @Test
    void addContact() {
    }

    @Test
    void getUsers() {
    }

    @Test
    void removeContact() {
    }

    @Test
    void getUnknownObject() {
    }

    @Test
    void removeUnknownObject() {
    }

    @Test
    void addUnknownObject() {
    }

    @Test
    void existsObject() {
    }

    @Test
    void isEmptyMap() {
        assertTrue(crmData.isEmptyMap(Account.class));
        crmData.addOpportunity(new Opportunity());
        assertFalse(crmData.isEmptyMap(Opportunity.class));

    }
}