package com.ironhack.ScreenManager.Screens;
import static com.ironhack.Constants.ColorFactory.*;
import static com.ironhack.Constants.ColorFactory.TextStyle.*;

public class MenuOption<T> {

    //TODO reconfigure menuOption the idea is that each MenuOption is related to one Screen  destination(value=screen.name?)
    // it must have also a list of "commands" that will trigger this option

    private String display;
    private T value;

    private boolean available = true;

    public MenuOption(String display, T value) {
        this.display = display;
        this.value = value;
    }
    public MenuOption(String display, T value, boolean available) {
        this.display = display;
        this.value = value;
        this.available = available;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String display() {
       return (isAvailable()? CColors.BLUE+ BOLD.toString()+ UNDERLINE : CColors.BRIGHT_BLACK.toString())
               +getDisplay()+ RESET;
    }

    public static <T> MenuOption<T> create(String display, T value) {
        return new MenuOption<T>(display, value);
    }
    public static <T> MenuOption<T> create(String display, T value, boolean available) {
        return new MenuOption<T>(display, value, available);
    }
}
