package com.ironhack.CRMManager;

import com.ironhack.ScreenManager.Screens.InputScreen;

public class CRMManager {
    private boolean exit;
    CRMData crmData;
    public CRMManager(){
        this.exit=false;
        try{
            this.crmData=loadData();
        }catch (Exception e){
            this.crmData=new CRMData();
            runFirstConfig();
        }
    }

    public void appStart(){
        var currentScreen= new InputScreen("LOGIN");
        while (!exit){//if in any moment we enter EXIT it must turn this.exit to true so while will end
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
    }


//    public  void mainMenu (User user){
//        Screens.mainMenu(user);
//    }


    public  boolean checkCredentials (String userName, String password){
        return crmData.getUserList().containsKey(userName) && java.util.Objects.equals(crmData.getUserList().get(userName).getPassword(), password);
    }


}
