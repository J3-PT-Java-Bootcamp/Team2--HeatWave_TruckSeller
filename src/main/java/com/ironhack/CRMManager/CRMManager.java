package com.ironhack.CRMManager;

import com.ironhack.Commercial.Account;
import com.ironhack.Commercial.Lead;
import com.ironhack.Commercial.Opportunity;
import com.ironhack.Exceptions.CRMException;
import com.ironhack.Exceptions.ExitException;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.Screens.*;
import com.ironhack.ScreenManager.Text.TextObject;

import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;
import static com.ironhack.Exceptions.ErrorType.*;
import static com.ironhack.ScreenManager.InputReader.*;
import static com.ironhack.ScreenManager.Screens.Commands.*;
@lombok.Data
public class CRMManager {
    public boolean exit;
    public User currentUser;
    private final ConsolePrinter printer;
    public static CRMData crmData;

    //-------------------------------------------------------------------------------------------------------CONSTRUCTOR
    public CRMManager() {
        this.exit = false;
        this.printer = new ConsolePrinter(this);

        try {
            crmData = loadData();
        } catch (Exception e) {
            crmData = new CRMData();
            runFirstConfig();
        }
        appStart();
    }
    //---------------------------------------------------------------------------------------------------------MAIN FLOW

    /**
     * Method that runs only if there is not any data saved
     */
    private void runFirstConfig() {
        //TODO METHOD THAT CREATES A ADMIN USER AND ASK FOR OTHER USERS DATA
        try {
            newUser_screen(true);
        } catch (CRMException e) {
            runFirstConfig();
        }
    }

    /**
     * Main app Screens loop
     */
    public void appStart() {
        while (!exit) {//if in any moment we enter EXIT it must turn this.exit to true so while will end
            var comm = menu_screen(currentUser == null ? login_screen() : currentUser);
            switch (comm) {
                case OPP -> {
                    opp_screen();
                    //TODO
                }
                case LEAD -> {
                    lead_screen();
                    //convert--> inputScreen--> account screen
                    //discard--> same lead screen
                }
                case ACCOUNT -> {
                    account_screen();
                    //todo
                }
                case CLOSE -> closeOpportunity(comm.getCaughtInput());
                case CONVERT -> convertLeadToOpp(comm.getCaughtInput());
                case VIEW -> viewObject(comm.getCaughtInput());
                default -> {

                }
            }
        }
        //TODO beforeClose();
        System.exit(0);
    }

    //---------------------------------------------------------------------------------------------------COMMAND ACTIONS
    public void closeOpportunity(String[] caughtInput) {
    }
/*product - an Enum with options HYBRID, FLATBED, or BOX
quantity - the number of trucks being considered for purchase
decisionMaker - a Contact
status*/
    public void convertLeadToOpp(String[] caughtInput) {
        try {
            new com.ironhack.ScreenManager.Screens.InputScreen(this,printer,"New Opportunity",new com.ironhack.ScreenManager.Text.TextObject("Enter the information offered to customer:"),
                    new String[]{"Product","Quantity","Decision Maker","Account"},
                    OPEN,OPEN,OPEN,OPEN).start();
        } catch (com.ironhack.Exceptions.CRMException e) {
            throw new RuntimeException(e);
        }
    }

    public void viewObject(String[] caughtInput) {

    }

    //-----------------------------------------------------------------------------------------------------------SCREENS

    /**
     * Creates the newUser screen to create a new user,
     * if there is not previous user it makes admin user by default
     *
     * @param isAdmin true if must create an admin
     *
     * @throws CRMException if Back/Exit/Logout/Menu commands are read
     */
    private void newUser_screen(boolean isAdmin) throws CRMException {
        var newUserScreen = new InputScreen(this, printer, "New User", new TextObject("Enter a Name and Password to create a New User:").addText(BLANK_SPACE).addText(BLANK_SPACE), new String[]{"User Name", "Password", "Repeat Password"}, OPEN, NEW_PASSWORD, NEW_PASSWORD);
        var strRes = newUserScreen.start();
        var userVal = newUserScreen.getValues();
        if (userVal.size() == 3 && crmData.addToUserList(new User(userVal.get(0), userVal.get(1), isAdmin))) {
            showConfirmingScreen("User " + userVal.get(0) + " was properly saved.", strRes, true);
        }
    }

