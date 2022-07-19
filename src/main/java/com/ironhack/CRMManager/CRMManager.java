package com.ironhack.CRMManager;

import com.ironhack.Exceptions.CRMException;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.Screens.Commands;
import com.ironhack.ScreenManager.Screens.InputScreen;
import com.ironhack.ScreenManager.Text.TextObject;

import static com.ironhack.Exceptions.ErrorType.*;
import static com.ironhack.ScreenManager.InputReader.*;
import static com.ironhack.ScreenManager.Screens.Commands.*;

public class CRMManager {
    public boolean exit;
    public User currentUser;
    private ConsolePrinter printer;
    public static CRMData crmData;
    public CRMManager(){
        this.exit=false;
        this.printer=new ConsolePrinter(this);
        try{
            crmData=loadData();
        }catch (Exception e){
            crmData=new CRMData();
            runFirstConfig();
        }
        appStart();
    }
    public void appStart(){
//        var currentScreen= new InputScreen("LOGIN");
        while (!exit){//if in any moment we enter EXIT it must turn this.exit to true so while will end
            switch (menu_screen(currentUser==null?login_screen():currentUser)){
                case OPP -> {
                    //TODO
                }
                case LEAD -> {
                    //TODO
                }
                case ACCOUNT -> {
                    //todo
                }

                default -> throw new IllegalStateException("Unexpected value: " + menu_screen(currentUser));
            };
            //TODO printScreen(currentScreen);
            // currentScreen= currentScreen.processNextScreen() returns a screen from inputReader result

        }
        //TODO confirmationNeeded();
        //TODO beforeClose();
        System.exit(0);

    }

    private Commands menu_screen(com.ironhack.CRMManager.User currentUser) {
        return Commands.valueOf(new com.ironhack.ScreenManager.Screens.MenuScreen(this,
                printer,
                "Main Menu",
                LEAD,
                ACCOUNT,
                OPP).print().toUpperCase());
    }

    //-----------------------------------------------------------------------------------------------------------SCREENS
    private void newUser_screen(boolean isAdmin) throws CRMException {
        var newUserScreen= new InputScreen(this,printer, "New User",
                new TextObject("Enter a Name and Password to create a New User:"),
                new String[]{"User Name","Password","Repeat Password"},
                OPEN, NEW_PASSWORD, NEW_PASSWORD);
        newUserScreen.print();
        var userVal=newUserScreen.getValues();
        if(crmData.addToUserList(new User(userVal.get(0),userVal.get(1),isAdmin))){
            showConfirmingScreen("User "+ userVal.get(0)+" was properly saved.");
        }

    }

    private void showConfirmingScreen(String message)  {
        try {
            new com.ironhack.ScreenManager.Screens.ConfirmationScreen(this,printer,"Confirmation",message).print();
        } catch (com.ironhack.Exceptions.CRMException e) {
            //print() a confirmationScreen wont send a exception
            throw new RuntimeException(e.getMessage());
        }
    }

    private User login_screen() {
        var loginScreen= new InputScreen(this,printer, "Login",
                new TextObject("Enter your User Name and Password:"),
                new String[]{"User Name","Password"},
                OPEN, PASSWORD);
        try {
            loginScreen.print();
        } catch (com.ironhack.Exceptions.CRMException e) {
            handleCRMExceptions(e);
        }
        var userVal=loginScreen.getValues();
        if (checkCredentials(userVal.get(0),userVal.get(1))){
            this.currentUser= crmData.getUserList().get(userVal.get(0));
            showConfirmingScreen("Welcome "+userVal.get(0)+"!");
        }
        else {
            printer.showErrorLine(WRONG_PASSWORD);
            login_screen();
        }
        return this.currentUser;
    }

    private void handleCRMExceptions(com.ironhack.Exceptions.CRMException e) {
        appStart();
        //TODO MANAGE OUR EXCEPTIONS, exit without save case is VALIDATED BEFORE IN inputScreen
    }

    //-------------------------------------------------------------------------------------------------------PRIVATE METHODS
    private CRMData loadData() throws Exception {
        //TODO LOAD FULL CRMData object from json
        throw new IllegalAccessException();
    }
    private void saveData() throws Exception{
        //TODO Save crmData object to .json file in ./data
        throw new IllegalAccessException();
    }


    private void runFirstConfig() {
        //TODO METHOD THAT CREATES A ADMIN USER AND ASK FOR OTHER USERS DATA

        try {
            newUser_screen(true);
        } catch (com.ironhack.Exceptions.CRMException e) {
            runFirstConfig();
        }
    }

    public  boolean checkCredentials (String userName, String password){
        var user=crmData.getUserList().get(userName);
        if (user != null) {
            return user.getPassword().equalsIgnoreCase(password);
        }
        return false;
    }


    public boolean showModal(String name,String message) {
        var modal=new com.ironhack.ScreenManager.Screens.ModalScreen(this,printer,name,message);
        try {
            if (Commands.valueOf(modal.print().toUpperCase()).equals(YES)) return true;
            if (Commands.valueOf(modal.print().toUpperCase()).equals(NO)) return false;
        }catch (IllegalArgumentException e){
            printer.showErrorLine(FORMAT_NOK);
            return showModal(name,message);
        }
        return showModal(name,message);


    }
}
