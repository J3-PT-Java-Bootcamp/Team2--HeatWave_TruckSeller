package com.ironhack.CRMManager;

import com.ironhack.Commercial.*;
import com.ironhack.Constants.ColorFactory;
import com.ironhack.Constants.IndustryType;
import com.ironhack.Constants.Product;
import com.ironhack.Exceptions.CRMException;
import com.ironhack.Exceptions.ExitException;
import com.ironhack.Exceptions.NoCompleteObjectException;
import com.ironhack.FakeLead;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.Screens.*;
import com.ironhack.ScreenManager.Text.TextObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Objects;

import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;
import static com.ironhack.Constants.Constants.LIMIT_X;
import static com.ironhack.Constants.Constants.LIMIT_Y;
import static com.ironhack.Exceptions.ErrorType.*;
import static com.ironhack.ScreenManager.InputReader.*;
import static com.ironhack.ScreenManager.Screens.Commands.EXIT;
import static com.ironhack.ScreenManager.Screens.Commands.*;

@lombok.Data
public class CRMManager {
    public static final ColorFactory.BgColors MAIN_BG = ColorFactory.BgColors.CYAN;
    public static final ColorFactory.CColors MAIN_TXT_COLOR = ColorFactory.CColors.BLACK;
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

    /**
     * Constructor only for tests purposes
     * @param testWithScreens if true it will prompt all screens, else it will run silently
     */
    public CRMManager(Boolean testWithScreens) {
        this.exit = false;
        this.printer = new ConsolePrinter(this);
        crmData = new CRMData();
        var leadList= FakeLead.getRawLeads(200);

        crmData.addToUserList(new User("ADMIN", "ADMIN", true));
        crmData.addToUserList(new User("USER", "USER", false));
        crmData.addToUserList(new User("PATATA", "111", false));
        crmData.addToUserList(new User("FRITA", "111", false));
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
        boolean stop = false;
        do {
            var txtObj=  new TextObject("You can load lead data from any CSV file saved on root/import",LIMIT_X/2,LIMIT_Y)
                    .addText(BLANK_SPACE).setBgcolor(MAIN_BG).setTxtColor(MAIN_TXT_COLOR);
            var rightCol= new TextObject("File names:");
            var leftCol= new TextObject("INDEX: ");
            var files = new File("import").listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                rightCol.addText(file.getName());
                leftCol.addText("-"+i+": ");
            }
            txtObj.addGroupInColumns(2, txtObj.MAX_WIDTH,new TextObject[]{leftCol,rightCol} );
            var inpScreen=new InputScreen(this, printer, "Select a File: ",txtObj,
                    new String[]{"File Index"}, INTEGER);
            try {
                var resVal= inpScreen.start();
                if(resVal.contains(":"))resVal=resVal.split(":")[1].trim();
                if(resVal.equals(EXIT.name()))stop=true;
                var index = Integer.parseInt(resVal);
                var leads=parseCSVLeads(files[index]);
                if(!leads.isEmpty()){
                    assignLeadsToUser(leads);
                };

            } catch (Exception ignored) {
            }


        }while(!stop);
    }

    private void assignLeadsToUser(ArrayList<Lead> leadList) throws Exception {
        TextObject txtObj;
        boolean stop=false;
        do {
            if (crmData.getUserList().isEmpty()) {
                break;
            } else {
                txtObj = new TextObject("-- USERS --").addText(BLANK_SPACE);
                for (User user : crmData.getUserList().values()) {
                    txtObj.addText(user.getName());
                }
                txtObj.addText(BLANK_SPACE)
                        .addText("Enter a user name to assign loaded Leads")
                        .addText("Or enter \"ALL\" to divide leads between all users")
                        .addText(BLANK_SPACE).addText(BLANK_SPACE);
            }
            var selectUserScreen = new InputScreen(this,
                    printer,
                    "Select User:",
                    txtObj,
                    new String[]{"User Name"},
                    OPEN);
            var strRes = selectUserScreen.start();
            if (strRes.equalsIgnoreCase(EXIT.name())) break;
            var userVal = selectUserScreen.getValues();
            if (userVal != null && !userVal.isEmpty()) {
                var users = crmData.getUsers(false);
                int leadsPerUser = leadList.size()/users.size();
                int rest= leadList.size()%users.size();
                int counter=0;
                if (userVal.get(0).equalsIgnoreCase("ALL")) {
                    for (int i = 0; i < users.size(); i++) {
                        for (int j = 0; j < leadsPerUser+(i== users.size()-1?rest:0); j++) {
                            String id = leadList.get(j+counter).getId();
                            users.get(i).addToLeadList(id);
                            crmData.addLead(leadList.get(j+counter));
                        }
                        counter+=leadsPerUser;
                    }
                    saveData();
                    showConfirmingScreen("All leads assigned correctly",
                            "",
                            false);
                    stop=true;
                } else if (crmData.getUserList().containsKey(userVal.get(0))) {
                    var user = crmData.getUserList().get(userVal.get(0));
                    for(Lead lead: leadList ){
                        user.getLeadList().add(lead.getId());
                        crmData.addLead(lead);
                    }
                    saveData();
                    showConfirmingScreen("All leads assigned to: " + userVal.get(0) + " correctly",
                            "",
                            false);
                    stop=true;
                }
            }
        }while (!stop);
    }

    private ArrayList<Lead> parseCSVLeads(File file) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(file));
        ArrayList<Lead>resVal=new ArrayList<>();
        String line;
        while ((line=br.readLine())!=null){
            String[] values=line.split(",");
            Lead lead = new Lead(values[0], crmData.getNextID(Lead.class), values[1], values[2], values[3]);
            resVal.add(lead);
        }
        saveData();
        return resVal;
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
                    var oppBuilder= new OpportunityBuilder();
                    var firstScreen = new InputScreen(this,
                            printer,
                            "New Opportunity",
                            new TextObject("Enter the information offered to customer:"),
                            new String[]{"Product", "Quantity", "Decision Maker", "Account"},
                            PRODUCT_TYPE,
                            INTEGER);
                    var result = firstScreen.start();
                    if (result.equalsIgnoreCase(EXIT.name())) return EXIT.name();
                    //TODO CHANGE FOR BUILDER

                    var accountName = account_screen(true);
                    //TODO CHANGE FOR BUILDER
                    var contact = createNewContactBuilder(lead, accountName);

                    var firstData = firstScreen.getValues();
