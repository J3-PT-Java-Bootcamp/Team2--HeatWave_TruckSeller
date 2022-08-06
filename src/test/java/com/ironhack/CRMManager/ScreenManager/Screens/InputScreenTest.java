package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.Exceptions.CRMException;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.CRMManager.ScreenManager.InputReader.OPEN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputScreenTest {

    InputScreen screen;
    @BeforeEach
    void setUp() {
        new CRMManager(false);
        screen=new InputScreen(crmData.getUser("USER"),
                "INPUT TEST",
                new TextObject("TEST"),
                new String[]{"Open"},
                OPEN);
    }

    @Test
    void start_test_ok() throws CRMException {

        System.setIn(new ByteArrayInputStream("PATATA\n".getBytes()));
        screen.start();
        assertEquals("PATATA",screen.getValues().get(0));

    }

    @Test
    void constructScreen_test_ok() {
        var size= screen.textObject.getTotalHeight();
        screen.constructScreen();
        assertTrue(size<screen.textObject.getTotalHeight());
    }

    @Test
    void getValues_test_ok() throws CRMException {
        System.setIn(new ByteArrayInputStream("PATATA\n".getBytes()));
        screen.start();
        assertEquals("PATATA",screen.getValues().get(0));

    }
}