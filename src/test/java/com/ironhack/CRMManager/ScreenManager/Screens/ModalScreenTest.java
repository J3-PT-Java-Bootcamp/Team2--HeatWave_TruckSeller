package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModalScreenTest {
    ModalScreen screen;
    @BeforeEach
    void setUp() {
        new CRMManager(false);
        screen=new ModalScreen(crmData.getUser("USER"),"TEST",new TextObject("Testing..."));
    }


    @Test
    void start_CONFIRM_OK() {
        System.setIn(new ByteArrayInputStream("yes\n".getBytes()));
        assertEquals("YES",screen.start());
    }
    @Test
    void start_CANCEL_OK() {
        System.setIn(new ByteArrayInputStream("cancel\n".getBytes()));
        assertEquals("NO",screen.start());
    }

    @Test
    void constructScreen() {
        var size=screen.getTextObject().getTotalHeight();
        screen.constructScreen();
        assertTrue(size<screen.getTextObject().getTotalHeight());
    }
}