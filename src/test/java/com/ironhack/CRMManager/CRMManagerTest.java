package com.ironhack.CRMManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CRMManagerTest {
    CRMManager crm;
    @BeforeEach
    void setUp() {
        crm=new CRMManager(false);
    }

    @Test
    void checkCredentials_test_ok() {
        assertTrue(crm.checkCredentials("USER","USER"));
    }

    @Test
    void checkCredentials_test_nok() {
        assertFalse(crm.checkCredentials("USER","2345"));
    }
}