package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.User;

import static com.ironhack.CRMManager.CRMManager.printer;
import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;

/**
 * ConfirmationScreen is a CRMScreen that shows the results from last InputScreen during set time
 */
public class ConfirmationScreen extends CRMScreen{
    public ConfirmationScreen(User currentUser, String name, String message, String strData) {
        super(currentUser, name);
        this.addText(message);
        this.addText(BLANK_SPACE);
        this.addText(BLANK_SPACE);
        this.addText(strData);
    }
    public ConfirmationScreen(User currentUser, String name, String message) {
        super(currentUser, name);
        this.addText(message);
    }

    @Override
    public String start() {
        printer.clearScreen();
        printer.sendToQueue(getTextObject());
        printer.startPrint();
        int DURATION = 2000;
        printer.waitFor(DURATION);
        return null;
    }

    @Override
    public void constructScreen() {

    }
}
