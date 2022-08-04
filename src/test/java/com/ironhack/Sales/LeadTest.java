package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Constants.IndustryType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeadTest {
    CRMManager crm;
    @BeforeEach
    void setUp() {
        crm = new CRMManager(false);

    }
    @Test
    void getId_test() {
        var led= new Lead("name","1234","78123713821","trucha@gmail.com","Adidas");
        assertEquals("1234",led.getId());
    }




}