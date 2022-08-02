package com.ironhack.CRMManager.ScreenManager;

import com.ironhack.CRMManager.Exceptions.ErrorType;
import com.ironhack.CRMManager.ScreenManager.Screens.Commands;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsolePrinterTest {
    ConsolePrinter printer;
    TextObject txtObj;
    @BeforeEach
    void setUp() {
        printer=new ConsolePrinter();
        txtObj=new TextObject("TEST",100,10);
    }

    @Test
    void startPrint_test() {
        printer.sendToQueue(txtObj);
        assertDoesNotThrow(()->printer.startPrint());
    }

    @Test
    void sendToQueue() throws NoSuchFieldException, IllegalAccessException {
        printer.sendToQueue(txtObj);
        Field queueField= printer.getClass().getDeclaredField("printQueue");
        queueField.setAccessible(true);
        ArrayList<TextObject> queue= (ArrayList<TextObject>) queueField.get(printer);
        assertTrue(queue.contains(txtObj));
    }

    @Test
    void waitFor() {
        assertDoesNotThrow(()->printer.waitFor(2000));
    }

    @Test
    void clearScreen() {
        assertDoesNotThrow(()->printer.clearScreen());
    }

    @Test
    void showErrorLine() {
        assertDoesNotThrow(()->printer.showErrorLine(ErrorType.ID_NOK));
    }

    @Test
    void showHintLine() {
        assertDoesNotThrow(()->printer.showHintLine("patata",new Commands[]{Commands.OPP,Commands.ACCOUNT}));
    }
}