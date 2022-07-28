package com.ironhack.Commercial;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Constants.IndustryType;
import com.ironhack.Exceptions.NoCompleteObjectException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ContactBuilderTest {
    @BeforeEach
    @Test
    void constructContact_test_1() throws NoCompleteObjectException {
            var crm=new CRMManager(false);
            var contactOne = new ContactBuilder();
            contactOne.setName("Salvatore");
            contactOne.setId("EDFFR");
            contactOne.setPhoneNumber("672168089");
            contactOne.setMail("beatrizip_12@gmail.com");
            contactOne.setCompany("Cocacola");

        var finalContact= contactOne.constructContact();
        assertEquals("Salvatore",finalContact.getName()); //TODO TEST INDUSTRYTYPE no lagafa
        }
    }