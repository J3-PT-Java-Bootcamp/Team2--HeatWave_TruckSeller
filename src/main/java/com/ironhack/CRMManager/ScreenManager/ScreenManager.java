package com.ironhack.CRMManager.ScreenManager;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.ScreenManager.Screens.*;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.CRMManager.User;
import com.ironhack.Sales.Account;
import com.ironhack.Sales.Lead;
import com.ironhack.Sales.Opportunity;
import com.ironhack.Constants.OpportunityStatus;
import com.ironhack.CRMManager.Exceptions.CRMException;
import lombok.Data;

import java.util.ArrayList;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.CRMManager.ScreenManager.InputReader.*;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.YES;
import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;
import static com.ironhack.CRMManager.Exceptions.ErrorType.*;

@Data
public class ScreenManager {
    private final CRMManager manager;
    private final ConsolePrinter printer;


    /**
     * Creates the newUser screen to create a new user,
     * if there is not previous user it makes admin user by default
     *
     * @param isAdmin true if must create an admin
     * @throws CRMException if Back/Exit/Logout/Menu commands are read
     */
    public void newUser_screen(boolean isAdmin) throws CRMException {
        TextObject txtObj;
        if (crmData.getUserList().isEmpty()) {
            txtObj = new TextObject("Enter a Name and Password to create a New Admin")
                    .addText(BLANK_SPACE).addText(BLANK_SPACE);
        } else {
            txtObj = new TextObject("-- USERS --");
            for (User user : crmData.getUserList().values()) {
                txtObj.addText(user.getName());
            }
            txtObj.addText(BLANK_SPACE)
                    .addText("Enter a new user name and password to create new user.")
                    .addText("Or enter an existing user name and a new Password to change it. ")
                    .addText(BLANK_SPACE).addText(BLANK_SPACE);
        }
        var newUserScreen = new InputScreen(manager,
                printer,
                "New User",
                txtObj,
                new String[]{"User Name", "Password", "Repeat Password"},
                OPEN,
                NEW_PASSWORD,
                NEW_PASSWORD);
        var strRes = newUserScreen.start();
        var userVal = newUserScreen.getValues();

        if (userVal != null && !userVal.isEmpty()) {
            if (crmData.getUserList().containsKey(userVal.get(0))) {
                if (userVal.get(1).equalsIgnoreCase(userVal.get(2))) {
                    crmData.getUserList().get(userVal.get(0)).setPassword(userVal.get(1));
                    try {
                        manager.saveData();
                    } catch (Exception ignored) {
                    }
                    confirming_screen("User " + userVal.get(0) + " password was properly updated.",
                            strRes,
                            true);
                }
            } else {
                if (userVal.size() == 3 && crmData.addToUserList(new User(userVal.get(0), userVal.get(1), isAdmin))) {
                    confirming_screen("User " + userVal.get(0) + " was properly saved.",
                            strRes,
                            true);
                }
            }
        }
    }

    /**
     * Shows a ConfirmationScreen that prints results from last InputScreen
     *
     * @param message  Confirmation text
     * @param strData  Data saved on last InputScreen
     * @param showData true if it must print saved data
     */
    public void confirming_screen(String message, String strData, boolean showData) {
        try {
            if (showData) {
                new ConfirmationScreen(manager, printer, "Confirmation", message, strData).start();
            } else {
                new ConfirmationScreen(manager, printer, "Confirmation", message).start();
            }
        } catch (CRMException ignored) {
            //start() a confirmationScreen won't send any exception
        }
    }

    /**
     * Shows a ModalScreen with YES/NO option
     *
     * @param name    Screen name
     * @param message Message to be printed
     * @return boolean value (true=YES, false=NO)
     */
    public boolean modal_screen(String name, TextObject message) {
        var modal = new ModalScreen(manager, printer, name, message);
        try {
            var val = Commands.valueOf(modal.start().toUpperCase());
            return val.equals(YES);
        } catch (IllegalArgumentException e) {
            printer.showErrorLine(FORMAT_NOK);
            return modal_screen(name, message);
        }
    }

    /**
     * Shows login screen
     *
     * @return the user logged
     */
    public User login_screen() {
        String strResult = "";
        var loginScreen = new InputScreen(manager, printer, "Login", new TextObject("Enter your User Name and Password:"), new String[]{"User Name", "Password"}, OPEN, PASSWORD);
        try {
            strResult = loginScreen.start();
        } catch (CRMException e) {
            manager.setExit(true);
        }
        var userVal = loginScreen.getValues();
        if (userVal == null || userVal.size() < 2) return null;
        if (manager.checkCredentials(userVal.get(0), userVal.get(1))) {
            manager.setCurrentUser(crmData.getUserList().get(userVal.get(0)));
            confirming_screen("Welcome " + userVal.get(0) + "!", strResult, false);
        } else {
            printer.showErrorLine(WRONG_PASSWORD);
            login_screen();
        }
        return manager.getCurrentUser();
    }

