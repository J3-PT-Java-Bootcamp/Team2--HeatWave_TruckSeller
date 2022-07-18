package com.ironhack.Exceptions;

public class BackScreenInput extends Exception{
    com.ironhack.ScreenManager.Screens.CRMScreen screen;
    BackScreenInput(com.ironhack.ScreenManager.Screens.CRMScreen screen){
        this.screen=screen;
    }
}
