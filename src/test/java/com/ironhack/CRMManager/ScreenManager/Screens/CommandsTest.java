package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.Exceptions.CRMException;
import com.ironhack.CRMManager.Exceptions.ExitException;
import com.ironhack.CRMManager.Exceptions.GoToMenuException;
import com.ironhack.CRMManager.ScreenManager.InputReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.*;
import static org.junit.jupiter.api.Assertions.*;

class CommandsTest {
    CRMManager crm;
    CRMScreen screen;
    @BeforeEach
    void setUp(){
        crm= new CRMManager(false);
        screen=new ViewScreen(crmData.getUser("USER"),"TEST",crmData.getLead("LFFE"));

    }
    @Test
    void check_command_test_EXIT() {
        assertThrows(ExitException.class,()-> EXIT.check("EXIT",screen, InputReader.COMMAND));
    }

    @Test
    void check_command_test_MENU() {
        assertThrows(GoToMenuException.class,()-> MENU.check("MENU",screen, InputReader.COMMAND));
    }

    @Test
    void check_command_test_CONVERT_ok() throws CRMException {
        assertTrue(CONVERT.check("CONVERT LFFE",screen, InputReader.COMMAND));
    }
    @Test
    void check_command_test_CONVERT_currentObject_ok() throws CRMException {
        assertTrue(CONVERT.check("CONVERT",screen, InputReader.COMMAND));
    }
    @Test
    void check_command_test_CONVERT_nok() throws CRMException {
        assertFalse(CONVERT.check("DISCARD LFFE",screen, InputReader.COMMAND));
    }
}