package com.ironhack.ScreenManager;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.Exceptions.CRMException;
import com.ironhack.CRMManager.ScreenManager.InputReader;
import com.ironhack.CRMManager.ScreenManager.Screens.InputScreen;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static com.ironhack.CRMManager.ScreenManager.InputReader.*;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.HELP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputReaderTest {
    CRMManager manager;
    private InputScreen screen;

    @BeforeEach
    void setUp() {

        manager = new CRMManager(false);

        screen = new InputScreen(null,
                "Phone Test",
                new TextObject().addText("--  Testing phone data  --"),
                new String[]{"Phone1"},
                InputReader.PHONE);
    }

    @Test
    void mail_test_inputOK() {
        //TODO
    }

    @Test
    void command_test_inputOK() throws CRMException {

        System.setIn(new ByteArrayInputStream("CONVERT LFFE\n".getBytes()));
        assertEquals("CONVERT",screen.start());

    }

    @Test
    void COMMAND_HELP_test_inputOK() throws CRMException {
        System.setIn(new ByteArrayInputStream("HELP\n".getBytes()));
        assertEquals(HELP.name(), COMMAND.getInput(screen));    }

    @Test
    void open_test_inputOK() throws CRMException {
        System.setIn(new ByteArrayInputStream("Patatas fritas\n".getBytes()));
        assertTrue(OPEN.getInput(screen).equalsIgnoreCase("Patatas fritas"));
    }

    @Test
    void integer_test_inputOK() throws CRMException {
        System.setIn(new ByteArrayInputStream("2\n".getBytes()));
        assertEquals("2", INTEGER.getInput(screen));
        System.setIn(new ByteArrayInputStream("58798\n".getBytes()));
        assertEquals("58798", INTEGER.getInput(screen));
    }

    @Test
    void phone_test_inputOK() throws CRMException {
//        screen = new InputScreen(null,
//                "Phone Test",
//                new TextObject().addText("--  Testing phone data  --"),
//                new String[]{"Phone1"},
//                InputReader.PHONE);
        System.setIn(new ByteArrayInputStream("600100200\n".getBytes()));
        assertEquals("600100200", PHONE.getInput(screen));
        System.setIn(new ByteArrayInputStream("93456456\n".getBytes()));
        assertEquals("93456456", PHONE.getInput(screen));
        System.setIn(new ByteArrayInputStream("933456456\n".getBytes()));
        assertEquals("933456456", PHONE.getInput(screen));
        System.setIn(new ByteArrayInputStream("+3493456456\n".getBytes()));
        assertEquals("+3493456456", PHONE.getInput(screen));
        System.setIn(new ByteArrayInputStream("(34)93456456\n".getBytes()));
        assertEquals("(34)93456456",PHONE.getInput(screen));
    }
}