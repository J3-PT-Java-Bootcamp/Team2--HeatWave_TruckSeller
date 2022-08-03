package com.ironhack.CRMManager;

import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Constants.ColorFactory;
import com.ironhack.Sales.Lead;
import com.ironhack.Sales.Opportunity;
import com.ironhack.Sales.Printable;
import lombok.Data;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.ironhack.CRMManager.ScreenManager.InputReader.OPEN;
import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;
import static com.ironhack.Constants.ColorFactory.CColors.RED;
import static com.ironhack.Constants.ColorFactory.SMART_RESET;
import static com.ironhack.Constants.ColorFactory.TextStyle.BOLD;

@Data
public class User implements Printable {
    private String name;
    private String password;
    private ArrayList<String> opportunityList,leadList,recentObjects;
    private final boolean isAdmin;
    private int closedLeads;
    private int lostOpp;
    private int successfulOpp;

    private int totalLeads;
    private int totalOpps;


    public User(String name, String password, boolean isAdmin) {
        this.name = name;
        this.isAdmin=isAdmin;
        this.password = password;
        this.opportunityList=new ArrayList<>();
        this.recentObjects=new ArrayList<>();
        this.leadList=new ArrayList<>();
        this.closedLeads = 0;
        this.lostOpp = 0;
        this.successfulOpp = 0;
        this.totalLeads = leadList.size() + this.closedLeads;
        this.totalOpps = opportunityList.size() + this.successfulOpp + this.lostOpp;
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
        this.setClosedLeads(+1);
    }
    public void addToRecentObjects(String id){
        if(this.recentObjects.size()>=10) recentObjects.remove(0);
        recentObjects.add(id);
    }
    public boolean isAdmin() {
        return this.isAdmin;
    }

    @Override
    public String getId() {
        return this.name;
    }

    @Override
    public TextObject toTextObject() {
        DecimalFormat df = new DecimalFormat("###.##");
        return new TextObject(getName()).addText(String.valueOf(getLeadListSize()))
                .addText(String.valueOf(getOpportunityListSize()))
                .addText(df.format(getSuccessfulOppRatio()) + " %" )
                .addText( df.format(getTotalProductivity()) + " %" );

    }

    @Override
    public String shortPrint() {
        return OPEN.formatOutput(getName());
    }

    @Override
    public TextObject printFullObject() {
        var userStats = new TextObject();
        this.updateTotals();
        DecimalFormat df = new DecimalFormat("###.##");
        userStats.addText(BLANK_SPACE)
                .addText("- LEADS- ")
                .addText("Pending: " + getLeadListSize())
                .addText("Closed: " +BOLD+ leadObjectiveChecker(this.getLeadRatio()) + df.format(getLeadRatio() )+ "%" + SMART_RESET)
                .addText("")
                .addText("- OPPORTUNITIES -")
                .addText("Pending: " + getOpportunityListSize())
                .addText("Success Rate :"+BOLD+ oppObjectiveChecker(this.getSuccessfulOppRatio()) + df.format(getSuccessfulOppRatio()) + "%" + SMART_RESET )
                .addText(BLANK_SPACE)
                .addText("Overall Productivity : " +BOLD+ totalObjectiveChecker(this.getTotalProductivity()) + df.format(getTotalProductivity()) + "%" + SMART_RESET );



        return userStats;
    }

    @Override
    public String[] getPrintableAttributes() {
        return new String[]{"Subj.","Leads","Opp.","Success","Productivity"};
    }


    private double getLeadRatio(){return ((getClosedLeads()+0.0)/(getTotalLeads()+0.0))*100;}
    private double getSuccessfulOppRatio(){return ((getSuccessfulOpp()+0.0)/(getTotalOpps()+0.0))*100;}
    private double getTotalProductivity(){return ((getLeadRatio()+getSuccessfulOppRatio())/200)*100;}


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

    private void updateTotals(){
        this.totalLeads = this.getLeadListSize() + this.getClosedLeads();
        this.totalOpps = this.getOpportunityListSize() + this.getSuccessfulOpp() + this.getLostOpp();
    }

    //This one is for the removeUnknownObject it only lets you set it as a lost Opp
    public void removeFromOpportunities(String id) {
        opportunityList.remove(id);
        this.setLostOpp(+1);

    }


    public void removeFromOpportunities(String id, boolean isSuccess) {
        opportunityList.remove(id);
        if (isSuccess){this.setSuccessfulOpp(+1);}
        else {this.setLostOpp(+1);}
    }

    public void removeUnknown(String id) {
        if(id.startsWith("L")) {
            removeFromLeadList(id);
        }
        else if (id.startsWith("P")) {
            removeFromOpportunities(id);
        }
    }
}
