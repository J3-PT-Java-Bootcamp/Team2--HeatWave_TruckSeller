package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.ScreenManager.ConsolePrinter;

import static com.ironhack.ScreenManager.InputReader.*;
import static com.ironhack.ScreenManager.Screens.Commands.*;

public class ModalScreen extends CRMScreen{


    public ModalScreen(CRMManager manager, ConsolePrinter printer, String name, String message) {
        super(manager, printer, name);
        this.commands= new java.util.ArrayList<>();
        this.addCommand(YES).addCommand(NO);
        this.addText(message);
    }

    @Override
    public String print()  {
        String input="";
        printer.clearScreen();
        getTextObject().alignTextCenter();
        printer.sendToQueue(this.getTextObject());
        printer.startPrint();
        try {
            input = COMMAND.getInput(this, printer);
        }catch (com.ironhack.Exceptions.CRMException e){
        }
        return input;
    }

    @Override
    public void checkCommandInput() {

    }
}
