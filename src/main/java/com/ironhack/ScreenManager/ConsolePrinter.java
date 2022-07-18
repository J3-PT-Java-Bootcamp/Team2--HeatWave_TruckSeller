package com.ironhack.ScreenManager;


import com.ironhack.CRMManager.CRMManager;

import static com.ironhack.Constants.Constants.*;
import static com.ironhack.Constants.ColorFactory.*;

/**
 *
 */
public class ConsolePrinter {

    private final java.util.ArrayList<com.ironhack.ScreenManager.Text.TextObject> printQueue;
    private final CRMManager crm;

    //---------------------------------------------------------------------------   CONSTRUCTOR
    public ConsolePrinter(CRMManager crm) {
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

    /**
     * Shows Square with the screen size to allow User to resize console,
     * waits until user confirm
     */
    public void calibrateScreen() throws Exception {
        sendToQueue(new com.ironhack.ScreenManager.Text.WindowObject(LIMIT_X, LIMIT_Y + 2, 1, 1).setBgColor(BgColors.CYAN)
                .setFrameColor(BgColors.BRIGHT_BLACK).setTxtColor(CColors.BRIGHT_WHITE)
                .addText(TextStyle.BOLD + "Adjust your console size to fit this box.")
                .addText(TextStyle.BOLD + "Press Enter when done").alignTextCenter().alignTextMiddle()
                .addText(CENTER_CARET));
        startPrint();

    }


//    @Deprecated
//    public boolean confirmationNeeded(String message) {
//        clearScreen();
//        printQueue.add(new  WindowObject(LIMIT_X, LIMIT_Y, 3, 3)
//                .setBgColor(BgColors.BLACK).setFrameColor(BgColors.WHITE).setTxtColor(CColors.BRIGHT_WHITE)
//                .setTitleColor(CColors.BLACK).setTitle("Confirmation Needed")
//                .addText(message).addGroupAligned(2, LIMIT_X / 2,
//                        new  TextObject[]{
//                                new  TextObject(Modal.CANCEL.ordinal() + "- " + Modal.CANCEL,
//                                         TextObject.Scroll.BLOCK, LIMIT_X / 4, 1),
//                                new  TextObject(Modal.OK.ordinal() + "- " + Modal.OK,
//                                         TextObject.Scroll.BLOCK, LIMIT_X / 4, 1)})
//                .alignTextCenter().alignTextMiddle());
//        startPrint();
//        return Modal.values()[getIntFromInput(Modal.values())] == Modal.OK;
//    }
    //---------------------------------------------------------------------------   CONSOLE MANAGER
    public void startPrint() {
        var sb = new StringBuilder();
        while (!printQueue.isEmpty()) {
            var txtObj = pollNext();
            switch (txtObj.getScroll()) {
                case NO -> {
                    if (queueContainsScroll( com.ironhack.ScreenManager.Text.TextObject.Scroll.NO)) sb.append(txtObj.print()).append(NEW_LINE);
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

    public void sendToQueue( com.ironhack.ScreenManager.Text.TextObject txtObj) {
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
    private void clearScreen() {
        sendToQueue(new com.ironhack.ScreenManager.Text.TextObject(BLANK_SPACE,  com.ironhack.ScreenManager.Text.TextObject.Scroll.NO, LIMIT_X, LIMIT_Y * 2).alignTextTop());
    }

    private com.ironhack.ScreenManager.Text.TextObject pollNext() {
        return printQueue.remove(0);
    }

    private boolean queueContainsScroll( com.ironhack.ScreenManager.Text.TextObject.Scroll scroll) {
        for ( com.ironhack.ScreenManager.Text.TextObject txtObj : printQueue) {
            if (txtObj.getScroll().equals(scroll)) return true;
        }
        return false;
    }

    //-----------------------------------------------------------------------------------------------------INPUT_METHODS

}
