package com.ironhack.ScreenManager;

import com.ironhack.Constants.ColorFactory.CColors;
import com.ironhack.Constants.ColorFactory.TextStyle;
import com.ironhack.Exceptions.CRMException;
import com.ironhack.Exceptions.WrongInputException;
import com.ironhack.ScreenManager.Screens.CRMScreen;
import com.ironhack.ScreenManager.Screens.Commands;
import com.ironhack.ScreenManager.Text.DynamicLine;

import java.util.regex.Pattern;

import static com.ironhack.Constants.ColorFactory.BLANK_SPACE_CH;
import static com.ironhack.Constants.Constants.CENTER_CARET;
import static com.ironhack.Constants.Constants.LIMIT_X;
import static com.ironhack.Exceptions.ErrorType.*;

public enum InputReader {
    MAIL("Expects a mail format ex: user@domain.com"),
    PHONE("Expects phone number format, only numbers and +() signs"),
    INTEGER("Expects an integer value"),
    NEW_PASSWORD("Use a alphanumeric value, you may enter it twice for security reasons"),
    PASSWORD("Use a alphanumeric value, you may enter it twice for security reasons"),
    OPEN("Expects a String value, special characters not allowed"),
    COMMAND("Check the commands box to see available actions");

    private String hint;
    private ConsolePrinter printer;
    public String password;
    InputReader(String hint) {
        this.hint=hint;
    }


    //------------------------------------------------------------------------------------------------------------VALIDATORS
    private String validateOpenInput(CRMScreen screen) throws CRMException {
        String input =validateCommand(screen.getCommands().toArray(new Commands[0]),screen);//check if there is any global command
        if (input.trim().length() < 3 || !isValidString(input.trim())) {
            printer.showErrorLine(FORMAT_NOK);
            return validateOpenInput(screen);
        }
        return input.trim();
    }

    private int validateIntegerInput(int min, int max) {
        int inputNumber = -1;
        try {
            inputNumber = Integer.parseInt(waitForInput());
        } catch (Exception e) {
            printer.showErrorLine(INTEGER_NOK);
            validateIntegerInput(min, max);
        }
        if (inputNumber <= max && inputNumber >= min) return inputNumber;
        printer.showErrorLine(FORMAT_NOK);
//        startPrint();
        return validateIntegerInput(min, max);
    }

    private String validateMailInput(CRMScreen screen) throws CRMException {
        String input = "";
        try {
            input = waitForInput();
        } catch (Exception e) {
            printer.showErrorLine(FATAL_ERR);
            return validateMailInput(screen);
        }

        validateCommand(screen.getCommands().toArray(new Commands[0]),screen);//check if there is any global command
        //Mail regex provided by the RFC standards
        if (!patternMatches(input.trim(), "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            printer.showErrorLine(MAIL_NOK);
            return validateMailInput(screen);
        }
        return input;
    }

    private String validatePhoneInput(CRMScreen screen) throws CRMException {
        String input = "";
        try {
            input = waitForInput();
        } catch (Exception e) {
            printer.showErrorLine(FATAL_ERR);
            return validatePhoneInput(screen);
        }

        validateCommand(screen.getCommands().toArray(new Commands[0]),screen);//check if there is any global command
        //Mail regex provided by the RFC standards
        if (!patternMatches(input.trim(), "^(\\+\\d{1,3}( )?)?(\\d{3}[- .]?){2}\\d{4}$")) {
            printer.showErrorLine(PHONE_NOK);
            return validatePhoneInput(screen);
        }
        return input;
    }

    private String validateCommand(Commands[] commands, CRMScreen screen) throws CRMException {
        String input = "";
        try {
            input = waitForInput().trim().toLowerCase();

        } catch (Exception e) {
            printer.showErrorLine(FATAL_ERR);
            return validateCommand(commands,screen);
        }
        for(Commands comm:commands){
            if (comm.check(input, screen,this)) return comm.name();
        }
        return input;
    }
    private String validatePassword(CRMScreen screen) throws CRMException {
        var input=validateOpenInput(screen);
        if (this.password==null){
            this.password=input;
            return input;
        }
        if(this.password.equalsIgnoreCase(input)) {
            this.password = null;
            return input;
        }
        printer.showErrorLine(PASSWORD_NOK);
        return validatePassword(screen);

    }


    //---------------------------------------------------------------------------------------------------------------PRIVATE


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
        input = input.replace("\n", "").trim().toUpperCase();
//        if(input=="HELP")showHintLine();

        return input;
    }

//    private void showHintLine() {
//        var line = new DynamicLine(LIMIT_X, 1, 1);
//        line.addText(CColors.BRIGHT_GREEN + printHint() + TextStyle.RESET).alignTextCenter();
//        line.addText(CENTER_CARET);
//        //TODO new printer link
////        sendToQueue(line);
////        startPrint();
//    }
//
//    private String printHint() {
//        if (this.equals(COMMAND)) {//todo: method to print all available commands in one line
//            return this.hint;
//        }
//         return this.hint;
//
//    }

//---------------------------------------------------------------------------------------------------------OUTER METHODS

    public String getInput(CRMScreen screen, ConsolePrinter printer, Commands... options) throws CRMException {
        this.printer=printer;
        try {
            return switch (this) {
                case MAIL -> validateMailInput(screen);
                case PHONE -> validatePhoneInput(screen);
                case INTEGER -> String.valueOf(validateIntegerInput(0, Integer.MAX_VALUE));
                case OPEN, PASSWORD -> validateOpenInput(screen);
                case COMMAND -> validateCommand(options, screen);
                case NEW_PASSWORD -> validatePassword(screen);
            };
        }catch (CRMException e){
            if(e.getClass().equals(WrongInputException.class)){
                printer.showErrorLine(e.getErrorType());
                return getInput(screen,printer,options);
            }
            else throw e;
        }
    }

    @Override
    public String toString() {
        return this.name();
    }

    public String getHint() {
        return this.hint;
    }
}
