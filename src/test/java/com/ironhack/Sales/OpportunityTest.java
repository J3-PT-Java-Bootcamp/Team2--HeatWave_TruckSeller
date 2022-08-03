package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMData;
import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Constants.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OpportunityTest extends TextObject {
    static CRMData crm;
    @org.junit.jupiter.api.BeforeAll
    static void setUp() {
        var crm=new CRMManager(false);
    }

    @Test
    void getId_test() {
        var opp= new Opportunity(Product.FLATBED,45,"Antonio","dsgfsf","me");
        assertEquals("PFFA",opp.getId());
    }


}