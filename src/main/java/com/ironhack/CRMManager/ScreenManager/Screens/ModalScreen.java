package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.Exceptions.CRMException;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.CRMManager.User;

import static com.ironhack.CRMManager.CRMManager.printer;
import static com.ironhack.CRMManager.ScreenManager.InputReader.COMMAND;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.NO;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.YES;
import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;

public class ModalScreen extends CRMScreen{
    private final TextObject message;

    public ModalScreen(User currentUser, String name, TextObject message) {
        super(currentUser, name);
        this.message=message;
        this.commands= new java.util.ArrayList<>();
        this.addCommand(YES).addCommand(NO);
        constructScreen();
    }

    @Override
    public String start()  {
        printer.clearScreen();
        getTextObject().alignTextCenter();
        printer.sendToQueue(this.getTextObject());
        printer.startPrint();
        try {
            return COMMAND.getInput(this,commands.toArray(new Commands[0]));
        }catch (CRMException e){
            return NO.name();
        }
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
