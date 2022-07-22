package com.ironhack.Exceptions;

public class GoBackException extends CRMException{
    com.ironhack.ScreenManager.Screens.CRMScreen screen;
    public GoBackException(com.ironhack.ScreenManager.Screens.CRMScreen screen){
        super(com.ironhack.Exceptions.ErrorType.OK);
        this.screen=screen;
    }
}
