package com.ironhack.ScreenManager;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Exceptions.CRMException;
import com.ironhack.ScreenManager.Screens.Commands;
import com.ironhack.ScreenManager.Screens.InputScreen;
import com.ironhack.ScreenManager.Text.TextObject;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;

import static com.ironhack.ScreenManager.InputReader.*;
import static com.ironhack.ScreenManager.Screens.Commands.HELP;
import static org.junit.jupiter.api.Assertions.*;

class InputReaderTest {
    CRMManager manager;
    private InputScreen screen;

    @BeforeEach
    void setUp() {
        manager = new CRMManager(true);
        for (InputReader reader : InputReader.values()) {
            screen = new InputScreen(manager,
                    manager.getPrinter(),
                    "Phone Test",
                    new TextObject().addText("--  Testing phone data  --"),
                    new String[]{"Phone1"},
                    InputReader.PHONE);
        }

    }

    @org.junit.jupiter.api.Test
    void mail_test_inputOK() {
        //TODO
    }

    @org.junit.jupiter.api.Test
    void command_test_inputOK() {
        //TODO
    }

    @org.junit.jupiter.api.Test
    void COMMAND_HELP_test_inputOK() throws CRMException {
        System.setIn(new ByteArrayInputStream("HELP\n".getBytes()));
        assertEquals(HELP.name(), COMMAND.getInput(screen, manager.getPrinter()));    }

    @org.junit.jupiter.api.Test
    void open_test_inputOK() throws CRMException {
        System.setIn(new ByteArrayInputStream("Patatas fritas\n".getBytes()));
        assertEquals(true, OPEN.getInput(screen, manager.getPrinter()).equalsIgnoreCase("Patatas fritas"));
    }

    @org.junit.jupiter.api.Test
    void integer_test_inputOK() throws CRMException {
        System.setIn(new ByteArrayInputStream("2\n".getBytes()));
        assertEquals("2", INTEGER.getInput(screen, manager.getPrinter()));
        System.setIn(new ByteArrayInputStream("58798\n".getBytes()));
        assertEquals("58798", INTEGER.getInput(screen, manager.getPrinter()));
    }

    @org.junit.jupiter.api.Test
    void phone_test_inputOK() throws CRMException {

        System.setIn(new ByteArrayInputStream("600100200\n".getBytes()));
        assertEquals("600100200", PHONE.getInput(screen, manager.getPrinter()));
        System.setIn(new ByteArrayInputStream("93456456\n".getBytes()));
        assertEquals("93456456", PHONE.getInput(screen, manager.getPrinter()));
        System.setIn(new ByteArrayInputStream("933456456\n".getBytes()));
        assertEquals("933456456", PHONE.getInput(screen, manager.getPrinter()));
        System.setIn(new ByteArrayInputStream("+3493456456\n".getBytes()));
        assertEquals("+3493456456", PHONE.getInput(screen, manager.getPrinter()));
        System.setIn(new ByteArrayInputStream("(34)93456456\n".getBytes()));
        assertEquals("(34)93456456",PHONE.getInput(screen,manager.getPrinter()));
    }
}