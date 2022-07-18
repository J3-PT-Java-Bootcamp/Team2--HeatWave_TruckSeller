package com.ironhack.CRMManager;

import com.ironhack.Commercial.Lead;
import com.ironhack.Commercial.Opportunity;

import java.util.HashMap;

public class CRMManager {

    private int leadCounter,opportunityCounter;
    private HashMap<String, Lead> leadList;
    private HashMap<String, Opportunity> opportunityList;
    private HashMap<String, User> userList;




    public CRMManager(){
        try{
            loadData();
        }catch (Exception e){
            leadCounter=0;
            opportunityCounter=0;
            leadList=new HashMap<>();
            opportunityList=new HashMap<>();
            userList=new HashMap<>();
        }
    }

    private void loadData() throws Exception {
        //TODO
        throw new IllegalAccessException();
    }
    private void saveData() throws Exception{
        //TODO
        throw new IllegalAccessException();
    }

    public  void mainMenu (User user){
        Screens.mainMenu(user);
    }

    public void addToUserList(User newUser){
        userList.put(newUser.getName(), newUser);
    }


    public  boolean checkCredentials (String userName, String password){
        return userList.containsKey(userName) && java.util.Objects.equals(userList.get(userName).getPassword(), password);
    }

    public  HashMap<String, User> getUserList() {
        return userList;
    }

}
