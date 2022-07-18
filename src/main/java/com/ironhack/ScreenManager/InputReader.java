package com.ironhack.ScreenManager;

import com.ironhack.Constants.ColorFactory.CColors;
import com.ironhack.Constants.ColorFactory.TextStyle;
import com.ironhack.Exceptions.ErrorType;

import java.util.regex.Pattern;

import static com.ironhack.Constants.ColorFactory.BLANK_SPACE_CH;
import static com.ironhack.Constants.Constants.CENTER_CARET;
import static com.ironhack.Constants.Constants.LIMIT_X;
import static com.ironhack.Exceptions.ErrorType.*;

public enum InputReader {
    MAIL,
    PHONE,
    INTEGER,
    OPEN,
    BOOLEAN,
    OPTIONS,
    COMMAND;
    private MenuOption<String>[] options;


    InputReader() {

    }


    //------------------------------------------------------------------------------------------------------------VALIDATORS
    private String validateOpenInput() {
        String input = "";
        try {
            input = waitForInput();
        } catch (Exception e) {
            showErrorLine(FATAL_ERR);
            return validateOpenInput();
        }
        if (input.trim().length() < 3 || !isValidString(input.trim())) {
            showErrorLine(FORMAT_NOK);
            return validateOpenInput();
        }
        return input;
    }

    private int validateIntegerInput(int min, int max) {
        int inputNumber = -1;
        try {
            inputNumber = Integer.parseInt(waitForInput());
        } catch (Exception e) {
            showErrorLine(INTEGER_NOK);
            validateIntegerInput(min, max);
        }
        if (inputNumber <= max && inputNumber >= min) return inputNumber;
        showErrorLine(FORMAT_NOK);
//        startPrint();
        return validateIntegerInput(min, max);
    }

    private String validateMailInput() {
        String input = "";
        try {
            input = waitForInput();
        } catch (Exception e) {
            showErrorLine(FATAL_ERR);
            return validateMailInput();
        }
        //Mail regex provided by the RFC standards
        if (!patternMatches(input.trim(), "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            showErrorLine(MAIL_NOK);
            return validateMailInput();
        }
        return input;
    }

    private String validatePhoneInput() {
        String input = "";
        try {
            input = waitForInput();
        } catch (Exception e) {
            showErrorLine(FATAL_ERR);
            return validatePhoneInput();
        }
        //Mail regex provided by the RFC standards
        if (!patternMatches(input.trim(), "^(\\+\\d{1,3}( )?)?(\\d{3}[- .]?){2}\\d{4}$")) {
            showErrorLine(PHONE_NOK);
            return validatePhoneInput();
        }
        return input;
    }

    private String validateOptionInput(Object[] options) {
        //TODO IMPLEMENTS MENUOPTIONS to choose from various options entering the option name
        // ex: "view leads" "view opportunities" or maybe as an extra
        // allow variations as "LEADS" "opportunities" "OPP" "view opp"
        return null;
    }

    private String validateCommand(Object[] options) {
        //TODO CHECKS COMMANDS IN TABLESCREENS ex: in Opportunities Screen user input
        // ex: "CLOSE won OP012" o "convert L154"
        // Think how to execute determinate function (like convert, map.get("OP012").close(true)),

        return null;
    }

    //---------------------------------------------------------------------------------------------------------------PRIVATE
    private void showErrorLine(ErrorType errorType) {
        var line = new com.ironhack.ScreenManager.Text.DynamicLine(LIMIT_X, 1, 1);
        line.addText(CColors.BRIGHT_RED + errorType.toString() + TextStyle.RESET);
        line.addText(CColors.BRIGHT_GREEN + " TRY AGAIN or enter \"/HELP\" " + TextStyle.RESET).alignTextCenter();
        line.addText(CENTER_CARET);
        //TODO new printer link
//        sendToQueue(line);
//        startPrint();
    }

    private boolean isValidString(String str) {
        str = str.trim();
        if (str.trim().length() == 0) return false;
        var chars = str.toCharArray();
        for (char c : chars)
            if (!(Character.isAlphabetic(c) ||
                    c == BLANK_SPACE_CH || Character.isDigit(c) ||
                    c == '_' || c == '@' || c == '-' || c == '.')) return false;
        return true;

    }


    private boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }

    private String waitForInput() {
        String input;
        var in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
        try {
            input = in.readLine();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        input = input.replace("\n", "").trim();
        return input;
    }

//---------------------------------------------------------------------------------------------------------OUTER METHODS

    String getInput(Object... options) {
        return switch (this) {
            case MAIL -> validateMailInput();
            case PHONE -> validatePhoneInput();
            case INTEGER -> String.valueOf(validateIntegerInput((Integer) options[0], (Integer)options[1]));
            case OPEN -> validateOpenInput();
            case OPTIONS -> validateOptionInput(options);
            case COMMAND -> validateCommand(options);
            default -> throw new RuntimeException("Unexpected value: " + this);
        };
    }
}
