package com.ironhack.CRMManager;

import com.google.gson.Gson;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Sales.*;

import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ironhack.Constants.Constants.MAX_ID;


public class CRMData implements Printable{
    private int leadCounter,opportunityCounter,accountCounter,contactCounter;
    private final HashMap<String, Lead> leadMap;
    private final HashMap<String, Opportunity> opportunityMap;
    private final HashMap<String, Account> accountMap;

    private final HashMap<String, Contact> contactMap;
    private final HashMap<String, User> userList;
    
    public CRMData(){
        leadCounter=0;
        opportunityCounter=0;
        leadMap =new HashMap<>();
        opportunityMap =new HashMap<>();
        contactMap=new HashMap<>();
        accountMap=new HashMap<>();
        userList=new HashMap<>();
    }

    //---------------------------------------------------------------------------------------------------GETTERSnSETTERS
    public  String getNextID(Class objClass){
        var sb= new StringBuilder();
        if (Opportunity.class.equals(objClass)) {
            opportunityCounter++;
            sb.append("P").append(Integer.toHexString(Integer.parseInt(MAX_ID, 16) - opportunityCounter));
        } else if (Lead.class.equals(objClass)) {
            leadCounter++;
            sb.append("L").append(Integer.toHexString(Integer.parseInt(MAX_ID, 16) - leadCounter));
        } else if (Contact.class.equals(objClass)) {
            contactCounter++;
            sb.append("C").append(Integer.toHexString(Integer.parseInt(MAX_ID, 16) - contactCounter));
        }
        return sb.toString().toUpperCase();


    }
      public HashMap<String, User> getUserList() {
        return userList;
    } 
     public boolean addToUserList(User newUser){
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

    public List<User> getUsers(boolean includeAdmin) {
        if(includeAdmin) return getUserList().values().stream().toList();

        var resArr= new ArrayList<User>();
        for (User user:userList.values()) {
            if (!user.isAdmin())resArr.add(user);
        }
        return resArr;
    }
    public CRMData removeContact(String id){
        this.contactMap.remove(id);
        return this;}


    //----------------------------------------------------------------------------------------------------UNKNOWN OBJECT
    public Printable getUnknownObject(String id) {
        Printable val=null;
        if(id.startsWith("L")) {
            val= leadMap.get(id);
        } else if (id.startsWith("P")) {
            val= opportunityMap.get(id);
        } else if (id.startsWith("C")) {
            val= contactMap.get(id);
        }
        if (val!=null)return val;
        return accountMap.get(id);
    }
    public void removeUnknownObject(String id){
        Printable val=null;
        if(id.startsWith("L")) {
           val= leadMap.remove(id);
        } else if (id.startsWith("P")) {
            val= opportunityMap.remove(id);
        } else if (id.startsWith("C")) {
            val= contactMap.remove(id);
        }
        if (val==null) accountMap.remove(id);
        removeAccount(id);
    }
    public CRMData addUnknownObject(Printable obj){
        if(obj instanceof Lead) return addLead((Lead)obj);
        else if (obj instanceof Opportunity) return addOpportunity((Opportunity) obj);
        else if (obj instanceof Contact) return addContact((Contact) obj);
        else if(obj instanceof Account) return addAccount((Account) obj);
        return null;
    }
    public boolean existsObject(String id){
        boolean firstCheck = false;
        if(id.startsWith("L")) firstCheck= leadMap.containsKey(id);
        else if (id.startsWith("P")) firstCheck= opportunityMap.containsKey(id);
        else if (id.startsWith("C")) firstCheck= contactMap.containsKey(id);
        return firstCheck||accountMap.containsKey(id)||userList.containsKey(id);
    }
    public boolean isEmptyMap(Class<? extends Printable> objType){
        return switch (objType.getSimpleName()){
            case "Account" -> accountMap.isEmpty();
            case "Opportunity" -> opportunityMap.isEmpty();
            case "Lead" -> leadMap.isEmpty();
            case "Contact" -> contactMap.isEmpty();
            case "User" -> userList.isEmpty();
            default ->
                    LogWriter.logError(getClass().getSimpleName(),
                            "isEmptyMap",
                            "Unexpected objType class.. "+objType.getSimpleName())==null;
        };
    }

    public static CRMData loadData() throws Exception {
        Gson sessionGson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get("data/SessionData.json"));
        CRMData crmData = sessionGson.fromJson(reader, CRMData.class);

        return crmData;

        //TODO LOAD FULL CRMData object from json and aSsign it to crmData field

    }

    public static void saveData() throws Exception {
        Gson sessionGson = new Gson();
        var writer= new FileWriter("data/SessionData.json", false);
        writer.write(sessionGson.toJson(CRMManager.crmData));
        writer.close();
        //TODO Save crmData object to .json file in ./data

    }

    public User getUser(String userName) {
        return userList.get(userName);
    }
    public void addUser(User user){
        this.userList.put(user.getName(), user);
    }
    public void removeUser(String userName){
        this.userList.remove(userName);
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public TextObject toTextObject() {
        return new TextObject("Global");//TODO
    }

    @Override
    public String shortPrint() {
        return "Global";
    }

    @Override
    public TextObject printFullObject() {
        return new TextObject("Global Stats.");//TODO
    }

    @Override
    public String[] getPrintableAttributes() {
        return new String[]{"Name"};
    }
}
