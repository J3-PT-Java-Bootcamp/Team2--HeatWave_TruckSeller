package com.ironhack.ScreenManager;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.Exceptions.CRMException;
import com.ironhack.CRMManager.ScreenManager.InputReader;
import com.ironhack.CRMManager.ScreenManager.Screens.InputScreen;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

import static com.ironhack.CRMManager.ScreenManager.InputReader.*;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.HELP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InputReaderTest {
    final static CRMManager manager=new CRMManager(false);;
    final static private HashMap<InputReader,InputScreen> screen = new HashMap<>();;

    @BeforeAll
    static void setUp() {
        for(InputReader reader: InputReader.values()){
            screen.put(reader,(new InputScreen(null,
                    reader.name()+" Test",
                    new TextObject().addText("--  Testing data  --"),
                    new String[]{"Data"},
                    reader)));
        }
    }

    @Test
    void mail_test_input_OK() throws CRMException {
        System.setIn(new ByteArrayInputStream("PATATA@HOTMAIL.COM\n".getBytes()));
        assertEquals("PATATA@HOTMAIL.COM",MAIL.getInput(screen.get(MAIL)));
    }
    @Test
    void command_test_inputOK() throws CRMException {


        System.setIn(new ByteArrayInputStream("CONVERT LFFE\n".getBytes()));
        assertEquals("CONVERT LFFE",COMMAND.getInput(screen.get(COMMAND)));

    }

    @Test
    void COMMAND_HELP_test_inputOK() throws CRMException {
        System.setIn(new ByteArrayInputStream("HELP\n".getBytes()));
        assertEquals(HELP.name(), COMMAND.getInput(screen.get(COMMAND)));    }

    @Test
    void open_test_inputOK() throws CRMException {
        System.setIn(new ByteArrayInputStream("Patatas fritas\n".getBytes()));
        assertTrue(OPEN.getInput(screen.get(OPEN)).equalsIgnoreCase("Patatas fritas"));
    }

    @Test
    void integer_test_inputOK() throws CRMException {
        System.setIn(new ByteArrayInputStream("2\n".getBytes()));
        assertEquals("2", INTEGER.getInput(screen.get(INTEGER)));
        System.setIn(new ByteArrayInputStream("58798\n".getBytes()));
        assertEquals("58798", INTEGER.getInput(screen.get(INTEGER)));
    }

    @Test
    void phone_test_inputOK() throws CRMException {
        System.setIn(new ByteArrayInputStream("600100200\n".getBytes()));
        assertEquals("600100200", PHONE.getInput(screen.get(PHONE)));
        System.setIn(new ByteArrayInputStream("93456456\n".getBytes()));
        assertEquals("93456456", PHONE.getInput(screen.get(PHONE)));
        System.setIn(new ByteArrayInputStream("933456456\n".getBytes()));
        assertEquals("933456456", PHONE.getInput(screen.get(PHONE)));
        System.setIn(new ByteArrayInputStream("+3493456456\n".getBytes()));
        assertEquals("+3493456456", PHONE.getInput(screen.get(PHONE)));
        System.setIn(new ByteArrayInputStream("(34)93456456\n".getBytes()));
        assertEquals("(34)93456456",PHONE.getInput(screen.get(PHONE)));
    }
}