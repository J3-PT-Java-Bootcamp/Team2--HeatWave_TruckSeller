package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContactTest {

    @BeforeEach
    void setUp() {
        new CRMManager(false);

    }
    @Test
    void getName_test() {
        var con= new Contact("trixoide","+36 67318909", "ironhackers.gmail.com","Nike");
        assertEquals("trixoide",con.getName());
    }


}