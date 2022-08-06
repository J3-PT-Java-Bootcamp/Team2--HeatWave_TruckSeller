package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.Exceptions.CRMException;
import com.ironhack.CRMManager.Exceptions.GoBackException;
import com.ironhack.CRMManager.Exceptions.GoToMenuException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.VIEW;
import static org.junit.jupiter.api.Assertions.*;

class TableScreenTest {

    TableScreen screen;
    @BeforeEach
    void setUp() {
        new CRMManager(false);
        screen=new TableScreen(crmData.getUser("USER"),
                "TEST",crmData.getLeadsAsList());
    }

    @Test
    void constructScreen_test_ok() {
        var size= screen.textObject.getTotalHeight();
        screen.constructScreen();
        assertTrue(size<screen.textObject.getTotalHeight());
    }

    @Test
    void start_exceptionTest_backOK() {
        System.setIn(new ByteArrayInputStream("BACK\n".getBytes()));
        assertThrows(GoBackException.class,()->screen.start());
    }
    @Test
    void start_exceptionTest_menuOK() {
        System.setIn(new ByteArrayInputStream("menu\n".getBytes()));
        assertThrows(GoToMenuException.class,()->screen.start());
    }
    @Test
    void start_option_ok() throws CRMException {
        System.setIn(new ByteArrayInputStream("view lffe\n".getBytes()));
        assertEquals(VIEW.name(),screen.start());
    }
}