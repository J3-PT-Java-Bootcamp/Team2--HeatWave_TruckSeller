package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeadTest {
    @BeforeEach
    void setUp() {
        new CRMManager(false);

    }
    @Test
    void getId_test() {
        var led= new Lead("name","1234","78123713821","trucha@gmail.com","Adidas");
        assertEquals("1234",led.getId());
    }




}