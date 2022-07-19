package com.ironhack.ScreenManager.Screens;

import com.ironhack.ScreenManager.ConsolePrinter;

public class MenuScreen extends CRMScreen{

    private final Commands[] options;


    public MenuScreen(com.ironhack.CRMManager.CRMManager manager,
                      ConsolePrinter printer,
                      String name, Commands... options) {
        super(manager,printer,name);
        this.options = options;
    }
    @Override
    public String print() {
        return null;
    }

    @Override
    public void checkCommandInput() {

    }
}
