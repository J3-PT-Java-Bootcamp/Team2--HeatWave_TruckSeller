package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Exceptions.CRMException;
import com.ironhack.Exceptions.ExitException;
import com.ironhack.Exceptions.GoBackException;
import com.ironhack.Exceptions.HelpException;
import com.ironhack.Exceptions.LogoutException;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.Text.TextObject;

import static com.ironhack.Constants.Constants.LIMIT_X;
import static com.ironhack.Constants.Constants.LIMIT_Y;
import static com.ironhack.ScreenManager.InputReader.COMMAND;
import static com.ironhack.ScreenManager.Screens.Commands.EXIT;
import static com.ironhack.ScreenManager.Screens.Commands.LOGOUT;
import static com.ironhack.ScreenManager.Text.TextObject.Scroll.NO;

public class MenuScreen extends CRMScreen {

    private final Commands[] options;
    private final TextObject optionsNames, globalCommands;


    public MenuScreen(CRMManager manager, ConsolePrinter printer, String name, Commands... options) {
        super(manager, printer, name);
        this.options = options;
        optionsNames = new TextObject(NO, LIMIT_X / 3, LIMIT_Y / 2);
        globalCommands = new TextObject("GLOBAL COMMANDS", NO, LIMIT_X / 3, LIMIT_Y / 2);
//        var names= new java.util.ArrayList<TextObject>();
        for (Commands opt : options) optionsNames.addText(opt.toString());
        for (Commands comm : this.commands) globalCommands.addText(comm.toString());
        this.textObject.addGroupInColumns(2, this.getMaxWidth(), new TextObject[]{optionsNames.alignTextCenter(), globalCommands.alignTextCenter()});

        for (Commands opt : options) this.addCommand(opt);
    }

    @Override
    public String start() {
        printer.clearScreen();
        printer.sendToQueue(getTextObject());
        printer.startPrint();
        String input = "";
        try {
            return COMMAND.getInput(this, printer, commands.toArray(new Commands[0]));
        } catch (HelpException help) {
            printer.showHintLine(help.hint, help.commands);
        } catch (LogoutException logout) {
            crmManager.currentUser = null;
            return LOGOUT.name();
        } catch (ExitException e) {
            //If enter EXIT it prompts user for confirmation as entered data will be lost
            if (this.crmManager.showModalScreen("Confirmation Needed", "Do you want to close app?")) {
                crmManager.currentUser = null;
                crmManager.exit = true;
                return EXIT.name();
            }
        } catch (GoBackException e) {
            if (this.crmManager.showModalScreen("Confirmation Needed", "Do you want to logout?")) {
                crmManager.currentUser = null;
                return LOGOUT.name();
            }
        } catch (CRMException ignored) {
        }
        constructContent();
        return start();
    }

    private void constructContent() {
        this.constructTitle(getName());
        this.textObject.addGroupInColumns(2, this.getMaxWidth(), new TextObject[]{optionsNames.alignTextCenter(), globalCommands.alignTextCenter()});

    }
}
