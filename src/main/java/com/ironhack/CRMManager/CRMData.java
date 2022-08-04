package com.ironhack.CRMManager;

import com.google.gson.Gson;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Constants.ColorFactory;
import com.ironhack.Sales.*;

import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;
import static com.ironhack.Constants.ColorFactory.CColors.RED;
import static com.ironhack.Constants.ColorFactory.SMART_RESET;
import static com.ironhack.Constants.ColorFactory.TextStyle.BOLD;
import static com.ironhack.Constants.Constants.MAX_ID;


public class CRMData implements Printable{
    private int leadCounter,opportunityCounter,accountCounter,contactCounter;
    private final HashMap<String, Lead> leadMap;
    private final HashMap<String, Opportunity> opportunityMap;
    private final HashMap<String, Account> accountMap;

    private final HashMap<String, Contact> contactMap;
    private final HashMap<String, User> userList;

    private int totalClosedLeads;
    private int totalLostOpps;
    private int totalSuccesOpps;
    
    public CRMData(){
        leadCounter=0;
        opportunityCounter=0;
        leadMap =new HashMap<>();
        opportunityMap =new HashMap<>();
        contactMap=new HashMap<>();
        accountMap=new HashMap<>();
        userList=new HashMap<>();
        totalClosedLeads=0;
        totalLostOpps=0;
        totalSuccesOpps=0;
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
     public void removeLead(String id){
        this.leadMap.remove(id);
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
    public void removeOpportunity(String id){
        this.opportunityMap.remove(id);
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

    public void removeAccount(String id) {
        this.accountMap.remove(id);
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
    public void removeContact(String id){
        this.contactMap.remove(id);
    }

    public int getTotalClosedLeads() {
        int totalClosedLeads = 0;
        for (int i=0; i < userList.size(); i++){
            totalClosedLeads = totalClosedLeads + userList.get(i).getClosedLeads();
        }
        return totalClosedLeads;
    }

    public int getTotalLostOpps() {
        int totalLostOpps = 0;
        for (int i=0; i < userList.size(); i++){
            totalLostOpps = totalLostOpps + userList.get(i).getLostOpp();
        }
        return totalLostOpps;
    }

    public int getTotalSuccesOpps() {
        int totalSuccesOpps =0;
        for (int i=0; i < userList.size(); i++){
            totalSuccesOpps = totalSuccesOpps + userList.get(i).getSuccessfulOpp();
        }
        return totalSuccesOpps;
    }



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
        if(userList.containsKey(id))return getUser(id);
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
    public void addUnknownObject(Printable obj){
        if(obj instanceof Lead) {
            addLead((Lead) obj);
        } else if (obj instanceof Opportunity) {
            addOpportunity((Opportunity) obj);
        } else if (obj instanceof Contact) {
            addContact((Contact) obj);
        } else if(obj instanceof Account) {
            addAccount((Account) obj);
        }
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
            default ->false;
        };
    }

    public static CRMData loadData() throws Exception {
        Gson sessionGson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get("data/SessionData.json"));
        CRMData crmData = sessionGson.fromJson(reader, CRMData.class);
        if (crmData==null) throw new Exception();
        return crmData;
    }

    public static void saveData() throws Exception {
        Gson sessionGson = new Gson();
        var writer= new FileWriter("data/SessionData.json", false);
        writer.write(sessionGson.toJson(CRMManager.crmData));
        writer.close();
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
        var globalStats = new TextObject();
        DecimalFormat df = new DecimalFormat("###.##");
        globalStats.addText(BLANK_SPACE)
                .addText("- LEADS- ")
                .addText("Pending: " + leadMap.size())
                .addText("Closed: " +BOLD+ leadObjectiveChecker(this.totalLeadRatioGetter()) + df.format(totalLeadRatioGetter())+ "%" + SMART_RESET)
                .addText("")
                .addText("- OPPORTUNITIES -")
                .addText("Pending: " + opportunityMap.size())
                .addText("Success Rate :"+BOLD+ oppObjectiveChecker(this.totalOppSuccessRatio()) + df.format(totalOppSuccessRatio()) + "%" + SMART_RESET)
                .addText(BLANK_SPACE)
                .addText("Overall Productivity : " +BOLD+ totalObjectiveChecker(this.totalOverallProductivityGetter()) + df.format(totalOverallProductivityGetter()) + "%" + SMART_RESET );


        return globalStats;//TODO
    }

    @Override
    public String[] getPrintableAttributes() {
        return new String[]{"Name"};
    }


    public double totalLeadRatioGetter(){ return ((getTotalClosedLeads())/(leadMap.size()+0.0))*100;}

    public double totalOppSuccessRatio(){ return ((getTotalSuccesOpps()+0.0)/(opportunityMap.size()+0.0))*100;}

    public double totalOverallProductivityGetter(){return ((totalLeadRatioGetter()+totalOppSuccessRatio())/200)*100;}

    public ColorFactory.CColors leadObjectiveChecker(double ratio){
        if (ratio < 50) return RED;
        else if (ratio >= 50 & ratio < 75) return ColorFactory.CColors.YELLOW;
        else return ColorFactory.CColors.GREEN;
    }

    public ColorFactory.CColors oppObjectiveChecker(double ratio){
        if(ratio < 15 ) return RED;
        else if(ratio >= 15 & ratio < 30 ) return ColorFactory.CColors.YELLOW;
        else if(ratio >= 30 & ratio < 50 ) return ColorFactory.CColors.GREEN;
        else if(ratio >= 50 & ratio < 75 ) return ColorFactory.CColors.BLUE;
        else  return ColorFactory.CColors.PURPLE;
    }

    public ColorFactory.CColors totalObjectiveChecker(double ratio){
        if (ratio < 50) return RED;
        else if (ratio >= 50 & ratio < 75) return ColorFactory.CColors.YELLOW;
        else return ColorFactory.CColors.GREEN;
    }
}
