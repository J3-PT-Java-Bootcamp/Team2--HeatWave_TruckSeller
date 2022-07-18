package com.ironhack.ScreenManager.Screens;

import com.ironhack.ScreenManager.Text.*;

import static com.ironhack.Constants.Constants.*;

public abstract class CRMScreen {
    private WindowObject textObject;
    private com.ironhack.CRMManager.CRMManager crmManager;

    public CRMScreen(){
        this.textObject=new WindowObject(LIMIT_X,LIMIT_Y,1,0);
    }
    public abstract TextObject constructScreen();
    public abstract void getInput();

}
