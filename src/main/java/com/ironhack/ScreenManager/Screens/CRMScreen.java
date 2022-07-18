package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.ScreenManager.Text.*;

import static com.ironhack.Constants.ColorFactory.*;
import static com.ironhack.Constants.Constants.*;

public abstract class CRMScreen {
    private WindowObject textObject;
    private String name;
    private java.util.HashMap<String,CRMScreen> linkedScreens;
    private CRMManager crmManager;

    public CRMScreen(String name,CRMScreen... linkedScreens){
        this.linkedScreens =new java.util.HashMap<>();
        for(CRMScreen screen :linkedScreens)this.linkedScreens.put(screen.getName(),screen);
        this.textObject=new WindowObject(LIMIT_X,LIMIT_Y,1,0)
                .setBgColor(BgColors.WHITE)
                .setFrameColor(BgColors.WHITE)
                .setTxtColor(CColors.BLUE)
                .setTitle(name)
                .setTitleColor(CColors.BRIGHT_BLACK);
    }

    public String getName() {
        return this.name;
    }

    public abstract TextObject constructScreen();
    public abstract void getInput();

    public TextObject toTextObject(){
        return constructScreen();
    }

}
