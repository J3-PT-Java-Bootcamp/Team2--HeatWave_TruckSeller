package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.Exceptions.*;
import com.ironhack.CRMManager.LogWriter;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.CRMManager.User;
import com.ironhack.Sales.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.ironhack.CRMManager.CRMManager.printer;
import static com.ironhack.CRMManager.CRMManager.screenManager;
import static com.ironhack.CRMManager.ScreenManager.InputReader.COMMAND;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.*;
import static com.ironhack.Constants.ColorFactory.SMART_RESET;
import static com.ironhack.Constants.ColorFactory.TextStyle.UNDERLINE;
import static com.ironhack.Constants.Constants.LIMIT_Y;

public class ViewScreen extends CRMScreen{
    //Screen that prints properties of an object
    Printable object;
    Type type;
    ArrayList<String> optionsNames;
    public ViewScreen(User currentUser, String name, Printable object) {
        super(currentUser, name);
        this.object=object;
        this.optionsNames=new ArrayList<>();
        type = object.getClass();
        if (Opportunity.class.equals(type)) {
            addCommand(CLOSE).addCommand(ACCOUNT).addCommand(CONTACTS);
            optionsNames.add("CLOSE WON");
            optionsNames.add("CLOSE LOST");
            optionsNames.add("View "+UNDERLINE+"ACC"+SMART_RESET+textObject.getTextModifications()+"OUNT");
            optionsNames.add(UNDERLINE+"CONT"+SMART_RESET+textObject.getTextModifications()+"ACT");
        } else if (Lead.class.equals(type)) {
            addCommand(CONVERT).addCommand(DISCARD);
            optionsNames.add(CONVERT.getDisplay());
            optionsNames.add(DISCARD.getDisplay());
        } else if (Account.class.equals(type)) {
            addCommand(OPP);
            optionsNames.add("View related "+UNDERLINE+"OPP"+SMART_RESET+textObject.getTextModifications()+"ORTUNITIES");
        } else if (Contact.class.equals(type)) {
            addCommand(ACCOUNT).addCommand(OPP);
            optionsNames.add("View "+UNDERLINE+"ACC"+SMART_RESET+textObject.getTextModifications()+"OUNT");
            optionsNames.add("View "+UNDERLINE+"OPP"+SMART_RESET+textObject.getTextModifications()+"ORTUNITY");

        } else if (User.class.equals(type)) {

        } else {
            throw new IllegalStateException("Unexpected value: " + object.getClass());
        }
        constructScreen();
    }
    @Override
    public String start() throws CRMException {
        boolean stop = false;
        printer.clearScreen();
        printer.sendToQueue(getTextObject());
        printer.startPrint();
        String input = "";
        try {
            return COMMAND.getInput(this, commands.toArray(new Commands[0]));

        } catch (HelpException help) {
            printer.showHintLine(help.hint, help.commands);
        } catch (LogoutException logout) {
            if (screenManager.modal_screen(currentUser, "Confirmation Needed",
                    new TextObject("Do you want to logout?"))) {
                throw logout;
            }
        } catch (ExitException e) {
            //If enter EXIT it prompts user for confirmation as entered data will be lost
            if (screenManager.modal_screen(currentUser, "Confirmation Needed",
                    new TextObject("Do you want to close app?"))) {
                throw e;
            }
        } catch (GoBackException|GoToMenuException e) {
            throw e;
        } catch (Exception ignored) {
            LogWriter.logError(getClass().getSimpleName(),
                    "start", "Received a unexpected exception.. " + ignored.getMessage());
        }
        constructScreen();
        return start();
    }

    @Override
    public void constructScreen() {
        constructTitle(getName());
        textObject.addText(object.printFullObject()).alignTextTop(LIMIT_Y/2);
        if (!optionsNames.isEmpty()){
            TextObject[] arr = new TextObject[optionsNames.size()];
            for (int i = 0; i < optionsNames.size(); i++) arr[i] = new TextObject("[ " + optionsNames.get(i) + " ]");
            textObject.addText(new TextObject(textObject.MAX_WIDTH, 2)
                    .addGroupInColumns(optionsNames.size(), textObject.MAX_WIDTH, arr));
        }

    }
}
