package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Constants.IndustryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.CRMManager.ScreenManager.InputReader.MAIL;
import static com.ironhack.CRMManager.ScreenManager.InputReader.toCamelCase;
import static org.junit.jupiter.api.Assertions.*;

class LeadTest {
    CRMManager crm;
    Lead led;
    @BeforeEach
    void setUp() {
        crm = new CRMManager(false);
        led= new Lead("bea","+34","92526272","Adidas","Ships");
        crmData.addLead(led);

    }
    @Test
    void getId_test() {
        var led= new Lead("name","1234","78123713821","trucha@gmail.com","Adidas");
        assertEquals("1234",led.getId());
    }

    @Test
    void test_textObject(){
        assertEquals(5,led.toTextObject().getTotalHeight() );
    }

    @Test
    void test_String(){
        assertNotNull(led.shortPrint());
    }

    @Test
    void test_printFullObject(){
        assertEquals(5,led.printFullObject().getTotalHeight() );
    }

    @Test
    void test_getPrintableAttributes(){
        assertEquals(led.toTextObject().getTotalHeight(),led.getPrintableAttributes().length );
    }

}


