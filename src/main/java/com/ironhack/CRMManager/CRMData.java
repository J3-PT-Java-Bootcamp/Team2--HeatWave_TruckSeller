package com.ironhack.CRMManager;

import com.ironhack.Commercial.*;

import java.util.ArrayList;
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
     public CRMData addLead(Lead lead) {
        this.leadMap.put(lead.getId(),lead);
        return this;
    }
     public CRMData removeLead(String id){
        this.leadMap.remove(id);
        return this;
     }
     public Lead getLead(String id) {
        return leadMap.get(id);
    }

    public ArrayList<Lead> getLeadsAsList(){
        return new ArrayList<>(leadMap.values());
    }
    //----------------------------------------OPPORTUNITY

    public ArrayList<Opportunity> getOpportunitiesAsList(){
        return new ArrayList<>(opportunityMap.values());
    }
     public Opportunity getOpportunity(String id) {
        return opportunityMap.get(id);
    }
     public CRMData addOpportunity(Opportunity opp) {
        this.opportunityMap.put(opp.getId(),opp);
        return this;
    }
    public CRMData removeOpportunity(String id){
        this.opportunityMap.remove(id);
        return this;
    }
    //----------------------------------------ACCOUNT
     public Account getAccount(String id) {
        return accountMap.get(id);
    }
    public ArrayList<Account> getAccountsAsList(){
        return new ArrayList<>(accountMap.values());
    }
     public CRMData addAccount(Account account) {
        this.accountMap.put(account.getCompanyName(),account);
        return this;
    }

    public CRMData removeAccount(String id) {
        this.accountMap.remove(id);
        return this;
    }
    //----------------------------------------CONTACT
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
    public CRMData removeContact(String id){
        this.leadMap.remove(id);
        return this;}


    //----------------------------------------------------------------------------------------------------UNKNOWN OBJECT
    public Printable getUnknownObject(String id) {
        if(id.startsWith("L")) return leadMap.get(id);
        else if (id.startsWith("O")) return opportunityMap.get(id);
        else if (id.startsWith("C")) return contactMap.get(id);
        return accountMap.get(id);
    }
    public CRMData removeUnknownObject(String id){
        if(id.startsWith("L")) return removeLead(id);
        else if (id.startsWith("O")) return removeOpportunity(id);
        else if (id.startsWith("C")) return removeContact(id);
        return removeAccount(id);
    }
    public CRMData addUnknownObject(Printable obj){
        if(obj instanceof Lead) return addLead((Lead)obj);
        else if (obj instanceof Opportunity) return addOpportunity((Opportunity) obj);
        else if (obj instanceof Contact) return addContact((Contact) obj);
        else if(obj instanceof Account) return addAccount((Account) obj);
        return null;
    }
    public boolean existsObject(String id){
        if(id.startsWith("L")) return leadMap.containsKey(id);
        else if (id.startsWith("O")) return opportunityMap.containsKey(id);
        else if (id.startsWith("C")) return contactMap.containsKey(id);
        return accountMap.containsKey(id)||userList.containsKey(id);
    }
    public boolean isEmptyMap(Class<? extends Printable> objType){
        return switch (objType.getSimpleName()){
            case "Account" -> accountMap.isEmpty();
            case "Opportunity" -> opportunityMap.isEmpty();
            case "Lead" -> leadMap.isEmpty();
            case "Contact" -> contactMap.isEmpty();
            case "User" -> userList.isEmpty();
            default ->
                    throw new RuntimeException("FFFFFFAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIL"+objType.getSimpleName());

        };
    }
}
