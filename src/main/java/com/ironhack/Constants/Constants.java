package com.ironhack.Constants;

import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;

public class Constants {
    public static final int FAV_MAX = 8;
    public static final ColorFactory.BgColors MAIN_BG = ColorFactory.BgColors.CYAN;
    public static final ColorFactory.BgColors FRAME_BG = ColorFactory.BgColors.CYAN;
    public static final ColorFactory.CColors MAIN_TXT_COLOR = ColorFactory.CColors.BLACK;
    public static final int LIMIT_X = 120;

    public static final int LIMIT_Y = 25;
    public static final String CENTER_CARET=  BLANK_SPACE.repeat(LIMIT_X / 2);

    public static final String MAX_ID= "FFF";
    public static final String APP_NAME= "TruckSeller v0.1";
    public static final String CLOSE_WITHOUT_SAVE = "You have changes without save, are you sure you want to exit and lose them?";

}
