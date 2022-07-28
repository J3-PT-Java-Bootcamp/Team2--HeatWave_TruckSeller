package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Commercial.*;
import com.ironhack.Exceptions.*;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.Text.TextObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.ironhack.Constants.Constants.LIMIT_Y;
import static com.ironhack.ScreenManager.InputReader.COMMAND;
import static com.ironhack.ScreenManager.Screens.Commands.*;

public class ViewScreen extends CRMScreen{
    //Screen that prints properties of an object
    Printable object;
    Type type;
    ArrayList<String> optionsNames;
    public ViewScreen(CRMManager manager, ConsolePrinter printer, String name, Printable object) {
        super(manager, printer, name);
        this.object=object;
        this.optionsNames=new ArrayList<>();
        var tClass = object.getClass();
        if (Opportunity.class.equals(tClass)) {
            addCommand(CLOSE).addCommand(ACCOUNT).addCommand(CONTACTS);
            optionsNames.add(CLOSE.display);
            optionsNames.add(ACCOUNT.display);
            optionsNames.add(CONTACTS.display);
        } else if (Lead.class.equals(tClass)) {
            addCommand(CONVERT).addCommand(DISCARD);
            optionsNames.add(CONVERT.display);
            optionsNames.add(DISCARD.display);
        } else if (Account.class.equals(tClass)) {
            addCommand(VIEW).addCommand(DISCARD).addCommand(OPP);
            optionsNames.add(VIEW.display);
            optionsNames.add(DISCARD.display);
            optionsNames.add(OPP.display);
        } else {
            throw new IllegalStateException("Unexpected value: " + object.getClass());
        }
        constructScreen();
    }

    @Override
    public String start() throws CRMException {
        printer.clearScreen();
        printer.sendToQueue(getTextObject());
        printer.startPrint();
        String input = "";
        try {
            return COMMAND.getInput(this, printer, commands.toArray(new Commands[0]));
        } catch (HelpException help) {
            printer.showHintLine(help.hint, help.commands);
        } catch (LogoutException logout) {
            if (this.crmManager.showModalScreen("Confirmation Needed",
                    new TextObject("Do you want to logout?"))) {
                crmManager.currentUser = null;
                return EXIT.name();
            }
        } catch (ExitException e) {
            //If enter EXIT it prompts user for confirmation as entered data will be lost
            if (this.crmManager.showModalScreen("Confirmation Needed",
                    new TextObject("Do you want to close app?"))) {
                crmManager.currentUser = null;
                crmManager.exit = true;
                return EXIT.name();
            }
        } catch (GoBackException e) {
            return EXIT.name();
        } catch (CRMException ignored) {
        }
        constructScreen();
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
