package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.Text.*;

public class ModalScreen extends CRMScreen{


    public ModalScreen(String name, ConsolePrinter printer, CRMManager manager) {
        super(manager, printer, name);
    }

    @Override
    public TextObject print() {
        return null;
    }

    @Override
    public void checkCommandInput() {

    }
}
