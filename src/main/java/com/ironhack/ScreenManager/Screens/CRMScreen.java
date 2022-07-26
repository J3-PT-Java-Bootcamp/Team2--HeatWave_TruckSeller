package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Exceptions.CRMException;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.Text.*;

import static com.ironhack.CRMManager.CRMManager.MAIN_BG;
import static com.ironhack.CRMManager.CRMManager.MAIN_TXT_COLOR;
import static com.ironhack.Constants.ColorFactory.*;
import static com.ironhack.Constants.ColorFactory.TextStyle.*;
import static com.ironhack.Constants.Constants.*;
import static com.ironhack.ScreenManager.Screens.Commands.*;

public abstract class CRMScreen {
    WindowObject textObject;
    ConsolePrinter printer;
    java.util.ArrayList<Commands> commands;
    private String name;
    public CRMManager crmManager;

    public CRMScreen(CRMManager manager, ConsolePrinter printer, String name){
        this.printer=printer;
        this.name=name;
        this.crmManager=manager;
        this.commands=new java.util.ArrayList<>();
        this.addCommand(EXIT).addCommand(LOGOUT).addCommand(MENU).addCommand(BACK).addCommand(HELP);
        this.textObject=new WindowObject(LIMIT_X,LIMIT_Y,2,1)
                .setFrameColor(BgColors.WHITE)
                .setTitleColor(CColors.BRIGHT_WHITE);
        this.textObject.setBgcolor(MAIN_BG).setTxtColor(MAIN_TXT_COLOR);
    }

    void constructTitle(String name) {
        this.addText(BLANK_SPACE).addText(BOLD+UNDERLINE.toString()+ "---- "+name+" ----" +RESET+textObject.getTextModifications()).alignTextCenter().addText(BLANK_SPACE);
    }

    /**Adds new text to main TextObject
     * @param str text to be added
     *
     * @return the main TextObject to allow chain calls
     */
    TextObject addText(String str){
        if(str.contains(RESET.toString())){
            return this.textObject.addText(str+this.textObject.getTextModifications());
        }
        return this.textObject.addText(str);
    }
    public TextObject addText(TextObject txtObj){
        return this.textObject.addText(txtObj);
    }
    /**Generates the Screen top title
     * @return the main TextObject to allow chain calls
     */
    private WindowObject generateTitle(){
        return this.textObject.setTitle(APP_NAME+BLANK_SPACE.repeat(LIMIT_X/2)
                +"User: "+(crmManager.currentUser==null?"not logged":crmManager.currentUser.getName()));
    }

    /**Returns the Screen with title and frame set
     * @return the Screen with title and frame set
     */
    WindowObject getTextObject(){
        this.generateTitle().alignTextMiddle().alignTextCenter();
        return this.textObject;
    }
    public String getName() {
        return this.name;
    }

    /**Main method to be overrided by all screens, it must print screen and return the user input as String
     * @return String value that could vary for every kind of screen
     *
     * @throws CRMException with all back crmExcpetions not handled
     */
    public abstract String start() throws CRMException;


    public abstract void constructScreen();
    /**Adds a new command to be listened on screen
     * @param command Command enum value to be added
     *
     * @return the screen itself to allow chain calls
     */
    public CRMScreen addCommand(Commands command){
        this.commands.add(command);
        return this;
    }

    public int getMaxWidth(){
        return this.textObject.MAX_WIDTH;
    }
    public ConsolePrinter getPrinter() {
        return printer;
    }

    public java.util.ArrayList<Commands> getCommands() {
        return commands;
    }
}
