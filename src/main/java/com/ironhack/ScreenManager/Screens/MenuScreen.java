package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.User;
import com.ironhack.Constants.ColorFactory;
import com.ironhack.Constants.ColorFactory.CColors;
import com.ironhack.Exceptions.*;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.Text.TextObject;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.Constants.ColorFactory.*;
import static com.ironhack.Constants.ColorFactory.TextStyle.*;
import static com.ironhack.ScreenManager.InputReader.COMMAND;
import static com.ironhack.ScreenManager.Screens.Commands.*;
import static com.ironhack.ScreenManager.Text.TextObject.Scroll;

public class MenuScreen extends CRMScreen {

    private final Commands[] options;
    private TextObject optionsNames;
    private TextObject historicObjects;
    private TextObject statistics;
    private final User user;


    public MenuScreen(CRMManager manager, ConsolePrinter printer, String title, User user) {
        super(manager, printer, title);
        this.user = user;
        this.options = user.isAdmin() ?
                new Commands[]{USERS, STATS, LOAD} : new Commands[]{LEAD, ACCOUNT, OPP};
        constructScreen();
        for (Commands opt : options) this.addCommand(opt);
        if (!user.isAdmin()) addCommand(VIEW).addCommand(DISCARD).addCommand(CLOSE).addCommand(CONVERT);
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
            if (this.crmManager.showModalScreen("Confirmation Needed",
                    new TextObject("Do you want to logout?"))) {
                crmManager.currentUser = null;
                return LOGOUT.name();
            }
        } catch (ExitException e) {
            //If enter EXIT it prompts user for confirmation as entered data will be lost
            if (this.crmManager.showModalScreen("Confirmation Needed",
                    new TextObject("Do you want to close app?"))) {
                crmManager.exit = true;
                return EXIT.name();
            }
        } catch (GoBackException e) {
            if (this.crmManager.showModalScreen("Confirmation Needed",
                    new TextObject("Do you want to logout?"))) {
                crmManager.currentUser = null;
                return LOGOUT.name();
            }
        } catch (CRMException ignored) {
        }
        constructScreen();
        return start();
    }

    @Override
    public void constructScreen() {
        this.constructTitle(getName());
        var width = textObject.MAX_WIDTH;
        var height = textObject.getMAX_HEIGHT() - 4;
        var container = new TextObject(Scroll.NO, width, height);
        container.setBgcolor(this.textObject.bgColor);
        container.setTxtColor(textObject.txtColor);
        container.setTxtStyle(textObject.txtStyle);
        statistics = new TextObject(user.getName() + " :", width / 4, height);
        optionsNames = new TextObject(BOLD + UNDERLINE.toString() + "Options :" + SMART_RESET, width / 4, height).addText(BLANK_SPACE);
        historicObjects = new TextObject("History :", width / 4, height);
        statistics.addText(user.printFullObject());
        for (Commands opt : options) optionsNames.addText("-" + opt.toString() + "-").addText(BLANK_SPACE);
        optionsNames.alignTextMiddle();
        for (String id : user.getRecentObjects())
            historicObjects.addText("- " + id + ": " + crmData.getUnknownObject(id).shortPrint());
        historicObjects.fillAllLines();
        optionsNames.alignTextCenter()
                .setBgcolor(BgColors.CYAN).setTxtStyle(BOLD);
        statistics.setBgcolor(BgColors.WHITE).alignTextTop(height)
                .setTxtColor(CColors.BRIGHT_WHITE).alignTextMiddle();
        historicObjects.alignTextTop(height)
                .setBgcolor(BgColors.BRIGHT_BLACK)
                .setTxtColor(ColorFactory.CColors.BRIGHT_WHITE);
        container.addGroupInColumns(3,
                width,
                new TextObject[]{optionsNames,statistics, historicObjects});
        container.alignTextMiddle();

        this.textObject.addText(container).alignTextTop(height);

    }
}


