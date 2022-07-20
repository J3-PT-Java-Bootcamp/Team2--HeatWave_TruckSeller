package com.ironhack.Exceptions;

import com.ironhack.ScreenManager.Screens.Commands;

public class HelpException extends CRMException{
    public final String hint;
    public final com.ironhack.ScreenManager.Screens.Commands[] commands;
    public HelpException(com.ironhack.Exceptions.ErrorType errorType, String hint,Commands...commands) {
        super(errorType);
        this.hint=hint;
        this.commands=commands;
    }

}
