package com.ironhack.CRMManager;

import com.ironhack.Commercial.*;

import java.util.HashMap;

import static com.ironhack.Constants.Constants.MAX_ID;

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
        } else if (Account.class.equals(objClass)) {
            accountCounter++;
            sb.append("A").append(Integer.toHexString(Integer.parseInt(MAX_ID, 16) - accountCounter));
        } else if (Contact.class.equals(objClass)) {
            contactCounter++;
            sb.append("C").append(Integer.toHexString(Integer.parseInt(MAX_ID, 16) - contactCounter));
        }
        return sb.toString();


    }
      HashMap<String, User> getUserList() {
        return userList;
    } 
     boolean addToUserList(User newUser){
        return userList.putIfAbsent(newUser.getName(), newUser)==null;

    }
    //----------------------------------------LEAD
     int getLeadCounter() {
        return leadCounter;
    }
     CRMData increaseLeadCounter() {
        this.leadCounter++;
        return this;
    }
     CRMData addLead(Lead lead) {
        this.leadMap.put(lead.getId(),lead);
        return this;
    }
     HashMap<String, Lead> getLeadMap() {
        return leadMap;
    }
     public Lead getLead(String id) {
        return leadMap.get(id);
    }
    //----------------------------------------OPPORTUNITY
     int getOpportunityCounter() {
        return opportunityCounter;
    }
     CRMData increaseOpportunityCounter() {
        this.opportunityCounter++;
        return this;
    }
     HashMap<String, Opportunity> getOpportunityMap() {
    return opportunityMap;
}
     public Opportunity getOpportunity(String id) {
        return opportunityMap.get(id);
    }
     public CRMData addOpportunity(Opportunity opp) {
        this.opportunityMap.put(opp.getId(),opp);
        return this;
    }
    //----------------------------------------ACCOUNT
     int getAccountCounter() {
        return accountCounter;
    }
     CRMData increaseAccountCounter() {
        this.accountCounter++;
        return this;
    }    
     HashMap<String, Account> getAccountMap() {
        return accountMap;
    }
     Account getAccount(String id) {
        return accountMap.get(id);
    }
     CRMData addAccount(Account account) {
        this.accountMap.put(account.getId(),account);
        return this;
    }
    
    //----------------------------------------CONTACT
     int getContactCounter() {
        return contactCounter;
    }
     CRMData increaseContactCounter() {
        this.contactCounter++;
        return this;
    }
     HashMap<String, Contact> getContactMap() {
        return contactMap;
    }
     Contact getContact(String id) {
        return contactMap.get(id);
    }
     CRMData addContact(Contact contact) {
        this.contactMap.put(contact.getId(),contact);
        return this;
    }
    
}
