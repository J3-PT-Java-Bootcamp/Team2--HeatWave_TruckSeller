package com.ironhack.CRMManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ironhack.CRMManager.CRMManager.crmData;

class UserTest {
    CRMManager crm;
    User user;
    @BeforeEach
    void setUp() {
        crm = new CRMManager(false);
        user = crmData.getUser("USER");
    }
    @Test
    void removeFromLeadList() {
    }

    @Test
    void addToRecentObjects() {
    }

    @Test
    void toTextObject() {
    }

    @Test
    void shortPrint() {
    }

    @Test
    void printFullObject() {
    }

    @Test
    void getPrintableAttributes() {
    }
}