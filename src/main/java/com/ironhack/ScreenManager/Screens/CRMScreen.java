package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.Text.*;

import static com.ironhack.Constants.ColorFactory.*;
import static com.ironhack.Constants.ColorFactory.TextStyle.*;
import static com.ironhack.Constants.Constants.*;
import static com.ironhack.ScreenManager.Screens.Commands.*;

public abstract class CRMScreen {
    WindowObject textObject;
    ConsolePrinter printer;
    java.util.ArrayList<Commands> commands;
    private String name;
    CRMManager crmManager;

    public CRMScreen(com.ironhack.CRMManager.CRMManager manager, com.ironhack.ScreenManager.ConsolePrinter printer, String name){
        this.printer=printer;
        this.crmManager=manager;
        this.commands=new java.util.ArrayList<>();
        this.addCommand(EXIT).addCommand(LOGOUT).addCommand(MENU).addCommand(BACK).addCommand(HELP);
        this.textObject=new WindowObject(LIMIT_X,LIMIT_Y,2,1)
                .setBgColor(BgColors.CYAN)
                .setFrameColor(BgColors.WHITE)
                .setTxtColor(CColors.BLACK)
                .setTitleColor(CColors.BRIGHT_WHITE);
        this.addText(BOLD+UNDERLINE.toString()+name+RESET).alignTextCenter().addText(BLANK_SPACE);
    }
    TextObject addText(String str){
        if(str.contains(RESET.toString())){
            return this.textObject.addText(str+this.textObject.getTextModifications());
        }
        return this.textObject.addText(str);
    }
    private WindowObject generateTitle(){
        return this.textObject.setTitle(APP_NAME+BLANK_SPACE.repeat(LIMIT_X/2)
                +"User: "+(crmManager.currentUser==null?"not logged":crmManager.currentUser.getName()));
    }
    TextObject addText(TextObject txtObj){
        return this.textObject.addText(txtObj);
    }
    WindowObject getTextObject(){
        this.generateTitle().alignTextMiddle().alignTextCenter();
        return this.textObject;
    }
    public String getName() {
        return this.name;
    }
    public abstract String start() throws com.ironhack.Exceptions.CRMException;
    abstract void checkCommandInput();
    public CRMScreen addCommand(Commands command){
        this.commands.add(command);
        return this;
    }
    public int getMaxWidth(){
        return this.textObject.MAX_WIDTH;
    }
    public com.ironhack.ScreenManager.ConsolePrinter getPrinter() {
        return printer;
    }

    public java.util.ArrayList<Commands> getCommands() {
        return commands;
    }
}
