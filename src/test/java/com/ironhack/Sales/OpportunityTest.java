package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Constants.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.CRMManager.CRMManager.printer;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OpportunityTest extends TextObject {
    @BeforeAll
    static void setUp() {
        new CRMManager(false);
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
        var cont = new Contact("ANTONIO","93456956","ANTONIO@APPLE.COM","ACC");
        crmData.addContact(cont);
     var op = new Opportunity(Product.BOX, 12, cont.getId(), "USER", "ACC").toTextObject();

        printer.sendToQueue(op);
        printer.startPrint();
    }

}