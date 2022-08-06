package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.Exceptions.NoCompleteObjectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContactBuilderTest {
    @BeforeEach
    @Test
    void constructContact_test_1() throws NoCompleteObjectException {
            new CRMManager(false);
            var contactOne = new ContactBuilder();
            contactOne.setName("Salvatore");
            contactOne.setPhoneNumber("672168089");
            contactOne.setMail("beatrizip_12@gmail.com");
            contactOne.setCompany("Cocacola");

        var finalContact= contactOne.constructContact();
        assertEquals("Salvatore",finalContact.getName());
        }
    }
