package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.Exceptions.*;
import com.ironhack.CRMManager.LogWriter;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.CRMManager.User;
import com.ironhack.Sales.Account;
import com.ironhack.Sales.Lead;
import com.ironhack.Sales.Opportunity;
import com.ironhack.Sales.Printable;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.ironhack.CRMManager.CRMManager.*;
import static com.ironhack.CRMManager.ScreenManager.InputReader.COMMAND;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.*;
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
            optionsNames.add(CLOSE.display);
            optionsNames.add(ACCOUNT.display);
            optionsNames.add(CONTACTS.display);
        } else if (Lead.class.equals(type)) {
            addCommand(CONVERT).addCommand(DISCARD);
            optionsNames.add(CONVERT.display);
            optionsNames.add(DISCARD.display);
        } else if (Account.class.equals(type)) {
            addCommand(VIEW).addCommand(OPP);
            optionsNames.add(VIEW.display);
            optionsNames.add(OPP.display);
        } else {
            throw new IllegalStateException("Unexpected value: " + object.getClass());
        }
        constructScreen();
    }

    @Override
    public String start() throws CRMException {
        boolean stop = false;
        do {
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
            } catch (GoBackException e) {
                return EXIT.name();
            } catch (Exception ignored) {
                LogWriter.logError(getClass().getSimpleName(),
                        "start", "Received a unexpected exception.. " + ignored.getMessage());
            }
            constructScreen();
        }while (!stop);
        return start();
    }

    @Override
    public void constructScreen() {
        constructTitle(getName());
        textObject.addText(object.printFullObject()).alignTextTop(LIMIT_Y/2);
        TextObject[] arr= new TextObject[optionsNames.size()];
        for (int i = 0; i < optionsNames.size(); i++) arr[i] = new TextObject("[ "+optionsNames.get(i)+" ]");
        textObject.addText(new TextObject(textObject.MAX_WIDTH, 2)
                .addGroupInColumns(optionsNames.size(), textObject.MAX_WIDTH,arr));

    }
}
