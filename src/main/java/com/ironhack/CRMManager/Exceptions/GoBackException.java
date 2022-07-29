package com.ironhack.CRMManager.Exceptions;

import com.ironhack.CRMManager.ScreenManager.Screens.CRMScreen;

public class GoBackException extends CRMException{
    CRMScreen screen;
    public GoBackException(CRMScreen screen){
        super(ErrorType.OK);
        this.screen=screen;
    }
}
