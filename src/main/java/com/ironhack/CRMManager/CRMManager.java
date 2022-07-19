package com.ironhack.CRMManager;

import com.ironhack.Exceptions.CRMException;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.Screens.InputScreen;
import com.ironhack.ScreenManager.Text.TextObject;

import static com.ironhack.ScreenManager.InputReader.*;

public class CRMManager {
    public boolean exit;
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

    private void newUser_screen(boolean isAdmin) throws CRMException {
        var newUserScreen= new InputScreen(this,printer, "New User",
                new TextObject("Enter a Name and Password to create a New User:"),
                new String[]{"User Name","Password","Repeat Password"},
                OPEN,PASSWORD,PASSWORD);
        newUserScreen.print();
        var userVal=newUserScreen.getValues();
        crmData.addToUserList(new User(userVal.get(0),userVal.get(1),isAdmin));

    }

    public void appStart(){
//        var currentScreen= new InputScreen("LOGIN");
        while (!exit){//if in any moment we enter EXIT it must turn this.exit to true so while will end
            try {
                newUser_screen(false);
            } catch (com.ironhack.Exceptions.CRMException e) {
                throw new RuntimeException(e);
            }
            //TODO printScreen(currentScreen);
            // currentScreen= currentScreen.processNextScreen() returns a screen from inputReader result

        }
        //TODO confirmationNeeded();
        //TODO beforeClose();
        System.exit(0);

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
        return crmData.getUserList().containsKey(userName) && java.util.Objects.equals(crmData.getUserList().get(userName).getPassword(), password);
    }


}
