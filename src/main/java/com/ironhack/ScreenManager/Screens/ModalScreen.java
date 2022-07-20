package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.Text.TextObject;

import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;
import static com.ironhack.ScreenManager.InputReader.*;
import static com.ironhack.ScreenManager.Screens.Commands.*;

public class ModalScreen extends CRMScreen{


    public ModalScreen(CRMManager manager, ConsolePrinter printer, String name, String message) {
        super(manager, printer, name);
        this.commands= new java.util.ArrayList<>();
        this.addCommand(YES).addCommand(NO);
        this.addText(new TextObject(message,getMaxWidth()/3,textObject.MAX_HEIGHT-6).alignTextMiddle());
        this.addText(BLANK_SPACE).addGroupInColumns(4, textObject.MAX_WIDTH, new TextObject[]{
                new TextObject("-".repeat((getMaxWidth()/4)-(getMaxWidth()%4))),
                new TextObject("[  YES ]",getMaxWidth()/4,1),
                new TextObject("[  NO  ]",getMaxWidth()/4,1),
                new TextObject("-".repeat(getMaxWidth()/4))
        });
    }

    @Override
    public String start()  {
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
