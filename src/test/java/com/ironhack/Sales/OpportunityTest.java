package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMData;
import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Constants.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.CRMManager.CRMManager.printer;
import static com.ironhack.CRMManager.ScreenManager.InputReader.PRODUCT_TYPE;
import static com.ironhack.CRMManager.ScreenManager.InputReader.toCamelCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OpportunityTest extends TextObject {
     CRMData crm;
    Opportunity opp;

    @BeforeEach
    void setUp() {
        var crm=new CRMManager(false);
        var cont= new Contact("SALVA","+23892781","aijdasjn@gmail.com","ACC");
        crmData.addContact(cont);
        opp= new Opportunity(Product.FLATBED,10,cont.getId(),"dsgfsf","ACC");
        crmData.addOpportunity(opp);
    }
    @Test
    void getId_test() {
        var opp= new Opportunity(Product.FLATBED,45,"Antonio","dsgfsf","me");
        assertEquals("PFF9",opp.getId());
    }

    @Test
    void getCompanyname_test() {
        var comp= new Opportunity(Product.FLATBED,234,"jose","yepala","Suricata");
        assertEquals("Suricata",comp.getAccount_companyName());
    }

    @Test
    void test_textObject(){
        assertEquals(6,opp.toTextObject().getTotalHeight() );
    }

    @Test
    void test_String(){
        assertNotNull(opp.shortPrint());
    }

    @Test
    void test_printFullObject(){
        assertEquals(7,opp.printFullObject().getTotalHeight() );
    }

    @Test
    void test_getPrintableAttributes(){
        assertEquals(opp.toTextObject().getTotalHeight(),opp.getPrintableAttributes().length );
    }

}
