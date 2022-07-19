package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.ScreenManager.ConsolePrinter;

public class ConfirmationScreen extends CRMScreen{
    public ConfirmationScreen(CRMManager manager, ConsolePrinter printer, String name,String message) {
        super(manager, printer, name);
        this.addText(message);

    }

    @Override
    public String print() throws com.ironhack.Exceptions.CRMException {
        printer.sendToQueue(getTextObject());
        printer.startPrint();
        printer.waitFor(800);
        return null;
    }

    @Override
    void checkCommandInput() {

    }
}
