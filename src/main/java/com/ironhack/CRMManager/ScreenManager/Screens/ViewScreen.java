package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.Exceptions.*;
import com.ironhack.CRMManager.ScreenManager.ConsolePrinter;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Sales.Account;
import com.ironhack.Sales.Lead;
import com.ironhack.Sales.Opportunity;
import com.ironhack.Sales.Printable;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.ironhack.CRMManager.ScreenManager.InputReader.COMMAND;
import static com.ironhack.Constants.Constants.LIMIT_Y;

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
            addCommand(Commands.CLOSE).addCommand(Commands.ACCOUNT).addCommand(Commands.CONTACTS);
            optionsNames.add(Commands.CLOSE.display);
            optionsNames.add(Commands.ACCOUNT.display);
            optionsNames.add(Commands.CONTACTS.display);
        } else if (Lead.class.equals(tClass)) {
            addCommand(Commands.CONVERT).addCommand(Commands.DISCARD);
            optionsNames.add(Commands.CONVERT.display);
            optionsNames.add(Commands.DISCARD.display);
        } else if (Account.class.equals(tClass)) {
            addCommand(Commands.VIEW).addCommand(Commands.DISCARD).addCommand(Commands.OPP);
            optionsNames.add(Commands.VIEW.display);
            optionsNames.add(Commands.DISCARD.display);
            optionsNames.add(Commands.OPP.display);
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
            if (this.crmManager.getScreenManager().modal_screen("Confirmation Needed",
                    new TextObject("Do you want to logout?"))) {
                crmManager.setCurrentUser(null);
                return Commands.EXIT.name();
            }
        } catch (ExitException e) {
            //If enter EXIT it prompts user for confirmation as entered data will be lost
            if (this.crmManager.getScreenManager().modal_screen("Confirmation Needed",
                    new TextObject("Do you want to close app?"))) {
                crmManager.setCurrentUser(null);
                crmManager.setExit(true);
                return Commands.EXIT.name();
            }
        } catch (GoBackException e) {
            return Commands.EXIT.name();
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
