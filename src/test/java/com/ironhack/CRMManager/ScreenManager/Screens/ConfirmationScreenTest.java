package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConfirmationScreenTest {
    CRMManager crm;
    ConfirmationScreen screen;
    @BeforeEach
    void setUp() {
        crm=new CRMManager(false);
        screen = new ConfirmationScreen(crmData.getUser("USER"),
                "TEST",
                "DSFSDFSFD",
                crmData.getLead("LFFE").printFullObject().toString());
    }

    @Test
    void start_test_ok() {
        assertDoesNotThrow(()->screen.start());
    }

    @Test
    void constructScreen_test_ok() {
        var size= screen.textObject.getTotalHeight();
        screen.constructScreen();
        assertTrue(size<=screen.textObject.getTotalHeight());
    }
}