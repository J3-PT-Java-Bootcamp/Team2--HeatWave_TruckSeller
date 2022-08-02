package com.ironhack.CRMManager;

import com.ironhack.Constants.IndustryType;
import com.ironhack.Constants.Product;
import com.ironhack.Sales.Account;
import com.ironhack.Sales.Contact;
import com.ironhack.Sales.Lead;
import com.ironhack.Sales.Opportunity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static org.junit.jupiter.api.Assertions.*;

class CRMDataTest {
    CRMManager crm;
    @BeforeEach
    void setUp() {
        crm = new CRMManager(false);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void loadData_test() throws Exception {
        var data= CRMData.loadData();
        assertFalse(data.isEmptyMap(User.class));
    }
    @Test
    void saveData_test() throws Exception {
        CRMData.saveData();
//        assertFalse(data.isEmptyMap(User.class));
    }

    @Test
    void test_getNextID_ok() {
        assertEquals("PFFA",crmData.getNextID(Opportunity.class));
    }


    @Test
    void test_addToUserList_ok() {
        var lastLength = crmData.getUserList().size();
        crmData.addToUserList(new User("a","b",false));
        assertEquals(lastLength+1,crmData.getUserList().size());
    }

    @Test
    void test_addLead_ok() {
        var lastLength = crmData.getLeadsAsList().size();
        crmData.addLead(new Lead("a","b","93456456","asdas@asdf.dsf","acc"));
        assertEquals(lastLength+1,crmData.getLeadsAsList().size());
    }

    @Test
    void test_removeLead_ok() {
        var lastLength = crmData.getLeadsAsList().size();
        crmData.addLead(new Lead("a","b","93456456","asdas@asdf.dsf","acc"));
        crmData.removeLead("b");
        assertEquals(lastLength,crmData.getLeadsAsList().size());
    }

    @Test
    void test_getLead_ok() {
        crmData.addLead(new Lead("a","b","93456456","asdas@asdf.dsf","acc"));
        assertEquals("a",crmData.getLead("b").getName());
    }

    @Test
    void test_getOpportunity_ok() {
        var opp=new Opportunity(Product.FLATBED,45,"Antonio","dsgfsf","me");
        crmData.addOpportunity(opp);
        assertEquals("Antonio", crmData.getOpportunity(opp.getId()).getDecisionMakerID());
    }

    @Test
    void test_addOpportunity_ok() {
        var lastLength= crmData.getOpportunitiesAsList().size();
        var opp=new Opportunity(Product.FLATBED,45,"Antonio","dsgfsf","me");
        crmData.addOpportunity(opp);
        assertEquals(lastLength+1,crmData.getOpportunitiesAsList().size());
    }

    @Test
    void test_removeOpportunity_ok() {
        var lastLength= crmData.getOpportunitiesAsList().size();
        var opp=new Opportunity(Product.FLATBED,45,"Antonio","dsgfsf","me");
        crmData.addOpportunity(opp);
        crmData.removeOpportunity(opp.getId());
        assertEquals(lastLength,crmData.getOpportunitiesAsList().size());
    }

    @Test
    void test_getAccount_ok() {
        var acc= new Account(IndustryType.MEDICAL,9889,"Oklahoma","India","ABC");
        crmData.addAccount(acc);
        assertNotNull(crmData.getAccount("ABC"));
    }

    @Test
    void test_addAccount_ok() {
        var lastLength= crmData.getAccountsAsList().size();
        crmData.addAccount(new Account(IndustryType.MEDICAL,9889,"Oklahoma","India","ABCDEF"));
        assertEquals(lastLength+1,crmData.getAccountsAsList().size());

    }

    @Test
    void test_removeAccount_ok() {
        var lastLength= crmData.getAccountsAsList().size();
        crmData.addAccount(new Account(IndustryType.MEDICAL,9889,"Oklahoma","India","gh"));
        crmData.removeAccount("gh");
        assertEquals(lastLength,crmData.getAccountsAsList().size());
    }

    @Test
    void test_getContact_ok() {
        var cont= new Contact("Antonio","93456456","antonio@cocacola.es","Coca-Cola");
        crmData.addContact(cont);
        assertNotNull(crmData.getContact(cont.getId()));

    }

    @Test
    void test_addContact_ok() {
        var cont= new Contact("Antonia","93456456","antonio@cocacola.es","Coca-Cola");
        crmData.addContact(cont);
        assertEquals("Antonia",crmData.getContact(cont.getId()).getName());
    }

    @Test
    void test_getUsers_withAdmin_ok() {
        boolean areAdmin=false;
        var list = crmData.getUsers(true);
        for(User user: list)if(user.isAdmin())areAdmin=true;
        assertTrue(areAdmin);
    }
    @Test
    void test_getUsers_withoutAdmin_ok() {
        boolean areAdmin=false;
        var list = crmData.getUsers(false);
        for(User user: list)if(user.isAdmin())areAdmin=true;
        assertFalse(areAdmin);
    }

    @Test
    void test_removeContact_ok() {
        var cont= new Contact("PEPE","93456456","antonio@cocacola.es","Coca-Cola");
        crmData.addContact(cont);
        crmData.removeContact(cont.getId());
        assertNull(crmData.getContact(cont.getId()));
    }

    @Test
    void test_getUnknownObject_ok() {
        crmData.addLead(new Lead("a","Lb","93456456","asdas@asdf.dsf","acc"));
        assertNotNull(crmData.getUnknownObject("Lb"));
    }

    @Test
    void test_removeUnknownObject_ok() {
        var lastLength = crmData.getLeadsAsList().size();
        crmData.addLead(new Lead("a","Lb","93456456","asdas@asdf.dsf","acc"));
        crmData.removeUnknownObject("Lb");
        assertEquals(lastLength,crmData.getLeadsAsList().size());
    }

    @Test
    void test_addUnknownObject_ok() {
        var lastLength = crmData.getLeadsAsList().size();
        crmData.addUnknownObject(new Lead("a","Lb","93456456","asdas@asdf.dsf","acc"));
        assertEquals(lastLength+1,crmData.getLeadsAsList().size());
    }

    @Test
    void test_existsObject_true() {
        crmData.addLead(new Lead("a","Lb","93456456","asdas@asdf.dsf","acc"));
        assertTrue(crmData.existsObject("Lb"));
    }

    @Test
    void test_existsObject_false() {
        assertFalse(crmData.existsObject("khgjgh"));
    }

    @Test
    void test_isEmptyMap_false() {
        crmData.addOpportunity(new Opportunity());
        assertFalse(crmData.isEmptyMap(Opportunity.class));
    }

    @Test
    void test_isEmptyMap_true() {
        for(Account account: crmData.getAccountsAsList()){
            crmData.removeAccount(account.getCompanyName());
        }
        assertFalse(crmData.isEmptyMap(Opportunity.class));
    }

}