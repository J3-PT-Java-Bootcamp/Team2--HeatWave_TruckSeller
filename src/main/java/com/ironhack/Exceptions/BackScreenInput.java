package com.ironhack.Exceptions;

public class BackScreenInput extends CRMException{
    com.ironhack.ScreenManager.Screens.CRMScreen screen;
    public BackScreenInput(com.ironhack.ScreenManager.Screens.CRMScreen screen){
        super(com.ironhack.Exceptions.ErrorType.OK);
        this.screen=screen;
    }
}
