package com.ironhack.CRMManager.ScreenManager;

import com.ironhack.CRMManager.ScreenManager.Screens.CRMScreen;
import com.ironhack.CRMManager.ScreenManager.Screens.Commands;
import com.ironhack.Constants.IndustryType;
import com.ironhack.Constants.Product;
import com.ironhack.CRMManager.Exceptions.CRMException;
import com.ironhack.CRMManager.Exceptions.WrongInputException;

import java.util.Arrays;
import java.util.regex.Pattern;

import static com.ironhack.CRMManager.CRMManager.printer;
import static com.ironhack.Constants.ColorFactory.BLANK_SPACE_CH;
import static com.ironhack.CRMManager.Exceptions.ErrorType.*;

/**
 * Enum with different configurations to read input from user,
 * it validates that user input matches the excepcted format
 */
public enum InputReader {
    MAIL("Expects a mail format ex: user@domain.com"),
    PHONE("Expects phone number format, only numbers and +() signs"),
    INTEGER("Expects an integer value"),
    NEW_PASSWORD("Use a alphanumeric value, you may enter it twice for security reasons"),
    PASSWORD("Use a alphanumeric value."),
    OPEN("Expects a String value(min 2 characters), special characters not allowed"),
    COMMAND("Available Commands: "),
    INDUSTRY_TYPE("Enter a Industry type: "+ Arrays.toString(IndustryType.values())),
    PRODUCT_TYPE("Enter a Product type: "+ Arrays.toString(Product.values()));

    private final String hint;
    public String password;
    public String lastInput;
    InputReader(String hint) {
        this.hint=hint;
    }


    //------------------------------------------------------------------------------------------------------------VALIDATORS
    private String validateIndustryType(CRMScreen screen) throws CRMException {
        IndustryType type;
        try {
            type = IndustryType.checkAllIndustries(validateOpenInput(screen));
        }catch (CRMException e){
            throw e;
        }catch (Exception e){
            printer.showErrorLine(INDUSTRY_NOK);
            return validateIndustryType(screen);
        }
        return type.name();

    }

    private String validateProductType(CRMScreen screen) throws CRMException {
        //TODO WHEN ENUM PRODUCT IS IMPLEMENTED
        Product type;
        try {
            type = Product.checkAllProducts(validateOpenInput(screen));
        }catch (CRMException e){
            throw e;
        }catch (Exception e){
            printer.showErrorLine(PRODUCT_NOK);
            return validateProductType(screen);
        }
        return type.name();

    }
    private String validateOpenInput(CRMScreen screen) throws CRMException {
        String input =validateCommand(screen.getCommands().toArray(new Commands[0]),screen);//check if there is any global command
        String finalInput= input.trim().replace(" ","_");
        if (finalInput.length() < 2 || !isValidString(finalInput)) {
            printer.showErrorLine(FORMAT_NOK);
            return validateOpenInput(screen);
        }
        return finalInput;
    }

    private int validateIntegerInput(int min, int max, CRMScreen screen) throws CRMException {
        int inputNumber = -1;
        try {
            inputNumber = Integer.parseInt(validateCommand(screen.getCommands().toArray(new Commands[0]),screen));
        } catch (CRMException e) {
            throw e;
        } catch (Exception e) {
            printer.showErrorLine(INTEGER_NOK);
            validateIntegerInput(min, max,screen);
        }
        if (inputNumber <= max && inputNumber >= min) return inputNumber;
        printer.showErrorLine(FORMAT_NOK);
        return validateIntegerInput(min, max,screen);
    }

    private String validateMailInput(CRMScreen screen) throws CRMException {
        String input = validateCommand(screen.getCommands().toArray(new Commands[0]),screen);//check if there is any global command
        //Mail regex provided by the RFC standards
        if (!patternMatches(input.trim(), "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            printer.showErrorLine(MAIL_NOK);
            return validateMailInput(screen);
        }
        return input;
    }

    private String validatePhoneInput(CRMScreen screen) throws CRMException {
        var input= validateCommand(screen.getCommands().toArray(new Commands[0]),screen);//check if there is any global command
        //Phone regex International
        if (!patternMatches(input.trim(), "^(\\+\\d{1,3}( )?)?((\\(\\d{1,3}\\))|\\d{1,3})[- .]?\\d{3,4}[- .]?\\d{4}$")) {
            printer.showErrorLine(PHONE_NOK);
            return validatePhoneInput(screen);
        }
        return input;
    }

    private String validateCommand(Commands[] commands, CRMScreen screen) throws CRMException {
        String input = "";
        try {
            input = waitForInput().trim().toUpperCase();

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


    private boolean patternMatches(String input, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(input)
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
        lastInput = input.replace("\n", "").trim().toUpperCase();

        return lastInput;
    }


//---------------------------------------------------------------------------------------------------------OUTER METHODS

    public String getInput(CRMScreen screen, Commands... options) throws CRMException {
        try {
            return switch (this) {
                case MAIL -> validateMailInput(screen);
                case PHONE -> validatePhoneInput(screen);
                case INTEGER -> String.valueOf(validateIntegerInput(0, Integer.MAX_VALUE,screen));
                case OPEN, PASSWORD -> validateOpenInput(screen);
                case COMMAND -> validateCommand(options, screen);
                case NEW_PASSWORD -> validatePassword(screen);
                case INDUSTRY_TYPE -> validateIndustryType(screen);
                case PRODUCT_TYPE -> validateProductType(screen);
            };
        }catch (CRMException e){
            if(e.getClass().equals(WrongInputException.class)){
                printer.showErrorLine(e.getErrorType());
                return getInput(screen,options);
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

    public String formatOutput(String output) {
        return switch (this) {
            case MAIL->output.toLowerCase();
            case NEW_PASSWORD, PASSWORD ->"*".repeat(output.length());
            case OPEN->toCamelCase(output).replace("_", " ");
            case INDUSTRY_TYPE-> IndustryType.valueOf(output).toString();
            case PRODUCT_TYPE-> Product.valueOf(output).toString();
            default -> output;
        };
    }
    public static String toCamelCase(String output){
        var outChArr= output.trim().toLowerCase().toCharArray();
        outChArr[0]=Character.toUpperCase(outChArr[0]);
        for (int i = 0; i < outChArr.length; i++) {
            if(outChArr[i]==' ')outChArr[i+1]=Character.toUpperCase(outChArr[i+1]);
        }
        return String.copyValueOf(outChArr);
    }
}
