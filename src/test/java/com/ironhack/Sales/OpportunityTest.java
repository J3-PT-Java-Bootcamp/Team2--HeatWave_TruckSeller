package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMData;
import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Constants.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ironhack.CRMManager.CRMManager.printer;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OpportunityTest extends TextObject {
    static CRMData crm;
    @BeforeEach
    void setUp() {
        new CRMManager(false);
    }

    @Test
    void getId_test() {
        var opp= new Opportunity(Product.FLATBED,45,"Antonio","dsgfsf","me");
        assertEquals("OPFFE",opp.getId());
    }
    @Test
    void dummy() {
        var op= new Opportunity(Product.FLATBED,45,"Antonio","dsgfsf","me").toTextObject();
        CRMManager.
        printer.sendToQueue(op);
        printer.startPrint();
    }

}