//                    var opp = new Opportunity(Product.valueOf(firstData.get(0)), Integer.parseInt(firstData.get(1)), contact, OpportunityStatus.OPEN, currentUser.getName());
                    //TODO CHANGE FOR BUILDER
                    oppBuilder.setOwner(currentUser.getName());
                    oppBuilder.setProduct(Product.valueOf(firstData.get(0)));
                    oppBuilder.setQuantity(Integer.parseInt(firstData.get(1)));
                    var opp =oppBuilder.constructOpportunity(accountName,contact);
                    crmData.addOpportunity(opp);
                    crmData.removeLead(lead.getId());
                    try {
                        saveData();
                    } catch (Exception ignored) {
                    }
                    return opp.getId();
                } catch (com.ironhack.Exceptions.CRMException e) {
                    throw new RuntimeException(e);
                } catch (NoCompleteObjectException e) {
                    throw new RuntimeException(e);
                }
            }

            printer.showErrorLine(COMMAND_NOK);
        }
        return null;
    }

    private ContactBuilder createNewContactBuilder(Lead lead, String accountName) throws CRMException, NoCompleteObjectException {
        ContactBuilder contact= new ContactBuilder();
        if (showModalScreen("Copy Lead Data ?",
                new TextObject("Do you want to create a new contact\n from the following Lead data?")
                        .addText(BLANK_SPACE).addText(lead.printFullObject())
                        .addText("-- Enter \"NO\" to enter manually a new contact information --"))) {
            contact.setName(lead.getName());
            contact.setPhoneNumber(lead.getPhoneNumber());
            contact.setMail(lead.getMail());
            contact.setCompany(accountName);
        } else {
            var screen = new InputScreen(this,
                    printer,
                    "-- New Contact --",
                    new TextObject("Enter the information about Contact for current Opportunity:").addText(BLANK_SPACE).addText("Company : " + accountName),
                    new String[]{"Name", "Phone Number", "Mail"},
                    OPEN,
                    PHONE,
                    MAIL);
            screen.start();
            var val = screen.getValues();
            contact.setName(val.get(0));
            contact.setPhoneNumber(val.get(1));
            contact.setMail(val.get(2));
            contact.setCompany(accountName);
        }
            return contact;
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
                        var lead = crmData.getLead(comm.getCaughtInput()[1]);
                        if (showModalScreen("Delete Lead ?", new TextObject("Do yo want to delete this Lead?").addText(lead.printFullObject()))) {
                           var arr=currentUser.getLeadList();
                            for (int i = 0; i < arr.size(); i++) {
                                if(arr.get(i).equalsIgnoreCase(lead.getId())) {
                                    currentUser.getLeadList().remove(i);
                                    break;
                                }
                            }
                            crmData.removeLead(lead.getId());

                        }
                    }
                    case HELP -> {
                        //fixme Should be there?¿
                    }
                }
            } catch (NullPointerException e) {
                break;
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
        if(selectionMode&&crmData.isEmptyMap(Account.class)) return createNewAccount();
        do {
            var accountArr= new ArrayList<Account>();
            if(!crmData.isEmptyMap(Account.class)) {
                accountArr =crmData.getAccountsAsList();
            }
            var account_screen = new TableScreen(this, selectionMode ? "Select an Account" : "Accounts", accountArr);

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
                case VIEW -> {
                    if (selectionMode) return res.getCaughtInput()[1];
                    viewObject(res.getCaughtInput());
                }
                case DISCARD -> {
                    //FIXME should delete an account with active opps?¿
                    var account = crmData.getAccount(res.getCaughtInput()[1]);
                    if (showModalScreen("Delete Account ?", new TextObject("Do yo want to delete this Lead?").addText(account.printFullObject()))) {
                        crmData.removeAccount(account.getCompanyName());
                    }


                }
            }

        } while (!stop);
        return res.name();
    }

    private String createNewAccount(String... importedData) {

        var newAccountScreen = new InputScreen(this,
                printer,
                "New Account",
                new TextObject("Enter data for the new Account: ").addText(BLANK_SPACE),
                new String[]{"Company name","Industry Type", "Employees","City","Country"},
                OPEN,INDUSTRY_TYPE,INTEGER,OPEN,OPEN);
        String strRes = null;
        try {
            strRes=newAccountScreen.start();
            if(Objects.equals(strRes, EXIT.name()))return EXIT.name();
        } catch (CRMException e) {
            return "";
        }
        var userVal = newAccountScreen.getValues();

        if (userVal != null && !userVal.isEmpty()&&userVal.size()>=5) {
            var account=new AccountBuilder();

            account.setCompanyName(userVal.get(0));
            account.setIndustryType(IndustryType.valueOf(userVal.get(1)));
            account.setEmployeeCount(Integer.parseInt(userVal.get(2)));
            account.setCity(userVal.get(3));
            account.setCountry(userVal.get(4));
            try {
                var finalAccount= account.constructAccount();
                crmData.addAccount(finalAccount);
                showConfirmingScreen("User " + userVal.get(0) + " password was properly updated.",
                        strRes,
                        true);
                return finalAccount.getCompanyName();
            } catch (NoCompleteObjectException e) {
                throw new RuntimeException(e);
            }
        }
        return createNewAccount(importedData);
    }


    //-------------------------------------------------------------------------------------------------------PRIVATE METHODS
    private CRMData loadData() throws Exception {
        //TODO LOAD FULL CRMData object from json and aSsign it to crmData field
        throw new IllegalAccessException();
    }

    private void saveData() throws Exception {
        //TODO Save crmData object to .json file in ./data

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
