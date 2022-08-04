package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMData;
import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Constants.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.ironhack.CRMManager.CRMManager.printer;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OpportunityTest extends TextObject {
    static CRMData crm;
    @BeforeAll
    static void setUp() {
        var crm=new CRMManager(false);
    }

    @Test
    void getId_test() {
        var opp= new Opportunity(Product.FLATBED,45,"Antonio","dsgfsf","me");
        assertEquals("PFFA",opp.getId());
    }

    @Test
    void getCompanyname_test() {
        var comp= new Opportunity(Product.FLATBED,234,"jose","yepala","Suricata");
        assertEquals("Suricata",comp.getAccount_companyName());
    }

   @Test
   void testprintable() {
     var op = new Opportunity(Product.BOX, 12, "abcd", "abcda", "hello").toTextObject();

        printer.sendToQueue(op);
        printer.startPrint();
    }

}