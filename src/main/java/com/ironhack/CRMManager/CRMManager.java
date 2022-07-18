package com.ironhack.CRMManager;

import com.ironhack.Commercial.Account;
import com.ironhack.Commercial.Contact;
import com.ironhack.Commercial.Lead;
import com.ironhack.Commercial.Opportunity;

import java.util.HashMap;

import static com.ironhack.Constants.Constants.MAX_ID;

public class CRMManager {

    private int leadCounter,opportunityCounter,accountCounter,contactCounter;
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

    public String getNextID(Class objClass){
        var sb= new StringBuilder();
        if (Opportunity.class.equals(objClass)) {
            opportunityCounter++;
            sb.append("OP").append(Integer.toHexString(Integer.parseInt(MAX_ID, 16) - opportunityCounter));
        } else if (Lead.class.equals(objClass)) {
            leadCounter++;
            sb.append("L").append(Integer.toHexString(Integer.parseInt(MAX_ID, 16) - leadCounter));
        } else if (Account.class.equals(objClass)) {
            accountCounter++;
            sb.append("A").append(Integer.toHexString(Integer.parseInt(MAX_ID, 16) - accountCounter));
        } else if (Contact.class.equals(objClass)) {
            contactCounter++;
            sb.append("C").append(Integer.toHexString(Integer.parseInt(MAX_ID, 16) - contactCounter));
        }
        return sb.toString();
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
