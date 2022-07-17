package com.ironhack.CRMManager;

import com.ironhack.Commercial.Lead;
import com.ironhack.Commercial.Opportunity;

import java.util.HashMap;

public class CRMManager {

    private String idGenerator;
    private String temporaryIdGenerator;
    private static HashMap<String, Lead> leadList;
    private static HashMap<String, Opportunity> opportunityList;
    private static HashMap<String, User> userList;

    public static void mainMenu (User user){
        Screens.mainMenu(user);

    }

    public static void addToUserList (User newUser){
        userList.put(newUser.getName(), newUser);
    }


    public  static boolean checkCredentials (String userName, String password){
        if (userList.containsKey(userName)&& userList.get(userName).getPassword() == password) {

            return true;
        }
        else {

            return false;
        }
    }

    public static HashMap<String, User> getUserList() {
        return userList;
    }

    public static void setUserList(HashMap<String, User> userList) {
        CRMManager.userList = userList;
    }
}
