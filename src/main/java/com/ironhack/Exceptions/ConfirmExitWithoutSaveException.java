package com.ironhack.Exceptions;

import com.ironhack.ScreenManager.Screens.CRMScreen;

public class ConfirmExitWithoutSaveException extends CRMException{
    CRMScreen originScreen,destinyScreen;

    ConfirmExitWithoutSaveException(CRMScreen originScreen, CRMScreen destinyScreen){
        super(com.ironhack.Exceptions.ErrorType.OK);
        this.destinyScreen=destinyScreen;
        this.originScreen=originScreen;
    }

    private com.ironhack.ScreenManager.Screens.CRMScreen getDestinyScreen() {
        return destinyScreen;
    }

    private ConfirmExitWithoutSaveException setDestinyScreen(com.ironhack.ScreenManager.Screens.CRMScreen destinyScreen) {
        this.destinyScreen = destinyScreen;
        return this;
    }

    private com.ironhack.ScreenManager.Screens.CRMScreen getOriginScreen() {
        return originScreen;
    }

    private ConfirmExitWithoutSaveException setOriginScreen(com.ironhack.ScreenManager.Screens.CRMScreen originScreen) {
        this.originScreen = originScreen;
        return this;
    }
}
