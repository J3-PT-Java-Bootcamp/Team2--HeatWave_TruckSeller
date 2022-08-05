package com.ironhack.CRMManager;

import com.ironhack.CRMManager.Exceptions.GoBackException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static com.ironhack.CRMManager.CRMManager.adminOpManager;
import static com.ironhack.CRMManager.CRMManager.crmData;
import static org.junit.jupiter.api.Assertions.*;

class AdminOpManagerTest {
    CRMManager crm;
    @BeforeEach
    void setUp() {
        crm = new CRMManager(false);
        crm.setCurrentUser(crmData.getUser("ADMIN"));
    }

    @Test
    void loadLeadData_exceptionTest_ok() {
        System.setIn(new ByteArrayInputStream("back\n".getBytes()));
        assertThrows(GoBackException.class,()->adminOpManager.loadLeadData(crm.getCurrentUser()));
    }

    @Test
    void showStats_exceptionTest_ok() {
        System.setIn(new ByteArrayInputStream("back\n".getBytes()));
        assertDoesNotThrow(()->adminOpManager.showStats_screen(crm.getCurrentUser()));
    }

    @Test
    void manageUsers_exceptionTest_ok() {
        System.setIn(new ByteArrayInputStream("back\n".getBytes()));
        assertDoesNotThrow(()->adminOpManager.manageUsers_screen(crm.getCurrentUser()));
    }

    @Test
    void createNewUser_exceptionTest_ok() {
        System.setIn(new ByteArrayInputStream("back\n".getBytes()));
        assertThrows(GoBackException.class,()->adminOpManager.loadLeadData(crm.getCurrentUser()));
    }
}