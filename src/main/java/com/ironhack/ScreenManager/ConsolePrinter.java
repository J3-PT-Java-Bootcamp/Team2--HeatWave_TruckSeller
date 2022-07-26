package com.ironhack.ScreenManager;


import com.ironhack.CRMManager.CRMManager;
import com.ironhack.ScreenManager.Screens.Commands;
import com.ironhack.ScreenManager.Text.TextObject;

import java.util.ArrayList;

import static com.ironhack.Constants.ColorFactory.*;
import static com.ironhack.Constants.Constants.*;

/**
 *
 */
public class ConsolePrinter {
    private final ArrayList<TextObject> printQueue;
    private final CRMManager crm;

    //---------------------------------------------------------------------------   CONSTRUCTOR
    public ConsolePrinter(CRMManager crm) {
        this.crm = crm;
        this.printQueue = new java.util.ArrayList<>();
    }

    //---------------------------------------------------------------------------   PUBLIC METHODS

   /**
     * Shows the Team Logo after calibrating console size
    * @deprecated
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

    /**
     * Shows Square with the screen size to allow User to resize console,
     * waits until user confirm
     */
    public void calibrateScreen() throws Exception {
        sendToQueue(new com.ironhack.ScreenManager.Text.WindowObject(LIMIT_X, LIMIT_Y + 2, 1, 1)
                .setFrameColor(BgColors.BRIGHT_BLACK).setBgcolor(BgColors.CYAN)
                .setTxtColor(CColors.BRIGHT_WHITE)
                .addText(TextStyle.BOLD + "Adjust your console size to fit this box.")
                .addText(TextStyle.BOLD + "Press Enter when done").alignTextCenter().alignTextMiddle()
                .addText(CENTER_CARET));
        startPrint();
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
//                    admin(1000 / txtObj.getPrintSpeed());
                }
                case LINE -> {
                    int counter = 0;
                    while (txtObj.hasText()) {
                        System.out.print(txtObj.poll());
                        waitFor((int)(1000 / txtObj.getPrintSpeed()));
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
                                waitFor((int)(1000 / txtObj.getPrintSpeed()));
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
    public void clearScreen() {
        sendToQueue(new TextObject(BLANK_SPACE,  TextObject.Scroll.NO, LIMIT_X, LIMIT_Y * 2).alignTextTop());
    }
    private TextObject pollNext() {
        return printQueue.remove(0);
    }
    private boolean queueContainsScroll( TextObject.Scroll scroll) {
        for ( TextObject txtObj : printQueue) {
            if (txtObj.getScroll().equals(scroll)) return true;
        }
        return false;
    }
    public void showErrorLine(com.ironhack.Exceptions.ErrorType errorType) {
        var line = new com.ironhack.ScreenManager.Text.DynamicLine(LIMIT_X, 1, 1);
        line.addText(CColors.BRIGHT_RED + errorType.toString() + TextStyle.RESET);
        line.addText(CColors.BRIGHT_GREEN + " TRY AGAIN or enter \"HELP\" " + TextStyle.RESET).alignTextCenter();
        line.addText(CENTER_CARET);
        sendToQueue(line);
        startPrint();
    }
    public void showHintLine(String message, Commands[] commands) {
        var line = new com.ironhack.ScreenManager.Text.DynamicLine(LIMIT_X, 5, 1);
        line.setPrintSpeed(0.5f);
        line.addText(CColors.BRIGHT_GREEN + message + TextStyle.RESET).alignTextCenter();
        var sb= new StringBuilder("Available Commands: ");
        for(Commands comm:commands)sb.append("["+comm.toString()+"] ");
        line.addText(CColors.BRIGHT_GREEN + sb.toString() + TextStyle.RESET).alignTextCenter();
        line.addText(CENTER_CARET);
        sendToQueue(line);
        startPrint();
    }
    //-----------------------------------------------------------------------------------------------------INPUT_METHODS

}
