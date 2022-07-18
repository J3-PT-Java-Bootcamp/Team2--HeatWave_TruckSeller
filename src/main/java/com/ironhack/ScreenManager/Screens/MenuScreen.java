package com.ironhack.ScreenManager.Screens;

import com.ironhack.ScreenManager.MenuOption;
import com.ironhack.ScreenManager.Text.*;

public class MenuScreen extends CRMScreen{

    private final MenuOption<String> options;


    public MenuScreen(String name,MenuOption<String> options) {
        super(name);
        this.options = options;
    }
    @Override
    public TextObject constructScreen() {
        return null;
    }

    @Override
    public void getInput() {

    }
}
