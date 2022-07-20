package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.ScreenManager.ConsolePrinter;

public class TableScreen extends CRMScreen{
    public TableScreen(CRMManager manager,
                       ConsolePrinter printer,
                       String name) {
        super(manager,printer,name);
    }

    @Override
    public String start() {
        return null;
    }

    @Override
    public void checkCommandInput() {

    }
}
