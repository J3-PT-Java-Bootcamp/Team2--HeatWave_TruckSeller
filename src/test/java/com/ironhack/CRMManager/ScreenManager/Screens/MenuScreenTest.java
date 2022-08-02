package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.Exceptions.CRMException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.CONVERT;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.OPP;
import static org.junit.jupiter.api.Assertions.*;

class MenuScreenTest {
    CRMManager crm;
    MenuScreen screen;
    @BeforeEach
    void setUp() {
        crm=new CRMManager(false);
        screen=new MenuScreen(crmData.getUser("USER"),"TEST MENU");

    }

    @Test
    void start_test_commandOK() throws CRMException {
        System.setIn(new ByteArrayInputStream("CONVERT LFFE\n".getBytes()));
        assertEquals(CONVERT.name(),screen.start());

    }

    @Test
    void start_test_optionOK() throws CRMException {
        System.setIn(new ByteArrayInputStream("oppORTUnities\n".getBytes()));
        assertEquals(OPP.name(),screen.start());

    }
    @Test
    void constructScreen() {
        var size= screen.textObject.getTotalHeight();
        screen.constructScreen();
        assertTrue(size<screen.textObject.getTotalHeight());
    }

    @Test
    void getHintLine() {

        assertNotNull(screen.getHintLine());

    }
}