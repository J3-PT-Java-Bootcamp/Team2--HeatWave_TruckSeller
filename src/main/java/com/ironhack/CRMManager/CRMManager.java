package com.ironhack.CRMManager;

import com.ironhack.CRMManager.ScreenManager.ConsolePrinter;
import com.ironhack.CRMManager.ScreenManager.ScreenManager;
import com.ironhack.CRMManager.ScreenManager.Screens.InputScreen;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Sales.AccountBuilder;
import com.ironhack.Sales.Lead;
import com.ironhack.Constants.IndustryType;
import com.ironhack.CRMManager.Exceptions.CRMException;
import com.ironhack.CRMManager.Exceptions.NoCompleteObjectException;
import com.ironhack.FakeLead;

import java.util.Objects;

import static com.ironhack.CRMManager.ScreenManager.InputReader.*;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.EXIT;
import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;

@lombok.Data
public class CRMManager {
    private boolean exit;
    private User currentUser;
    private final UserOpManager userOpManager;
    private final AdminOpManager adminOpManager;
    private final ConsolePrinter printer;
    public static CRMData crmData;
    private final ScreenManager screenManager;

    //-------------------------------------------------------------------------------------------------------CONSTRUCTOR
    public CRMManager() {
        this.exit = false;
        this.printer = new ConsolePrinter(this);
        userOpManager=new UserOpManager(this,printer);
        adminOpManager=new AdminOpManager(this,printer);
        screenManager=new ScreenManager(this,printer);
        try {
            crmData = loadData();

        } catch (Exception e) {
            crmData = new CRMData();
            runFirstConfig();

        }
        appStart();
    }

    /**
     * Constructor only for tests purposes
     *
     * @param testWithScreens if true it will prompt all screens, else it will run silently
     */
    public CRMManager(Boolean testWithScreens) {
        this.exit = false;
        this.printer = new ConsolePrinter(this);
        crmData = new CRMData();
        userOpManager=new UserOpManager(this,printer);
        adminOpManager=new AdminOpManager(this,printer);
        screenManager=new ScreenManager(this,printer);
        var leadList = FakeLead.getRawLeads(200);

        crmData.addToUserList(new User("ADMIN", "ADMIN", true));
        crmData.addToUserList(new User("USER", "USER", false));
        crmData.addToUserList(new User("PATATA", "111", false));
        crmData.addToUserList(new User("FRITA", "111", false));
        for (TextObject data : leadList) {
            try {
                var lead = new Lead(data.get(1), data.get(0), data.get(2), data.get(3), data.get(4));
                crmData.addLead(lead);
                crmData.getUserList().get("USER").addToLeadList(lead.getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (testWithScreens) appStart();
    }
    //---------------------------------------------------------------------------------------------------------MAIN FLOW

    /**
     * Method that runs only if there is not any data saved
     */
    private void runFirstConfig() {
        //TODO METHOD THAT CREATES A ADMIN USER AND ASK FOR OTHER USERS DATA
        try {
            screenManager.newUser_screen(true);
        } catch (CRMException e) {
            runFirstConfig();
        }
    }

    /**
     * Main app Screens loop
     */
    private void appStart() {
        while (!exit) {//if in any moment we enter EXIT it must turn this.exit to true so while will end
            var comm = screenManager.menu_screen(currentUser == null ? screenManager.login_screen() : currentUser);
            switch (comm) {
                case OPP -> screenManager.opportunities_screen();
                case LEAD -> screenManager.lead_screen();
                case ACCOUNT -> screenManager.accounts_screen(false);
                case USERS -> adminOpManager.manageUsers_screen();
                case STATS -> adminOpManager.showStats_screen();
                case LOAD -> adminOpManager.loadLeadData();
                case CLOSE -> userOpManager.closeOpportunity(comm.getCaughtInput());
                case CONVERT -> userOpManager.convertLeadToOpp(comm.getCaughtInput());
                case VIEW -> userOpManager.viewObject(comm.getCaughtInput());
                default -> {
                }
            }
        }
        System.exit(0);
    }



    //-------------------------------------------------------------------------------------------------------PRIVATE METHODS
    private CRMData loadData() throws Exception {
        //TODO LOAD FULL CRMData object from json and aSsign it to crmData field
        throw new IllegalAccessException();
    }

    public void saveData() throws Exception {
        //TODO Save crmData object to .json file in ./data

    }


    /**
     * Checks user and password
     *
     * @param userName Username input
     * @param password password input
     * @return true if user and password coincide with saved one
     */
    public boolean checkCredentials(String userName, String password) {
        var user = crmData.getUserList().get(userName);
        if (user != null) {
            return user.getPassword().equalsIgnoreCase(password);
        }
        return false;
    }
}
