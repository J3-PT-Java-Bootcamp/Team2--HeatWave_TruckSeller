package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.Exceptions.CRMException;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.CRMManager.ScreenManager.Text.WindowObject;
import com.ironhack.CRMManager.User;
import lombok.Getter;
import lombok.Setter;

import static com.ironhack.CRMManager.ScreenManager.InputReader.OPEN;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.*;
import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;
import static com.ironhack.Constants.ColorFactory.CColors;
import static com.ironhack.Constants.ColorFactory.TextStyle.*;
import static com.ironhack.Constants.Constants.*;
@Getter
@Setter
public abstract class CRMScreen {
    WindowObject textObject;
    User currentUser;
    String hintLine;
    java.util.ArrayList<Commands> commands;
    private String name;

    public CRMScreen(User user, String name){
        this.name=name;
        this.currentUser= user;
        this.commands=new java.util.ArrayList<>();
        this.addCommand(EXIT).addCommand(LOGOUT).addCommand(MENU).addCommand(BACK).addCommand(HELP);
        this.textObject=new WindowObject(LIMIT_X,LIMIT_Y,2,1)
                .setFrameColor(FRAME_BG)
                .setTitleColor(CColors.BRIGHT_WHITE);
        this.textObject.setBgcolor(MAIN_BG).setTxtColor(MAIN_TXT_COLOR);
    }

    void constructTitle(String name) {
        this.addText(BLANK_SPACE).addText(BOLD+UNDERLINE.toString()+ "---- "+name.toUpperCase()+" ----" +RESET+textObject.getTextModifications()).alignTextCenter().addText(BLANK_SPACE);
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
    private WindowObject constructHeader(){
        return textObject.setTitle(APP_NAME+BLANK_SPACE.repeat(LIMIT_X/2)
                +"User: "+(currentUser==null?"not logged": OPEN.formatOutput(currentUser.getName())));
    }

    /**Returns the Screen with title and frame set
     * @return the Screen with title and frame set
     */
    WindowObject getTextObject(){
        this.constructHeader().alignTextMiddle().alignTextCenter();
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

    public java.util.ArrayList<Commands> getCommands() {
        return commands;
    }

    protected String getHintLine() {
        if (hintLine == null || hintLine == "") {

            StringBuilder sb = new StringBuilder(String.valueOf(this.commands.toString()));
            return sb.toString();
        }
        return hintLine;
    }
}
