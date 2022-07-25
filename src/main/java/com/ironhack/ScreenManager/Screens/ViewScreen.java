package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Commercial.Printable;
import com.ironhack.Exceptions.*;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.Text.TextObject;

import static com.ironhack.ScreenManager.InputReader.COMMAND;
import static com.ironhack.ScreenManager.Screens.Commands.EXIT;

public class ViewScreen extends CRMScreen{
    //Screen that prints properties of an object
    Printable object;
    public ViewScreen(CRMManager manager, ConsolePrinter printer, String name, Printable object) {
        super(manager, printer, name);
        this.object=object;
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
        textObject.addText(object.printFullObject());

    }
}
