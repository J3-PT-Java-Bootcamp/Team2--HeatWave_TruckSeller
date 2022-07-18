package com.ironhack.CRMManager;

import com.ironhack.Commercial.Lead;
import com.ironhack.Commercial.Opportunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;
    private String password;
    private HashMap<String, Opportunity> opportunityList;
    private HashMap<String, Lead> leadList;



    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addToOpportunityList(Opportunity opportunity){
       opportunityList.put(opportunity.getId(), opportunity);
    }
    public void addToLeadList(Lead lead){
        leadList.put(lead.getId(), lead);
    }
    public int getOpportunityListSize(){
       return this.opportunityList.size();
    }

    public HashMap<String, Opportunity> getOpportunityList() {
        return opportunityList;
    }

    public HashMap<String, Lead> getLeadList() {
        return leadList;
    }

    public int getLeadlistSize(){
        return this.leadList.size();
    }

    public void removeFromLeadlist(String id){
        leadList.remove(id);
    }
}
