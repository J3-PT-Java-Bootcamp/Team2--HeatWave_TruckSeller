package com.ironhack.CRMManager;

import com.ironhack.Commercial.Account;
import com.ironhack.Commercial.Contact;
import com.ironhack.Commercial.Lead;
import com.ironhack.Commercial.Opportunity;
import com.ironhack.Constants.OpportunityStatus;
import com.ironhack.Exceptions.CRMException;
import com.ironhack.Exceptions.ExitException;
import com.ironhack.FakeLead;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.Screens.*;
import com.ironhack.ScreenManager.Text.TextObject;

import java.util.ArrayList;
import java.util.Objects;

import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;
import static com.ironhack.Exceptions.ErrorType.*;
import static com.ironhack.ScreenManager.InputReader.*;
import static com.ironhack.ScreenManager.Screens.Commands.EXIT;
import static com.ironhack.ScreenManager.Screens.Commands.YES;

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

    public CRMManager(Boolean testWithScreens) {
        this.exit = false;
        this.printer = new ConsolePrinter(this);
        crmData = new CRMData();
        var leadList= FakeLead.getRawLeads(200);

        crmData.addToUserList(new User("ADMIN", "ADMIN", true));
        crmData.addToUserList(new User("USER", "USER", false));
        for (TextObject data : leadList) {
            try {
                var lead= new Lead(data.get(1), data.get(0), data.get(2), data.get(3), data.get(4));
                crmData.addLead(lead);
                crmData.getUserList().get("USER").addToLeadList(lead.getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        if (testWithScreens)appStart();
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
                case LEAD -> lead_screen();
                case ACCOUNT -> account_screen(false);
                case USERS -> manageUsers_screen();
                case STATS -> showStats_screen();
                case LOAD -> loadLeadData();
                case CLOSE -> closeOpportunity(comm.getCaughtInput());
                case CONVERT -> convertLeadToOpp(COMMAND.lastInput.split(" "));
                case VIEW -> viewObject(comm.getCaughtInput());
                default -> {
                }
            }
        }
        System.exit(0);
    }
    //----------------------------------------------------------------------------------------------ADMIN SCREEN OPTIONS
    private void loadLeadData() {
        //TODO
    }

    private void showStats_screen() {
        //TODO
    }

    private void manageUsers_screen() {
        try {
            newUser_screen(false);
        } catch (CRMException e) {

        }
    }

    //---------------------------------------------------------------------------------------------------COMMAND ACTIONS
    public String closeOpportunity(String[] caughtInput) {
        //TODO
        return null;
    }

    public String convertLeadToOpp(String[] caughtInput) {
        if (caughtInput != null && caughtInput.length == 2) {
            var lead = crmData.getLead(caughtInput[1]);
            if (lead != null) {
                try {
                    var firstScreen = new InputScreen(this, printer, "New Opportunity", new TextObject("Enter the information offered to customer:"), new String[]{"Product", "Quantity", "Decision Maker", "Account"}, PRODUCT_TYPE, INTEGER);
                    var result = firstScreen.start();
                    if (result.equalsIgnoreCase(EXIT.name())) return EXIT.name();
                    var accountName = account_screen(true);
                    var contact = createNewContact(lead, accountName);
                    var firstData = firstScreen.getValues();
//                    var opp = new Opportunity(Product.valueOf(firstData.get(0)), Integer.parseInt(firstData.get(1)), contact, OpportunityStatus.OPEN, currentUser.getName());
                    var opp = new Opportunity(null, Integer.parseInt(firstData.get(1)), contact, OpportunityStatus.OPEN, currentUser.getName());
                    crmData.addOpportunity(opp);
                    crmData.getLeadMap().remove(lead.getId());
                    try {
                        saveData();
                    } catch (Exception ignored) {
                    }
                    return opp.getId();
                } catch (com.ironhack.Exceptions.CRMException e) {
                    throw new RuntimeException(e);
                }
            }

            printer.showErrorLine(COMMAND_NOK);
        }
        return null;
    }

    private String createNewContact(Lead lead, String accountName) throws CRMException {
        Contact contact;
        if (showModalScreen("Copy Lead Data ?", new TextObject("Do you want to create a new contact\n from the following Lead data?").addText(BLANK_SPACE).addText(lead.printFullObject()).addText("-- Enter \"NO\" to enter manually a new contact information --"))) {
            contact = new Contact(lead.getName(), crmData.getNextID(Contact.class), lead.getPhoneNumber(), lead.getMail(), accountName);
        } else {
            var screen = new InputScreen(this, printer, "-- New Contact --", new TextObject("Enter the information about Contact for current Opportunity:").addText(BLANK_SPACE).addText("Company : " + accountName), new String[]{"Name", "Phone Number", "Mail"}, OPEN, PHONE, MAIL);
            screen.start();
            var val = screen.getValues();
            contact = new Contact(val.get(0), crmData.getNextID(Contact.class), val.get(1), val.get(2), accountName);

        }

        crmData.addContact(contact);
        crmData.getAccountMap().get(accountName).getContacts().add(contact.getId());
        return contact.getId();
    }

    public void viewObject(String[] caughtInput) {
        if(caughtInput!=null&&caughtInput.length==2) {
            char identifier= caughtInput[1].toCharArray()[0];
            String title;
            switch (identifier){
                case 'O'->{
                    var object= crmData.getOpportunity(caughtInput[1]);
                    if (object==null){break;}
                    title = "Opportunity: "+ object.getId();
                    try {
                        new ViewScreen(this, printer, title, object).start();
                    } catch (CRMException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 'C'->{
                    var object= crmData.getContact(caughtInput[1]);
                    if (object==null){break;}
                    title = "Contact: "+ object.getName();
                    try {

                        new ViewScreen(this, printer, title, object).start();
                    } catch (CRMException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 'L'->{
                    var object= crmData.getLead(caughtInput[1]);
                    if (object==null){break;}
                    title = "Lead: "+ object.getId();
                    try {
                        new ViewScreen(this, printer, title, object).start();
                    } catch (CRMException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }
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
        var newUserScreen = new InputScreen(this,
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
                        saveData();
                    } catch (Exception ignored) {
                    }
                    showConfirmingScreen("User " + userVal.get(0) + " password was properly updated.",
                            strRes,
                            true);
                }
            } else {
                if (userVal.size() == 3 && crmData.addToUserList(new User(userVal.get(0), userVal.get(1), isAdmin))) {
                    showConfirmingScreen("User " + userVal.get(0) + " was properly saved.",
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
    public boolean showModalScreen(String name, TextObject message) {
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
        MenuScreen main_menu = new MenuScreen(this, printer, "Main Menu", currentUser);
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
        Commands comm = null;
        do {
            try {
                for (String id:currentUser.getLeadList()){
                    list.add(crmData.getLead(id));
                }
                comm = Commands.valueOf(new TableScreen(this, "Leads", list).start());

                switch (comm) {
                    case MENU, BACK, LOGOUT -> {
                        stop = true;
                    }
                    case CONVERT -> convertLeadToOpp(comm.getCaughtInput());
                    case VIEW -> viewObject(comm.getCaughtInput());
                    case DISCARD -> {
                        var lead = crmData.getLeadMap().get(comm.getCaughtInput()[1]);
                        if (showModalScreen("Delete Lead ?", new TextObject("Do yo want to delete this Lead?").addText(lead.printFullObject()))) {
                            crmData.getLeadMap().remove(lead.getId(), lead);
                        }
                    }
                    case HELP -> {
                        //fixme Should be there?¿
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            list.clear();
        } while (!stop);

    }

    private void opp_screen() {
        var oppScreen = new TableScreen(this, "Opportunities", null).start();
    }

    private String account_screen(boolean selectionMode) {
        boolean stop = false;
        Commands res;
        do {
            var accountArr= new ArrayList<Account>();
            if(!crmData.getAccountMap().isEmpty())accountArr= (ArrayList<Account>) crmData.getAccountMap().values();
            var account_screen = new TableScreen(this, selectionMode ? "Select an account: " : "-- Accounts --", accountArr);
            res = Commands.valueOf(account_screen.start());
            switch (res) {
                case EXIT, MENU, LOGOUT, BACK -> {
                    stop = true;
                }
                case HELP -> {
                    //fixme HELP CASE must be there?
                }
                case CREATE -> {
                    createNewAccount();
                }
                case CONVERT -> {
                    return convertLeadToOpp(res.getCaughtInput());
                }
                case CLOSE -> {
                    return closeOpportunity(res.getCaughtInput());
                }
                case VIEW -> {
                    if (selectionMode) return res.getCaughtInput()[1];
                    viewObject(res.getCaughtInput());
                }
                case DISCARD -> {
                    //FIXME should delete an account with active opps?¿
                    var account = crmData.getAccountMap().get(res.getCaughtInput()[1]);
                    if (showModalScreen("Delete Lead ?", new TextObject("Do yo want to delete this Lead?").addText(account.printFullObject()))) {
                        crmData.getAccountMap().remove(account.getCompanyName());
                    }


                }
            }

        } while (!stop);
        return res.name();
    }

    private void createNewAccount() {

        var newAccountScreen = new InputScreen(this,
                printer,
                "New Account",
                new TextObject("Enter data for the new Account: ").addText(BLANK_SPACE),
                new String[]{"Company name","Industry Type", "Employees","City","Country"},
                OPEN,INDUSTRY_TYPE,INTEGER,OPEN,OPEN);
        String strRes = null;
        try {
            strRes=newAccountScreen.start();
            if(Objects.equals(strRes, EXIT.name()))return;
        } catch (CRMException e) {
            return;
        }
        var userVal = newAccountScreen.getValues();

        if (userVal != null && !userVal.isEmpty()) {

                    showConfirmingScreen("User " + userVal.get(0) + " password was properly updated.",
                            strRes,
                            true);

        }
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
