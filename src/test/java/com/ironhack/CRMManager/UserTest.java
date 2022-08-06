package com.ironhack.CRMManager;

import org.junit.jupiter.api.BeforeEach;

import static com.ironhack.CRMManager.CRMManager.crmData;

class UserTest {
    User user;
    @BeforeEach
    void setUp() {
        new CRMManager(false);
        user = crmData.getUser("USER");

    }

}