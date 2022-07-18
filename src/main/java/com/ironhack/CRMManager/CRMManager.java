package com.ironhack.CRMManager;

import com.ironhack.Commercial.Account;
import com.ironhack.Commercial.Contact;
import com.ironhack.Commercial.Lead;
import com.ironhack.Commercial.Opportunity;

import java.util.HashMap;

import static com.ironhack.Constants.Constants.MAX_ID;

public class CRMManager {
    CRMData crmData;
    public CRMManager(){
        try{
            this.crmData=loadData();
        }catch (Exception e){
            this.crmData=new CRMData();
            runFirstConfig();
        }
    }

    private void runFirstConfig() {
        //TODO METHOD THAT CREATES A ADMIN USER AND ASK FOR OTHER USERS DATA
    }


    private CRMData loadData() throws Exception {
        //TODO LOAD FULL CRMData object from json
        throw new IllegalAccessException();
    }
    private void saveData() throws Exception{
        //TODO Save crmData object to .json file in ./data
        throw new IllegalAccessException();
    }

//    public  void mainMenu (User user){
//        Screens.mainMenu(user);
//    }


    public  boolean checkCredentials (String userName, String password){
        return crmData.getUserList().containsKey(userName) && java.util.Objects.equals(crmData.getUserList().get(userName).getPassword(), password);
    }


}
