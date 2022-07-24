package com.ironhack.CRMManager;

import com.ironhack.Commercial.Lead;
import com.ironhack.Exceptions.CRMException;
import com.ironhack.Exceptions.ExitException;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.Screens.*;
import com.ironhack.ScreenManager.Text.TextObject;
import lombok.NoArgsConstructor;

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
//            runFirstConfig(); FIXME UNCOMMENT TO DEPLOY
            crmData.addToUserList(new User("ADMIN","ADMIN",true));
            crmData.addToUserList(new User("USER","USER",false));

        }
        appStart();
    }
    public CRMManager(Boolean test) {
        this.exit = false;
        this.printer = new ConsolePrinter(this);

            crmData = new CRMData();
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
    private void appStart() {
        while (!exit) {//if in any moment we enter EXIT it must turn this.exit to true so while will end
            var comm = menu_screen(currentUser == null ? login_screen() : currentUser);
            switch (comm) {
                case OPP -> opp_screen();
                case LEAD ->  lead_screen();
                case ACCOUNT -> account_screen();
                case USERS -> manageUsers_screen();
                case STATS -> showStats_screen();
                case LOAD -> loadLeadData();
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

    private void loadLeadData() {
        //TODO
    }

    private void showStats_screen() {
        //TODO
    }

    private void manageUsers_screen() {
        //TODO
    }

    //---------------------------------------------------------------------------------------------------COMMAND ACTIONS
    public void closeOpportunity(String[] caughtInput) {
        //TODO
    }

    /*product - an Enum with options HYBRID, FLATBED, or BOX
    quantity - the number of trucks being considered for purchase
    decisionMaker - a Contact
    status*/
    public void convertLeadToOpp(String[] caughtInput) {
        try {
            new InputScreen(this,
                    printer,
                    "New Opportunity",
                    new com.ironhack.ScreenManager.Text.TextObject("Enter the information offered to customer:"),
                    new String[]{"Product", "Quantity", "Decision Maker", "Account"},
                    OPEN,
                    INTEGER,
                    CONTACT_INPUT,
                    ACCOUNT_INPUT).start();
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
     * @return Command selected by user
     */
    private Commands menu_screen(User currentUser) {
        //todo
        MenuScreen main_menu;
            main_menu = new MenuScreen(this,
                    printer,
                    "Main Menu",
                    currentUser);
        try {
            return Commands.valueOf(main_menu.start().split(" ")[0].toUpperCase());//fixme
        } catch (Exception e) {
            printer.showErrorLine(COMMAND_NOK);
            return menu_screen(currentUser);
        }
    }

    private void lead_screen() {
        var list = new java.util.ArrayList<Lead>();
        boolean stop = false;
        //FIXME : meanwhile leads are no implemented
        var fakeData = new java.util.ArrayList<>(java.util.List.of(com.ironhack.FakeLead.getRawLeads(200)));
        Commands comm = null;
        do {
            try {
                for (TextObject data : fakeData) {
                    list.add(new Lead(data.get(1), data.get(0), data.get(2), data.get(3), data.get(4)));
                }
                comm = Commands.valueOf(new TableScreen(this, "Leads", list).start());

                switch (comm) {
                    case MENU, BACK, LOGOUT -> {
                        stop = true;
                    }
                    case CONVERT -> convertLeadToOpp(comm.getCaughtInput());
                    case VIEW -> viewObject(comm.getCaughtInput());
                    case DISCARD -> {
                        if (new ModalScreen(this, getPrinter(), "Confirmation needed", "Do you want to discard this lead? ").start().equalsIgnoreCase(YES.name())) {
                            //TODO Delete lead from map and user list
                        }
                    }
                    case HELP -> {
                        //fixme Show be there?¿
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (!stop);

    }

    private void opp_screen() {
        var oppScreen = new TableScreen(this, "Opportunities", null).start();
    }

    private void account_screen() {
        var account_screen = new TableScreen(this, "Accounts", null).start();
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
