package com.ironhack.ScreenManager;

public class MenuOption<T> {
    private String display;
    private T value;//TODO allow multiple values like "Opportunity" "opportunities" "Opp"
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

    public void display(int index) {
        System.out.println("(" + (index + 1) + ") " + getDisplay() + (isAvailable() ? "" : " [DISABLED]"));
    }

    public static <T> MenuOption<T> create(String display, T value) {
        return new MenuOption<T>(display, value);
    }
    public static <T> MenuOption<T> create(String display, T value, boolean available) {
        return new MenuOption<T>(display, value, available);
    }
}
