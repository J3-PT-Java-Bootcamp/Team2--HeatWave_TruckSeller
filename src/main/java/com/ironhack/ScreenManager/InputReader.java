package com.ironhack.ScreenManager;

import com.ironhack.Exceptions.ErrorType;

import java.lang.reflect.Type;
import java.util.regex.Pattern;

import static com.ironhack.Constants.Constants.CENTER_CARET;
import static com.ironhack.Constants.Constants.LIMIT_X;
import static com.ironhack.ScreenManager.ColorFactory.BLANK_SPACE_CH;

public class InputReader {
    public InputReader(){

    }
//TODO IMPLEMENT ERRORTYPE
    private void showErrorLine() {
        var line = new DynamicLine(LIMIT_X, 1, 1);
        ErrorType errorType = null;//fixme as a parameter
        line.addText(com.ironhack.ScreenManager.ColorFactory.CColors.BRIGHT_RED + errorType.toString() + com.ironhack.ScreenManager.ColorFactory.TextStyle.RESET);
        line.addText(com.ironhack.ScreenManager.ColorFactory.CColors.BRIGHT_GREEN + " TRY AGAIN or enter \"/HELP\" " + com.ironhack.ScreenManager.ColorFactory.TextStyle.RESET).alignTextCenter();
        line.addText(CENTER_CARET);
//        sendToQueue(line);
//        startPrint();
    }

    public String obtainFreeInput() {
        String input = "";
        try {
            input = newInput().readLine();
        } catch (Exception e) {
            showErrorLine();
            return obtainFreeInput();
        }
        if (input.trim().length() < 3 || !isValidString(input.trim())) {
            showErrorLine();
            return obtainFreeInput();
        }
        return input;
    }

    private String waitForInput() {
        String input;
        var in = newInput();
        try {
            input = in.readLine();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        input = input.replace("\n", "").trim();
        return input;
    }

    private int getIntFromInput(Object[] values) {
        int inputNumber = -1;
        try {
            inputNumber = Integer.parseInt(waitForInput());
        } catch (Exception e) {
            showErrorLine();
            getIntFromInput(values);
        }
        if (inputNumber < values.length && inputNumber >= 0) return inputNumber;
        showErrorLine();
//        startPrint();
        return getIntFromInput(values);
    }

    private boolean isValidString(String str) {
        str=str.trim();
        if(str.trim().length()==0)return false;
        var chars = str.toCharArray();
        for (char c : chars) if (!(Character.isAlphabetic(c)||
                c==BLANK_SPACE_CH||Character.isDigit(c)||
                c=='_'||c=='@'||c=='-'||c=='.')) return false;
        return true;

    }

    private java.io.BufferedReader newInput() {
        return new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
    }

    public
    private boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
}