    /**
     * Shows different menu for User or Admin
     *
     * @param currentUser User that is logged
     * @return Command selected by user
     */
    public Commands menu_screen(User currentUser) {
        //todo
        MenuScreen main_menu = new MenuScreen(manager, printer, "Main Menu", currentUser);
        try {
            return Commands.valueOf(main_menu.start().split(" ")[0].toUpperCase());//fixme
        } catch (Exception e) {
            printer.showErrorLine(COMMAND_NOK);
            return menu_screen(currentUser);
        }
    }

    public void lead_screen() {
        var list = new java.util.ArrayList<Lead>();
        boolean stop = false;
        Commands comm = null;
        do {
            try {
                for (String id : manager.getCurrentUser().getLeadList()) {
                    list.add(crmData.getLead(id));
                }
                comm = Commands.valueOf(new TableScreen(manager, "Leads", list).start());

                switch (comm) {
                    case MENU, BACK, LOGOUT -> stop = true;
                    case CONVERT -> manager.getUserOpManager().convertLeadToOpp(comm.getCaughtInput());
                    case VIEW -> manager.getUserOpManager().viewObject(comm.getCaughtInput());
                    case DISCARD -> {
                        var lead = crmData.getLead(comm.getCaughtInput()[1]);
                        if (modal_screen("Delete Lead ?", new TextObject("Do yo want to delete manager Lead?").addText(lead.printFullObject()))) {
                            manager.getCurrentUser().removeFromLeadList(lead.getId());
                            crmData.removeLead(lead.getId());
                        }
                    }
                }
            } catch (NullPointerException e) {
                break;
            }
            list.clear();
        } while (!stop);

    }

    public void opportunities_screen() {
        var list = new java.util.ArrayList<Opportunity>();
        boolean stop = false;
        Commands comm = null;
        do {
            try {
                for (String id : manager.getCurrentUser().getOpportunityList()) {
                    var opp = crmData.getOpportunity(id);
                    if (opp.getStatus().equals(OpportunityStatus.OPEN)) list.add(opp);
                }
                comm = Commands.valueOf(new TableScreen(manager, "Opportunities", list).start());

                switch (comm) {
                    case MENU, BACK, LOGOUT -> stop = true;
                    case VIEW -> manager.getUserOpManager().viewObject(comm.getCaughtInput());
                    case DISCARD -> {
                        var opp = crmData.getOpportunity(comm.getCaughtInput()[1]);
                        if (modal_screen("Delete Opportunity ?", new TextObject("Do yo want to delete manager Opportunity?").addText(opp.printFullObject()))) {
                            manager.getCurrentUser().removeFromOpportunities(opp.getId());
                            crmData.removeOpportunity(opp.getId());
                            crmData.removeContact(opp.getDecisionMakerID());
                        }
                    }
                    case CLOSE -> manager.getUserOpManager().closeOpportunity(comm.getCaughtInput());
                }
            } catch (NullPointerException e) {
                break;
            }
            list.clear();
        } while (!stop);
    }

    public String accounts_screen(boolean selectionMode) {
        boolean stop = false;
        Commands res;
        if (selectionMode && crmData.isEmptyMap(Account.class)) return manager.createNewAccount();
        do {
            var accountArr = new ArrayList<Account>();
            if (!crmData.isEmptyMap(Account.class)) {
                accountArr = crmData.getAccountsAsList();
            }
            var account_screen = new TableScreen(manager, selectionMode ? "Select an Account" : "Accounts", accountArr);

            res = Commands.valueOf(account_screen.start());
            switch (res) {
                case EXIT, MENU, LOGOUT, BACK -> stop = true;
                case HELP -> {
                    //fixme HELP CASE must be there?
                }
                case CREATE -> manager.createNewAccount();
                case VIEW -> {
                    if (selectionMode) return res.getCaughtInput()[1];
                    manager.getUserOpManager().viewObject(res.getCaughtInput());
                }
                case DISCARD -> {
                    //FIXME should delete an account with active opps?¿
                    var account = crmData.getAccount(res.getCaughtInput()[1]);
                    if (modal_screen("Delete Account ?", new TextObject("Do yo want to delete manager Lead?").addText(account.printFullObject()))) {
                        crmData.removeAccount(account.getCompanyName());
                    }


                }
            }

        } while (!stop);
        return res.name();
    }





}
