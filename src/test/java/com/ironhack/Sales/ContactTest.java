package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.ScreenManager.InputReader;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Constants.IndustryType;
import com.ironhack.Constants.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.CRMManager.CRMManager.printer;
import static com.ironhack.CRMManager.ScreenManager.InputReader.toCamelCase;
import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    Contact cont;
    @BeforeEach
    void setUp() {
        new CRMManager(false);
        cont= new Contact("bea","+34672511","heavy@gmail.com","Adidas");
        crmData.addContact(cont);

    }
    @Test
    void getName_test() {
        var con= new Contact("trixoide","+36 67318909", "ironhackers.gmail.com","Nike");
        assertEquals("trixoide",con.getName());
    }
    @Test
    void test_textObject(){
        assertEquals(5,cont.toTextObject().getTotalHeight() );
    }

    @Test
    void test_String(){
        assertNotNull(cont.shortPrint());
    }

    @Test
    void test_printFullObject(){
        assertEquals(5,cont.printFullObject().getTotalHeight() );
    }

    @Test
    void test_getPrintableAttributes(){
        assertEquals(cont.toTextObject().getTotalHeight(),cont.getPrintableAttributes().length );
    }

}