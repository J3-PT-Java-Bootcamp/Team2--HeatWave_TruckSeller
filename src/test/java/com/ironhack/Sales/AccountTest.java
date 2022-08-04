package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Constants.IndustryType;
import com.ironhack.Constants.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    CRMManager crm;
    @BeforeEach
    void setUp() {
        crm = new CRMManager(false);

    }

    @Test
    void getCompanyName_test() {
        var acc= new Account(IndustryType.MEDICAL,39,"BCN","Spain","CocaCola");
        assertEquals("CocaCola",acc.getCompanyName());
    }

}