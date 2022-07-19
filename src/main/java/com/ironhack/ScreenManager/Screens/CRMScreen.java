package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.Text.*;

import static com.ironhack.Constants.ColorFactory.*;
import static com.ironhack.Constants.Constants.*;
import static com.ironhack.ScreenManager.Screens.Commands.*;

public abstract class CRMScreen {
    private WindowObject textObject;
    ConsolePrinter printer;
    private java.util.ArrayList<Commands> commands;
    private String name;
    private CRMManager crmManager;

    public CRMScreen(com.ironhack.CRMManager.CRMManager manager, com.ironhack.ScreenManager.ConsolePrinter printer, String name){
        this.printer=printer;
        this.crmManager=manager;
        this.commands=new java.util.ArrayList<>();
        this.addCommand(EXIT).addCommand(LOGOUT).addCommand(MENU).addCommand(BACK);
        this.textObject=new WindowObject(LIMIT_X,LIMIT_Y,1,0)
                .setBgColor(BgColors.WHITE)
                .setFrameColor(BgColors.WHITE)
                .setTxtColor(com.ironhack.Constants.ColorFactory.CColors.BLACK)
                .setTitle(name)
                .setTitleColor(CColors.BRIGHT_BLACK);
    }
    TextObject addText(String str){
        return this.textObject.addText(str);
    }
    TextObject addText(TextObject txtObj){
        return this.textObject.addText(txtObj);
    }
    WindowObject getTextObject(){
        this.textObject.alignTextMiddle();
        return this.textObject;
    }
    public String getName() {
        return this.name;
    }
    public abstract TextObject print() throws com.ironhack.Exceptions.CRMException;
    abstract void checkCommandInput();
    public CRMScreen addCommand(Commands command){
        this.commands.add(command);
        return this;
    }
    public int getMaxWidth(){
        return this.textObject.MAX_WIDTH;
    }
    void handleBackExceptions(com.ironhack.Exceptions.CRMException crmException) throws com.ironhack.Exceptions.CRMException {
        this.crmManager.exit=true;
        throw crmException;
        //TODO filter by exception type and go back, logout etc... show confirmation if needed
    }
    public com.ironhack.ScreenManager.ConsolePrinter getPrinter() {
        return printer;
    }

    public java.util.ArrayList<Commands> getCommands() {
        return commands;
    }
}
