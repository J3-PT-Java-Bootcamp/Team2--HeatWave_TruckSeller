package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.Exceptions.*;
import com.ironhack.CRMManager.LogWriter;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.CRMManager.User;
import com.ironhack.Constants.ColorFactory;
import com.ironhack.Sales.Printable;

import static com.ironhack.CRMManager.CRMManager.*;
import static com.ironhack.CRMManager.ScreenManager.InputReader.COMMAND;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.*;
import static com.ironhack.CRMManager.ScreenManager.Text.TextObject.Scroll;
import static com.ironhack.Constants.ColorFactory.*;
import static com.ironhack.Constants.ColorFactory.CColors.*;
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
                new Commands[]{USERS, STATS, LOAD,README} : new Commands[]{LEAD, ACCOUNT, OPP,README};

        for (Commands opt : options) this.addCommand(opt);
        if (!user.isAdmin()) addCommand(VIEW).addCommand(DISCARD).addCommand(CLOSE).addCommand(CONVERT).addCommand(FAV);
    }

    @Override
    public String start() throws CRMException {
        constructScreen();
        printer.clearScreen();
        printer.sendToQueue(getTextObject());
        printer.startPrint();

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
        } catch (CRMException e) {
            LogWriter.logError(getClass().getSimpleName(),
                    "start","Received a unexpected exception.. "+e.getErrorType());
        }
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
            statistics = new TextObject(BLANK_SPACE, width / 4, height).addText(user.getName() + " :");
            statistics.addText(user.printFullObject());
        }
        TextObject optionsNames = new TextObject(BOLD + UNDERLINE.toString() + "Options :" + SMART_RESET, width / 4, height).addText(BLANK_SPACE);
        TextObject favObjects = new TextObject( width / 4, height).addText(user.isAdmin()?"  Users : ":"  Favourites : ").addText(BLANK_SPACE);

        for (Commands opt : options) optionsNames.addText("-" + opt.getDisplay() + "-").addText(BLANK_SPACE);
        if(!user.isAdmin()&&user.getFavourites()!=null) {
            for (String id : user.getFavourites()) {
                Printable unknownObject = crmData.getUnknownObject(id);
                var className = unknownObject.getClass().getSimpleName();
                if (className.length()>5)className=className.substring(0, className.equalsIgnoreCase("Contact") ?4:3)+".";
                favObjects.addText(CColors.WHITE+className.toUpperCase()+CColors.BLUE+"{"+BRIGHT_CYAN+id+CColors.BRIGHT_WHITE+": "
                        + unknownObject.shortPrint()+CColors.BLUE+"}"+BRIGHT_PURPLE
                        + (user.getFavourites().indexOf(id)==user.getFavourites().size()-1?";":","));
            }
        }
        if (user.isAdmin()){
            for (User user : crmData.getUsers(false)) {
                int success = user.getSuccessfulOpp();
                favObjects.addText("- " + user.shortPrint() + ": " + user.oppObjectiveChecker(success)+ success);
            }

        }
        optionsNames.setBgcolor(MAIN_BG).setTxtStyle(BOLD).alignTextMiddle();
        statistics.setBgcolor(BgColors.CYAN)
                .setTxtColor(CColors.BRIGHT_WHITE).fillAllLines().alignTextMiddle();
        favObjects
                .setBgcolor(BgColors.BLACK)
                .setTxtColor(ColorFactory.CColors.BRIGHT_WHITE).fillAllLines().alignTextTop();
        container.addGroupInColumns(3,
                width,
                new TextObject[]{statistics,optionsNames, favObjects});
        container.addText(BRIGHT_BLACK+this.getHintLine()+SMART_RESET);
        container.alignTextMiddle();

        this.textObject.addText(container).alignTextTop(height);
    }
}


