package com.ironhack.Commercial;

import com.ironhack.CRMManager.CRMData;

import static org.junit.jupiter.api.Assertions.*;

class OpportunityTest extends com.ironhack.ScreenManager.Text.TextObject {
    static CRMData crm;
    @org.junit.jupiter.api.BeforeAll
    static void setUp() {
         crm = new CRMData();
    }

    @org.junit.jupiter.api.Test
    void getId_test() {
        String nextID = crm.getNextID(Opportunity.class);
        var opp= new Opportunity(nextID,null,1,null,null,null,null);
        System.out.println(opp.getId());
        assertEquals("OPFFE",opp.getId());
    }
    @org.junit.jupiter.api.Test
    void dummy() {
        var str="patata";
        System.out.println(str.contains(""));
    }

}