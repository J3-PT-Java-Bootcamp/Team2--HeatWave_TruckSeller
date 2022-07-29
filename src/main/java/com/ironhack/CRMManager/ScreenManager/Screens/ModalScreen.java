package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.Exceptions.CRMException;
import com.ironhack.CRMManager.ScreenManager.ConsolePrinter;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;

import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;
import static com.ironhack.CRMManager.ScreenManager.InputReader.*;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.*;

public class ModalScreen extends CRMScreen{
    TextObject message;

    public ModalScreen(CRMManager manager, ConsolePrinter printer, String name, TextObject message) {
        super(manager, printer, name);
        this.message=message;
        this.commands= new java.util.ArrayList<>();
        this.addCommand(YES).addCommand(NO);
        constructScreen();
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
        }catch (CRMException e){
            return NO.name();
        }
        return input;
    }

    @Override
    public void constructScreen() {
        constructTitle(getName());
        var container = new TextObject(textObject.MAX_WIDTH,textObject.MAX_HEIGHT-4)
                .setTxtColor(textObject.txtColor).setBgcolor(textObject.bgColor);
        for(String msg: message.getText()) {
            container.addText(new TextObject(msg, getMaxWidth() / 3, textObject.MAX_HEIGHT - 6));
        }
        container.alignTextMiddle();
        this.textObject.addText(container);
        this.addText(BLANK_SPACE).addGroupInColumns(4, textObject.MAX_WIDTH, new TextObject[]{
                new TextObject("-".repeat((getMaxWidth()/4)-(getMaxWidth()%4))),
                new TextObject("[  YES ]",getMaxWidth()/4,1),
                new TextObject("[  NO  ]",getMaxWidth()/4,1),
                new TextObject("-".repeat(getMaxWidth()/4))
        });

    }
}
