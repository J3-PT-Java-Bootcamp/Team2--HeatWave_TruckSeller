package com.ironhack.ScreenManager.Screens;

public class ViewScreen<T> extends CRMScreen{
    //Screen that prints properties of an object
    public ViewScreen(com.ironhack.CRMManager.CRMManager manager, com.ironhack.ScreenManager.ConsolePrinter printer, String name) {
        super(manager, printer, name);
    }

    @Override
    public String start() throws com.ironhack.Exceptions.CRMException {
        return null;
    }
}
