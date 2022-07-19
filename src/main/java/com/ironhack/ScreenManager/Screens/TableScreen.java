package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.Text.*;

public class TableScreen extends CRMScreen{
    public TableScreen(CRMManager manager,
                       ConsolePrinter printer,
                       String name) {
        super(manager,printer,name);
    }

    @Override
    public TextObject print() {
        return null;
    }

    @Override
    public void checkCommandInput() {

    }
}
