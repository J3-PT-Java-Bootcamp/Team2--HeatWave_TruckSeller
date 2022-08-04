package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Constants.IndustryType;
import com.ironhack.Constants.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ironhack.CRMManager.CRMManager.printer;
import static org.junit.jupiter.api.Assertions.*;

class ContactTest {

    CRMManager crm;
    @BeforeEach
    void setUp() {
        crm = new CRMManager(false);

    }
    @Test
    void getName_test() {
        var con= new Contact("trixoide","+36 67318909", "ironhackers.gmail.com","Nike");
        assertEquals("trixoide",con.getName());
    }


}