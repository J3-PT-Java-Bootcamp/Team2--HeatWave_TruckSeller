package com.ironhack.CRMManager;

import com.ironhack.Commercial.Lead;
import com.ironhack.Commercial.Opportunity;
import com.ironhack.Commercial.Printable;
import com.ironhack.ScreenManager.Text.TextObject;
import lombok.Data;

import java.util.ArrayList;

@Data
public class User implements Printable {
    private String name;
    private String password;
    private ArrayList<String> opportunityList,leadList,recentObjects;
    private final boolean isAdmin;


    public User(String name, String password, boolean isAdmin) {
        this.name = name;
        this.isAdmin=isAdmin;
        this.password = password;
        this.opportunityList=new ArrayList<>();
        this.recentObjects=new ArrayList<>();
        this.leadList=new ArrayList<>();
    }


    public void addToOpportunityList(Opportunity opportunity){
       addToOpportunityList(opportunity.getId());
    }
    public void addToOpportunityList(String opportunityID){
        opportunityList.add(opportunityID);
    }

    public void addToLeadList(String leadID){
        leadList.add(leadID);
    }
    public void addToLeadList(Lead lead){
        addToLeadList(lead.getId());
    }
    public int getOpportunityListSize(){
       return this.opportunityList.size();
    }


    public int getLeadListSize(){
        return this.leadList.size();
    }

    public void removeFromLeadList(String id){
        leadList.remove(id);
    }
    public void addToRecentObjects(String id){
        if(this.recentObjects.size()>=10){
            recentObjects.remove(0);
        }else {
            recentObjects.add(id);
        }
    }
    public boolean isAdmin() {
        return this.isAdmin;
    }

    @Override
    public TextObject toTextObject() {
        //TODO
        return null;
    }

    @Override
    public String shortPrint() {
        //TODO
        return null;
    }

    @Override
    public TextObject printFullObject() {
        //TODO
        return new TextObject();
    }

    @Override
    public String[] getPrintableAttributes() {
        return new String[0];
    }
}
