package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.Exceptions.*;
import com.ironhack.CRMManager.LogWriter;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.CRMManager.User;
import com.ironhack.Constants.ColorFactory;

import java.util.List;

import static com.ironhack.CRMManager.CRMManager.*;
import static com.ironhack.CRMManager.ScreenManager.InputReader.COMMAND;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.*;
import static com.ironhack.CRMManager.ScreenManager.Text.TextObject.Scroll;
import static com.ironhack.Constants.ColorFactory.*;
import static com.ironhack.Constants.ColorFactory.CColors.BRIGHT_BLACK;
import static com.ironhack.Constants.ColorFactory.TextStyle.BOLD;
import static com.ironhack.Constants.ColorFactory.TextStyle.UNDERLINE;
import static com.ironhack.Constants.Constants.MAIN_BG;

public class MenuScreen extends CRMScreen {
    private final Commands[] options;
    private final User user;


    public MenuScreen(User currentUser, String title) {
        super(currentUser, title);
        this.user = currentUser;
        this.options = user.isAdmin() ?
                new Commands[]{USERS, STATS, LOAD} : new Commands[]{LEAD, ACCOUNT, OPP};
        constructScreen();
        for (Commands opt : options) this.addCommand(opt);
        if (!user.isAdmin()) addCommand(VIEW).addCommand(DISCARD).addCommand(CLOSE).addCommand(CONVERT);
    }

    @Override
    public String start() throws CRMException {
        printer.clearScreen();
        printer.sendToQueue(getTextObject());
        printer.startPrint();
        String input = "";
        try {
            return COMMAND.getInput(this,commands.toArray(new Commands[0]));
        } catch (HelpException help) {
            printer.showHintLine(help.hint, help.commands);
        } catch (LogoutException | GoBackException logout) {
            if (screenManager.modal_screen(currentUser,"Confirmation Needed",
                    new TextObject("Do you want to logout?"))) {
                throw logout;
            }
        } catch (ExitException e) {
            if (screenManager.modal_screen(currentUser,"Confirmation Needed",
                    new TextObject("Do you want to close app?"))) {
               throw e;
            }
        } catch (CRMException ignored) {
            LogWriter.logError(getClass().getSimpleName(),
                    "start","Received a unexpected exception.. "+ignored.getErrorType());
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
        TextObject statistics;
        if(user.isAdmin()){
            statistics= new TextObject("App stats: ",width/4,height);
            statistics.addText(crmData.printFullObject());
        }else {
            statistics = new TextObject(user.getName() + " :", width / 4, height);
            statistics.addText(user.printFullObject());
        }
        TextObject optionsNames = new TextObject(BOLD + UNDERLINE.toString() + "Options :" + SMART_RESET, width / 4, height).addText(BLANK_SPACE);
        TextObject historicObjects = new TextObject("History :", width / 4, height);

        for (Commands opt : options) optionsNames.addText("-" + opt.getDisplay() + "-").addText(BLANK_SPACE);
        optionsNames.alignTextMiddle();
        for (String id : user.getRecentObjects())
            historicObjects.addText("- " + id + ": " + crmData.getUnknownObject(id).shortPrint());
        historicObjects.fillAllLines();
        optionsNames.alignTextCenter()
                .setBgcolor(MAIN_BG).setTxtStyle(BOLD);
        statistics.setBgcolor(BgColors.CYAN).alignTextTop(height)
                .setTxtColor(CColors.BRIGHT_WHITE).alignTextMiddle();
        historicObjects.alignTextTop(height)
                .setBgcolor(BgColors.BLACK)
                .setTxtColor(ColorFactory.CColors.BRIGHT_WHITE);
        container.addGroupInColumns(3,
                width,
                new TextObject[]{optionsNames, statistics, historicObjects});
        container.addText(BRIGHT_BLACK+this.getHintLine()+SMART_RESET);
        container.alignTextMiddle();

        this.textObject.addText(container).alignTextTop(height);
    }

    @Override
    protected String getHintLine(){
        return super.getHintLine()+ List.of(options);
    }
}


