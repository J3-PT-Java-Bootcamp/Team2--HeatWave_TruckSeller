package com.ironhack.CRMManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    CRMManager crm;
    User user;
    @BeforeEach
    void setUp() {
        crm = new CRMManager(false);
        user = crmData.getUser("SIMON");

    }

}