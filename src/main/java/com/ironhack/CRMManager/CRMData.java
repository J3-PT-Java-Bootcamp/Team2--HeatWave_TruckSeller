package com.ironhack.CRMManager;

import com.ironhack.Commercial.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;

import static com.ironhack.Constants.Constants.MAX_ID;
@Data
public class CRMData {
    private int leadCounter,opportunityCounter,accountCounter,contactCounter;
    private HashMap<String, Lead> leadMap;
    private HashMap<String, Opportunity> opportunityMap;
    private HashMap<String, Account> accountMap;

    private HashMap<String, Contact> contactMap;
    private HashMap<String, User> userList;
    
    public CRMData(){
        leadCounter=0;
        opportunityCounter=0;
        leadMap =new HashMap<>();
        opportunityMap =new HashMap<>();
        contactMap=new HashMap<>();
        accountMap=new HashMap<>();

        userList=new HashMap<>();
    }
    public static CRMData loadCRMData(String rawData){
        com.google.gson.Gson gson=new com.google.gson.Gson();
        return gson.fromJson(rawData,CRMData.class);
        
    }


    //---------------------------------------------------------------------------------------------------GETTERSnSETTERS
    public  String getNextID(Class objClass){
        var sb= new StringBuilder();
        if (Opportunity.class.equals(objClass)) {
            opportunityCounter++;
            sb.append("OP").append(Integer.toHexString(Integer.parseInt(MAX_ID, 16) - opportunityCounter));
        } else if (Lead.class.equals(objClass)) {
            leadCounter++;
            sb.append("L").append(Integer.toHexString(Integer.parseInt(MAX_ID, 16) - leadCounter));
        } else if (Contact.class.equals(objClass)) {
            contactCounter++;
            sb.append("C").append(Integer.toHexString(Integer.parseInt(MAX_ID, 16) - contactCounter));
        }
        return sb.toString().toUpperCase();


    }
      HashMap<String, User> getUserList() {
        return userList;
    } 
     boolean addToUserList(User newUser){
        return userList.putIfAbsent(newUser.getName(), newUser)==null;

    }
    //----------------------------------------LEAD
     public CRMData increaseLeadCounter() {
        this.leadCounter++;
        return this;
    }
     public CRMData addLead(Lead lead) {
        this.leadMap.put(lead.getId(),lead);
        return this;
    }
     public Lead getLead(String id) {
        return leadMap.get(id);
    }
    //----------------------------------------OPPORTUNITY
     public CRMData increaseOpportunityCounter() {
        this.opportunityCounter++;
        return this;
    }
     public Opportunity getOpportunity(String id) {
        return opportunityMap.get(id);
    }
     public CRMData addOpportunity(Opportunity opp) {
        this.opportunityMap.put(opp.getId(),opp);
        return this;
    }
    //----------------------------------------ACCOUNT
     public int getAccountCounter() {
        return accountCounter;
    }
     public CRMData increaseAccountCounter() {
        this.accountCounter++;
        return this;
    }    
     public HashMap<String, Account> getAccountMap() {
        return accountMap;
    }
     public Account getAccount(String id) {
        return accountMap.get(id);
    }
     public CRMData addAccount(Account account) {
        this.accountMap.put(account.getCompanyName(),account);
        return this;
    }
    
    //----------------------------------------CONTACT
     public CRMData increaseContactCounter() {
        this.contactCounter++;
        return this;
    }
     public Contact getContact(String id) {
        return contactMap.get(id);
    }
     public CRMData addContact(Contact contact) {
        this.contactMap.put(contact.getId(),contact);
        return this;
    }

    public ArrayList<User> getUsers(boolean includeAdmin) {
        if(includeAdmin)return (ArrayList<User>) this.getUserList().values();
        var resArr= new ArrayList<User>();
        for (User user:userList.values()) {
            if (!user.isAdmin())resArr.add(user);
        }
        return resArr;
    }

    public Printable getUnknownObject(String id) {
        if(id.startsWith("L")) return leadMap.get(id);
        else if (id.startsWith("O")) return opportunityMap.get(id);
        else if (id.startsWith("C")) return contactMap.get(id);
        return accountMap.get(id);
    }
}
