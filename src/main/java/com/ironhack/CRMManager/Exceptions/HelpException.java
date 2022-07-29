package com.ironhack.CRMManager.Exceptions;

import com.ironhack.CRMManager.ScreenManager.Screens.Commands;

public class HelpException extends CRMException{
    public final String hint;
    public final Commands[] commands;
    public HelpException(ErrorType errorType, String hint, Commands...commands) {
        super(errorType);
        this.hint=hint;
        this.commands=commands;
    }

}
