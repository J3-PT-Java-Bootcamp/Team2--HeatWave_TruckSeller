package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Constants.IndustryType;
import com.ironhack.Constants.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.CRMManager.ScreenManager.InputReader.toCamelCase;
import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    CRMManager crm;
    Account acc;
    @BeforeEach
    void setUp() {
        crm = new CRMManager(false);
        acc= new Account(IndustryType.MEDICAL, 30,"Orlando","Pakistan", "Reebok");
        crmData.addAccount(acc);
    }

    @Test
    void getCompanyName_test() {
        var acc= new Account(IndustryType.MEDICAL,39,"BCN","Spain","CocaCola");
        assertEquals("CocaCola",acc.getCompanyName());
    }

    @Test
    void test_textObject(){
        assertEquals(5,acc.toTextObject().getTotalHeight() );
    }

    @Test
    void test_String(){
        assertNotNull(acc.shortPrint());
    }

    @Test
    void test_printFullObject(){
        assertEquals(5,acc.printFullObject().getTotalHeight() );
    }

    @Test
    void test_getPrintableAttributes(){
        assertEquals(acc.toTextObject().getTotalHeight(),acc.getPrintableAttributes().length );
    }

}