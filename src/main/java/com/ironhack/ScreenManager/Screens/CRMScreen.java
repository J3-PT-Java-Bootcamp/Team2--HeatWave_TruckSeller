package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.ScreenManager.Text.*;

import static com.ironhack.Constants.ColorFactory.*;
import static com.ironhack.Constants.Constants.*;

public abstract class CRMScreen {
    private WindowObject textObject;
    private CRMManager crmManager;

    public CRMScreen(String name){
        this.textObject=new WindowObject(LIMIT_X,LIMIT_Y,1,0)
                .setBgColor(BgColors.WHITE)
                .setFrameColor(BgColors.WHITE)
                .setTxtColor(CColors.BLUE)
                .setTitle(name)
                .setTitleColor(CColors.BRIGHT_BLACK);
    }
    public abstract TextObject constructScreen();
    public abstract void getInput();

    public TextObject toTextObject(){
        return constructScreen();
    }

}