    /**
     * Shows a ConfirmationScreen that prints results from last InputScreen
     *
     * @param message  Confirmation text
     * @param strData  Data saved on last InputScreen
     * @param showData true if it must print saved data
     */
    private void showConfirmingScreen(String message, String strData, boolean showData) {
        try {
            if (showData) {
                new ConfirmationScreen(this, printer, "Confirmation", message, strData).start();
            } else {
                new ConfirmationScreen(this, printer, "Confirmation", message).start();
            }
        } catch (CRMException e) {
            //start() a confirmationScreen wont send a exception
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Shows a ModalScreen with YES/NO option
     *
     * @param name    Screen name
     * @param message Message to be printed
     *
     * @return boolean value (true=YES, false=NO)
     */
    public boolean showModalScreen(String name, String message) {
        var modal = new ModalScreen(this, printer, name, message);
        try {
            var val = Commands.valueOf(modal.start().toUpperCase());
            return val.equals(YES);
        } catch (IllegalArgumentException e) {
            printer.showErrorLine(FORMAT_NOK);
            return showModalScreen(name, message);
        }
    }

    /**
     * Shows login screen
     *
     * @return the user logged
     */
    private User login_screen() {
        String strResult = "";
        var loginScreen = new InputScreen(this, printer, "Login", new TextObject("Enter your User Name and Password:"), new String[]{"User Name", "Password"}, OPEN, PASSWORD);
        try {
            strResult = loginScreen.start();
        } catch (CRMException e) {
            handleCRMExceptions(e);
        }
        var userVal = loginScreen.getValues();
        if (userVal == null || userVal.size() < 2) return null;
        if (checkCredentials(userVal.get(0), userVal.get(1))) {
            this.currentUser = crmData.getUserList().get(userVal.get(0));
            showConfirmingScreen("Welcome " + userVal.get(0) + "!", strResult, false);
        } else {
            printer.showErrorLine(WRONG_PASSWORD);
            login_screen();
        }
        return this.currentUser;
    }

    /**
     * Shows different menu for User or Admin
     *
     * @param currentUser User that is logged
     *
     * @return Command selected by user
     */
    private Commands menu_screen(User currentUser) {
        //todo
        try {
            return Commands.valueOf(new MenuScreen(this, printer, "Main Menu", LEAD, ACCOUNT, OPP).start().split(" ")[0].toUpperCase());//fixme
        } catch (Exception e) {
            printer.showErrorLine(COMMAND_NOK);
            return menu_screen(currentUser);
        }
    }

    private void opp_screen() {
        var oppScreen = new TableScreen<Opportunity>(this, "Opportunities", null).start();
    }

    private void account_screen() {
        var account_screen = new TableScreen<Account>(this, "Accounts", null).start();
    }

    private void lead_screen() {
        var list=  new java.util.ArrayList<Lead>();
        list.add(new Lead("a","0","96666666","ffff@fff.com","cocacola"));

                var leadScreen = new TableScreen<Lead>(this,
                "Leads",list).start();


    }


    //-------------------------------------------------------------------------------------------------------PRIVATE METHODS
    private CRMData loadData() throws Exception {
        //TODO LOAD FULL CRMData object from json and asign it to crmData field
        throw new IllegalAccessException();
    }

    private void saveData() throws Exception {
        //TODO Save crmData object to .json file in ./data
        throw new IllegalAccessException();
    }

    private void handleCRMExceptions(CRMException e) {
        if (e.getClass().equals(ExitException.class)) this.exit = true;
//        appStart();
        //TODO MANAGE OUR EXCEPTIONS NOT MANAGED IN SCREEN OR INPUT
    }

    /**
     * Checks user and password
     *
     * @param userName Username input
     * @param password password input
     *
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
