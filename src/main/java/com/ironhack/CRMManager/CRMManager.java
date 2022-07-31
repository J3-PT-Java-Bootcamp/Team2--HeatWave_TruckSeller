package com.ironhack.CRMManager;

import com.ironhack.CRMManager.Exceptions.CRMException;
import com.ironhack.CRMManager.Exceptions.ExitException;
import com.ironhack.CRMManager.Exceptions.LogoutException;
import com.ironhack.CRMManager.ScreenManager.ConsolePrinter;
import com.ironhack.CRMManager.ScreenManager.ScreenManager;
import com.ironhack.CRMManager.ScreenManager.Screens.InputScreen;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Constants.IndustryType;
import com.ironhack.Constants.Product;
import com.ironhack.FakeLead;
import com.ironhack.Sales.Account;
import com.ironhack.Sales.Contact;
import com.ironhack.Sales.Lead;
import com.ironhack.Sales.Opportunity;

import java.util.ArrayList;

import static com.ironhack.CRMManager.Exceptions.ErrorType.WRONG_PASSWORD;
import static com.ironhack.CRMManager.ScreenManager.InputReader.OPEN;
import static com.ironhack.CRMManager.ScreenManager.InputReader.PASSWORD;

@lombok.Data
public class CRMManager {
    private boolean exit;
    private User currentUser;
    public static final UserOpManager userOpManager= new UserOpManager();
    private static final AdminOpManager adminOpManager = new AdminOpManager();
    public static final ConsolePrinter printer = new ConsolePrinter();
    public static CRMData crmData;
    public static final ScreenManager screenManager = new ScreenManager();

    //-------------------------------------------------------------------------------------------------------CONSTRUCTOR
    public CRMManager() {
        this.exit = false;
        try {
            crmData = crmData.loadData();

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
        crmData = new CRMData();
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
        crmData.addAccount(new Account(IndustryType.MEDICAL,9889,"Oklahoma","India","ACC"));
        crmData.addAccount(new Account(IndustryType.PRODUCE,12450,"Martorell","Chile","COCACOLA"));
        var cont= new Contact("Antonio","93456456","antonio@cocacola.es","COCACOLA");
        crmData.addContact(cont);
        var opp = new Opportunity(Product.BOX,1, cont.getId(),"USER","COCACOLA");
        crmData.addOpportunity(opp);
        crmData.getUser("USER").addToOpportunityList(opp.getId());
        opp = new Opportunity(Product.BOX,1, cont.getId(),"USER","COCACOLA");
        crmData.addOpportunity(opp);
        crmData.getUser("USER").addToOpportunityList(opp.getId());
        opp = new Opportunity(Product.BOX,1, cont.getId(),"USER","COCACOLA");
        crmData.addOpportunity(opp);
        crmData.getUser("USER").addToOpportunityList(opp.getId());
        opp = new Opportunity(Product.BOX,1, cont.getId(),"USER","COCACOLA");
        crmData.addOpportunity(opp);
        crmData.getUser("USER").addToOpportunityList(opp.getId());

        if (testWithScreens) appStart();
    }
    //---------------------------------------------------------------------------------------------------------MAIN FLOW

    /**
     * Method that runs only if there is not any data saved
     */
    private void runFirstConfig() {
        try {
            adminOpManager.createNewUser(null,true);
        } catch (CRMException e) {
            runFirstConfig();
        }
    }

    /**
     * Main app Screens loop
     */
    private void appStart() {
        while (!exit) {
            try {
            var comm = screenManager.menu_screen(currentUser == null ?login_screen() : currentUser);
                if(currentUser.isAdmin()) switch (comm) {
                    case USERS -> adminOpManager.manageUsers_screen(currentUser);
                    case STATS -> adminOpManager.showStats_screen(currentUser);
                    case LOAD -> adminOpManager.loadLeadData(currentUser);
                    default -> {
                        LogWriter.logError(getClass().getSimpleName(),
                                "appStart",
                                "Unexpected command value... " + comm.name());
                    }
                }
                else switch (comm) {
                    case OPP -> screenManager.show_OpportunitiesScreen(currentUser,new ArrayList<>());
                    case LEAD -> screenManager.show_LeadScreen(currentUser);
                    case ACCOUNT -> screenManager.show_AccountsScreen(false, currentUser);
                    case CLOSE -> userOpManager.closeOpportunity(currentUser, comm.getCaughtInput());
                    case CONVERT -> userOpManager.convertLeadToOpp(currentUser, comm.getCaughtInput());
                    case VIEW -> userOpManager.viewObject(currentUser, comm.getCaughtInput());
                    default -> {
                        LogWriter.logError(getClass().getSimpleName(),
                                "appStart",
                                "Unexpected command value... " + comm.name());
                    }
                }
            }catch (ExitException e){
                this.exit=true;
            }catch (LogoutException logout){
                currentUser=null;
            } catch (CRMException ignored){
                LogWriter.logError(getClass().getSimpleName(),
                        "appStart","Unexpected exception.. "+ignored.getClass()+" "+ignored.getErrorType());
            }
        }
        System.exit(0);
    }



    //-------------------------------------------------------------------------------------------------------PRIVATE METHODS


    /**
     * Checks user and password
     *
     * @param userName Username input
     * @param password password input
     * @return true if user and password coincide with saved one
     */
    public boolean checkCredentials(String userName, String password) {
        var user = crmData.getUser(userName);
        if (user != null) {
            return user.getPassword().equalsIgnoreCase(password);
        }
        return false;
    }


    /**
     * Shows login screen
     *
     * @return the user logged
     */
    public User login_screen() {
        String strResult = "";
        var loginScreen = new InputScreen(null, "Login", new TextObject("Enter your User Name and Password:"), new String[]{"User Name", "Password"}, OPEN, PASSWORD);
        try {
            strResult = loginScreen.start();
        } catch (CRMException e) {
            setExit(true);
        }
        var userVal = loginScreen.getValues();
        if (userVal == null || userVal.size() < 2) return null;
        if (checkCredentials(userVal.get(0), userVal.get(1))) {
            setCurrentUser(crmData.getUser(userVal.get(0)));
            screenManager.confirming_screen(currentUser,"Welcome " + userVal.get(0) + "!", strResult, false);
        } else {
            printer.showErrorLine(WRONG_PASSWORD);
            login_screen();
        }
        return getCurrentUser();
    }
}
