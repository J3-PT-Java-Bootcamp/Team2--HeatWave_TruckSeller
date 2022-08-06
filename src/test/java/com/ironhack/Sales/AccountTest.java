package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Constants.IndustryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountTest {
    @BeforeEach
    void setUp() {
        new CRMManager(false);

    }

    @Test
    void getCompanyName_test() {
        var acc= new Account(IndustryType.MEDICAL,39,"BCN","Spain","CocaCola");
        assertEquals("CocaCola",acc.getCompanyName());
    }

}