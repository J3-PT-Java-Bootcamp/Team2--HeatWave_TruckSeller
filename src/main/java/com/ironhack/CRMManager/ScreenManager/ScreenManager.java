package com.ironhack.CRMManager.ScreenManager;

import com.ironhack.CRMManager.Exceptions.CRMException;
import com.ironhack.CRMManager.Exceptions.ExitException;
import com.ironhack.CRMManager.Exceptions.GoBackException;
import com.ironhack.CRMManager.LogWriter;
import com.ironhack.CRMManager.ScreenManager.Screens.*;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.CRMManager.User;
import com.ironhack.Sales.Account;
import com.ironhack.Sales.Lead;
import com.ironhack.Sales.Opportunity;
import lombok.Data;

import java.util.ArrayList;

import static com.ironhack.CRMManager.CRMManager.*;
import static com.ironhack.CRMManager.Exceptions.ErrorType.COMMAND_NOK;
import static com.ironhack.CRMManager.Exceptions.ErrorType.FORMAT_NOK;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.YES;

@Data
public class ScreenManager {

    /**
     * Shows different menu for User or Admin
     *
     * @param currentUser User that is logged
     * @return Command selected by user
     */
    public Commands menu_screen(User currentUser) throws CRMException {
        //todo
        MenuScreen main_menu = new MenuScreen(currentUser, "Main Menu");
        try {
            return Commands.valueOf(main_menu.start().split(" ")[0].toUpperCase());//fixme
        } catch (CRMException crmE) {
            throw crmE;

        } catch (Exception e) {
            printer.showErrorLine(COMMAND_NOK);
            return menu_screen(currentUser);
        }
    }

    public void show_LeadScreen(User currentUser) throws CRMException {
        var list = new java.util.ArrayList<Lead>();
        boolean stop = false;
        Commands comm = null;
        do {
            try {
                for (String id : currentUser.getLeadList()) {
                    list.add(crmData.getLead(id));
                }
                comm = Commands.valueOf(new TableScreen(currentUser, "Leads", list).start());

                switch (comm) {
                    case MENU, BACK, LOGOUT -> stop = true;
                    case CONVERT -> userOpManager.convertLeadToOpp(currentUser, comm.getCaughtInput());
                    case VIEW -> userOpManager.viewObject(currentUser, comm.getCaughtInput());
                    case DISCARD -> {
                        var lead = crmData.getLead(comm.getCaughtInput()[1]);
                        if (modal_screen(currentUser, "Delete Lead ?", new TextObject("Do yo want to delete manager Lead?").addText(lead.printFullObject()))) {
                            currentUser.removeFromLeadList(lead.getId());
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

    public void show_OpportunitiesScreen(User currentUser,ArrayList<String> opportunitiesId) throws CRMException {
        var list = new java.util.ArrayList<Opportunity>();
        boolean stop = false;
        Commands comm = null;
        do {
            try {
                ArrayList<String> oppList;
                if (opportunitiesId.isEmpty()) oppList = currentUser.getOpportunityList();
                else oppList = opportunitiesId;
                for (String id : oppList ) {
                    var opp = crmData.getOpportunity(id);
                    list.add(opp);
                }
                comm = Commands.valueOf(new TableScreen(currentUser, "Opportunities", list).start());

                switch (comm) {

                    case VIEW -> {
                        userOpManager.viewObject(currentUser, comm.getCaughtInput());
                    }
                    case DISCARD -> {
                        var opp = crmData.getOpportunity(comm.getCaughtInput()[1]);
                        if (modal_screen(currentUser,
                                "Delete Opportunity ?",
                                new TextObject("Do yo want to delete %s Opportunity?".formatted(opp.getId()))
                                        .addText(opp.printFullObject()))) {
                            currentUser.removeFromOpportunities(opp.getId());
                            crmData.removeOpportunity(opp.getId());
                            crmData.removeContact(opp.getDecisionMakerID());
                            screenManager.confirming_screen(currentUser,
                                    "Opportunity %s removed from system".formatted(opp.getId()),
                                    opp.printFullObject().toString(),
                                    true);
                        }
                    }
                    case CLOSE -> userOpManager.closeOpportunity(currentUser, comm.getCaughtInput());
                }
            } catch (NullPointerException e) {
                LogWriter.logError(getClass().getSimpleName(),
                        "show_OpportunitiesScreen",
                        "Received a unexpected NullPointerException.. " + e.getMessage());
                break;
            }
            list.clear();
        } while (!stop);
    }

    public String show_AccountsScreen(boolean selectionMode, User currentUser) throws CRMException {
        boolean stop = false;
        Commands res;
        if (selectionMode && crmData.isEmptyMap(Account.class)) {
            try {
                return userOpManager.createNewAccount(currentUser);
            } catch (GoBackException ignored) {
            }
        }
        do {
            var accountArr = new ArrayList<Account>();
            if (!crmData.isEmptyMap(Account.class)) {
                accountArr = crmData.getAccountsAsList();
            }
            var account_screen = new TableScreen(currentUser, selectionMode ? "Select an Account" : "Accounts", accountArr);
            try{
                res = Commands.valueOf(account_screen.start());
            } catch (GoBackException back){
                return "";
            }catch (CRMException e){throw e;}
            switch (res) {
                case CREATE -> {
                    try {
                        return userOpManager.createNewAccount(currentUser);
                    } catch (GoBackException ignored) {
                    }
                }
                case VIEW -> {
                    if (selectionMode) {
                        if (crmData.existsObject(res.getCaughtInput()[1])) {
                            return res.getCaughtInput()[1];
                        }
                    }
                    try {
                        userOpManager.viewObject(currentUser, res.getCaughtInput());
                    }catch (GoBackException ignored){}
                }
                case DISCARD -> {
                    var account = crmData.getAccount(res.getCaughtInput()[1]);
                    if (modal_screen(currentUser, "Delete Account ?", new TextObject("Do yo want to delete Lead?").addText(account.printFullObject()))) {
                        crmData.removeAccount(account.getCompanyName());
                    }
                }
            }

        } while (!stop);
        throw new ExitException(true);
    }


    /**
     * Shows a ConfirmationScreen that prints results from last InputScreen
     *
     * @param message  Confirmation text
     * @param strData  Data saved on last InputScreen
     * @param showData true if it must print saved data
     */
    public void confirming_screen(User currentUser, String message, String strData, boolean showData) {
        try {
            if (showData) {
                new ConfirmationScreen(currentUser, "Confirmation", message, strData).start();
            } else {
                new ConfirmationScreen(currentUser, "Confirmation", message).start();
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
    public boolean modal_screen(User currentUser, String name, TextObject message) {
        var modal = new ModalScreen(currentUser, name, message);
        try {
            var val = Commands.valueOf(modal.start().toUpperCase());
            return val.equals(YES);
        } catch (IllegalArgumentException e) {
            printer.showErrorLine(FORMAT_NOK);
            return modal_screen(currentUser, name, message);
        }
    }
}
