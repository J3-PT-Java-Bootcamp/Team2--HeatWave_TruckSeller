package com.ironhack.ScreenManager;


import com.ironhack.Constants.Constants;

import static com.ironhack.Constants.Constants.*;
import static com.ironhack.ScreenManager.ColorFactory.*;

/**
 *
 */
public class ConsolePrinter {

    private final java.util.ArrayList<TextObject> printQueue;
    private final com.ironhack.CRMManager.CRMManager crm;

    //---------------------------------------------------------------------------   CONSTRUCTOR
    public ConsolePrinter( com.ironhack.CRMManager.CRMManager crm) {
        this.crm = crm;
        this.printQueue = new java.util.ArrayList<>();
    }

    //---------------------------------------------------------------------------   PUBLIC METHODS

    /**
     * Shows the Team Logo after calibrating console size
     */
    public void splashScreen() {
        try {
            calibrateScreen();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        clearScreen();
        //TODO GENERATE WELCOME SCREEN
        startPrint();
        waitFor(1000);
    }

    public String askUserName() {
        //TODO Print user login
         startPrint();
        return getNameFromInput();
    }

    /**
     * Shows Square with the screen size to allow User to resize console,
     * waits until user confirm
     */
    public void calibrateScreen() throws Exception {
//      sendToQueue(SCREEN_RECT.setAllTextBackground(BgColors.BRIGHT_WHITE));
        sendToQueue(new WindowObject(LIMIT_X, LIMIT_Y + 2, 1, 1).setBgColor(BgColors.CYAN)
                .setFrameColor(BgColors.BRIGHT_BLACK).setTxtColor(CColors.BRIGHT_WHITE)
                .addText(TextStyle.BOLD + "Adjust your console size to fit this box.")
                .addText(TextStyle.BOLD + "Press Enter when done").alignTextCenter().alignTextMiddle());
//                .addText(CENTER_CARET));
        startPrint();
        var in = newInput();
        in.readLine();

    }

//    public Menu showMenu(boolean showError) {
//        if (showError) {
//            showErrorLine();
//        } else {
//            clearScreen();
//            var numberTextObject = new  TextObject( TextObject.Scroll.NO, LIMIT_X / 2
//                    , LIMIT_Y - (HEADER.getTotalHeight() + 1));
//            var titleTextObject = new  TextObject(HEADER,  TextObject.Scroll.NO, LIMIT_X, HEADER.getTotalHeight() + 1);
//            var nameTextObject = new  TextObject( TextObject.Scroll.NO, LIMIT_X / 3
//                    , LIMIT_Y - (HEADER.getTotalHeight() + 1));
//            titleTextObject.addText("--------------------------//  MENU  \\\\--------------------------")
//                    .addText(EMPTY_LINE).alignTextCenter().colorizeAllText();
//            sendToQueue(titleTextObject);
//            for (int i = 0; i < Menu.values().length; i++) {
//                numberTextObject.addText(BLANK_SPACE.repeat((LIMIT_X / 2) - 10) + i + " -->");
//                nameTextObject.addText(Menu.values()[i].toString());
//            }
//            var finalTxtObj = new  TextObject( TextObject.Scroll.NO, LIMIT_X
//                    , LIMIT_Y - (HEADER.getTotalHeight() + 2)).addGroupAligned(2,
//                    LIMIT_X, new  TextObject[]{numberTextObject.alignTextRight(), nameTextObject.fillAllLines()});
//            sendToQueue(finalTxtObj.addText(EMPTY_LINE).colorizeAllText());
//            sendToQueue(new  TextObject("Enter a number to continue",  TextObject.Scroll.NO, LIMIT_X, 1)
//                    .alignTextCenter().setPrintSpeed(6).addText(CENTER_CARET));
//            startPrint();
//        }
//        int inputNumber;
//        try {
//            inputNumber = Integer.parseInt(getInp());
//        } catch (Exception e) {
//            return showMenu(true);
//        }
//        if (inputNumber < Menu.values().length && inputNumber >= 0) {
//            return Menu.values()[inputNumber];
//        }
//        return showMenu(true);
//    }
//


    
    public boolean confirmationNeeded(String message) {
        clearScreen();
        printQueue.add(new  WindowObject(LIMIT_X, LIMIT_Y, 3, 3)
                .setBgColor(BgColors.BLACK).setFrameColor(BgColors.WHITE).setTxtColor(CColors.BRIGHT_WHITE)
                .setTitleColor(CColors.BLACK).setTitle("Confirmation Needed")
                .addText(message).addGroupAligned(2, LIMIT_X / 2,
                        new  TextObject[]{
                                new  TextObject(Modal.CANCEL.ordinal() + "- " + Modal.CANCEL,
                                         TextObject.Scroll.BLOCK, LIMIT_X / 4, 1),
                                new  TextObject(Modal.OK.ordinal() + "- " + Modal.OK,
                                         TextObject.Scroll.BLOCK, LIMIT_X / 4, 1)})
                .alignTextCenter().alignTextMiddle());
        startPrint();
        return Modal.values()[getIntFromInput(Modal.values())] == Modal.OK;
    }


    public void goodBye(String userName) {
        clearScreen();
        sendToQueue(new  TextObject("Thanks for Playing " + userName + ", Good Bye! ",
                 TextObject.Scroll.TYPEWRITER, LIMIT_X, LIMIT_Y).alignTextCenter().alignTextMiddle().setPrintSpeed(10));
        startPrint();
    }

    public void helloUser(String userName) {
        clearScreen();
        sendToQueue(new  TextObject("Welcome Back " + userName,  TextObject.Scroll.TYPEWRITER, LIMIT_X, LIMIT_Y)
                .setPrintSpeed(10).alignTextCenter().alignTextMiddle());
        startPrint();
        waitFor(500);
    }

    //---------------------------------------------------------------------------   CONSOLE MANAGER
    public void startPrint() {
        var sb = new StringBuilder();
        while (!printQueue.isEmpty()) {
            var txtObj = pollNext();
            switch (txtObj.getScroll()) {
                case NO -> {
                    if (queueContainsScroll( TextObject.Scroll.NO)) sb.append(txtObj.print()).append(NEW_LINE);
                    else System.out.print(sb.append(txtObj.print()));
                }
                case BLOCK -> {
                    System.out.print(txtObj.print());
                    waitFor(1000 / txtObj.getPrintSpeed());
                }
                case LINE -> {
                    int counter = 0;
                    while (txtObj.hasText()) {
                        System.out.print(txtObj.poll());
                        waitFor(1000 / txtObj.getPrintSpeed());
                        counter++;
                    }
                }
                case TYPEWRITER -> {
                    int counter = 0;
                    if (txtObj.hasText()) {
                        do {
                            String line = txtObj.printLine(counter);
                            for (int i = 0; i < line.length(); i++) {
                                var currentChar = line.charAt(i);
                                if (currentChar == COLOR_CHAR) {
                                    int j = i;
                                    i += COLOR_LABEL_CHAR_SIZE - 1;
                                    var format = line.substring(j, i);
                                    System.out.print(format);
                                } else if (isASpecialCharacter(currentChar) || currentChar == BLANK_SPACE_CH) {
                                    int j = i;
                                    while (i < line.length() - 1 && (isASpecialCharacter(line.charAt(i + 1)) || line.charAt(i + 1) == BLANK_SPACE_CH)) {
                                        i++;
                                    }
                                    i++;
                                    if (i < line.length()) System.out.print(line.substring(j, i + 1));
                                } else {
                                    System.out.print(currentChar);
                                }
                                waitFor(1000 / txtObj.getPrintSpeed());
                            }
                            System.out.print(NEW_LINE);
                        } while (txtObj.hasText());
                    }
                }
            }
        }
    }

    public void sendToQueue( TextObject txtObj) {
        this.printQueue.add(txtObj);
    }

    public void sendToQueue( TextObject txtObj, int emptyLinesBfr) {
        for (int i = 0; i < emptyLinesBfr; i++) {
            printQueue.add(new TextObject( com.ironhack.ScreenManager.TextObject.Scroll.BLOCK,LIMIT_X,LIMIT_Y));
        }
        sendToQueue(txtObj);
    }

    /**
     * Shorthand for Thread.sleep(miliseconds)
     *
     * @param milis time to sleep in miliseconds
     */
    public void waitFor(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends new lines to fill screen and clear last output
     */
    private void clearScreen() {
        sendToQueue(new  TextObject(BLANK_SPACE,  TextObject.Scroll.BLOCK, LIMIT_X, LIMIT_Y * 2).alignTextTop());
    }

    private  TextObject pollNext() {
        return printQueue.remove(0);
    }

    private boolean queueContainsScroll( TextObject.Scroll scroll) {
        for ( TextObject txtObj : printQueue) {
            if (txtObj.getScroll().equals(scroll)) return true;
        }
        return false;
    }

    //-----------------------------------------------------------------------------------------------------INPUT_METHODS
    private void showErrorLine() {
        var line = new DynamicLine(LIMIT_X, 1, 1);
        line.addText(CColors.BRIGHT_RED + "ERR_   Input not recognized" + TextStyle.RESET);
        line.addText(CColors.BRIGHT_GREEN + " TRY AGAIN " + TextStyle.RESET).alignTextCenter();
        line.addText(CENTER_CARET);
        sendToQueue(line);
        startPrint();
    }

    public String getNameFromInput() {
        String input = "";
        try {
            input = newInput().readLine();
        } catch (Exception e) {
            showErrorLine();
            return getNameFromInput();
        }
        if (input.trim().length() < 3 || !isValidString(input.trim())) {
            showErrorLine();
            return getNameFromInput();
        }
        clearScreen();
        return input;
    }

    public void welcomeNewUser() {
//        sendToQueue(new  TextObject("Nice to meet you " + crm.getUserName(),  TextObject.Scroll.BLOCK, LIMIT_X, LIMIT_Y)
//                .setPrintSpeed(1).alignTextCenter().alignTextMiddle());
//        waitFor(1000);
    }

    private String getInp() {
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
            inputNumber = Integer.parseInt(getInp());
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
        for (char c : chars) if (!(Character.isAlphabetic(c)||c==BLANK_SPACE_CH)) return false;
        return true;

    }

    private java.io.BufferedReader newInput() {
        return new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
    }


}
