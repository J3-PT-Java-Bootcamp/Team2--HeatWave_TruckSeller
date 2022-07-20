package com.ironhack.Commercial;

import static org.junit.jupiter.api.Assertions.*;

class OpportunityTest extends com.ironhack.ScreenManager.Text.TextObject {

    @org.junit.jupiter.api.Test
    void getId_test() {
        var crm = new com.ironhack.CRMManager.CRMData();
        var opp= new Opportunity( crm,null,1,null,null,null);
        System.out.println(opp.getId());
        assertEquals("OPFFE",opp.getId());
    }
}