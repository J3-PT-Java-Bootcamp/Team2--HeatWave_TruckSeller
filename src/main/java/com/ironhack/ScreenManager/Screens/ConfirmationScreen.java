package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Exceptions.CRMException;
import com.ironhack.ScreenManager.ConsolePrinter;
import static com.ironhack.Constants.ColorFactory.*;

/**
 * ConfirmationScreen is a CRMScreen that shows the results from last InputScreen during set time
 */
public class ConfirmationScreen extends CRMScreen{
    private final int DURATION = 2000;
    public ConfirmationScreen(CRMManager manager, ConsolePrinter printer, String name, String message, String strData) {
        super(manager, printer, name);
        this.addText(message);
        this.addText(BLANK_SPACE);
        this.addText(BLANK_SPACE);
        this.addText(strData);
    }
    public ConfirmationScreen(CRMManager manager, ConsolePrinter printer, String name, String message) {
        super(manager, printer, name);
        this.addText(message);
    }

    @Override
    public String start() throws CRMException {
        printer.clearScreen();
        printer.sendToQueue(getTextObject());
        printer.startPrint();
        printer.waitFor(DURATION);
        return null;
    }

    @Override
    public void constructScreen() {

    }
}
