package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.Exceptions.CRMException;
import com.ironhack.CRMManager.Exceptions.GoToMenuException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static org.junit.jupiter.api.Assertions.*;

class ViewScreenTest {
    ViewScreen screen;
    @BeforeEach
    void setUp() {
        new CRMManager(false);
        screen=new ViewScreen(crmData.getUser("USER"),"TEST",crmData.getLead("LFFE"));
    }

    @Test
    void start_command_ok() throws CRMException {
        System.setIn(new ByteArrayInputStream("CONVERT".getBytes()));
        assertEquals("CONVERT", screen.start());

    }


    @Test
    void start_exception_ok() {
        System.setIn(new ByteArrayInputStream("menu\n".getBytes()));
        assertThrows(GoToMenuException.class, ()->screen.start());

    }
    @Test
    void constructScreen_test_ok() {
        int size= screen.getTextObject().getTotalHeight();
        screen.constructScreen();
        assertTrue(size<screen.getTextObject().getTotalHeight());
    }
}