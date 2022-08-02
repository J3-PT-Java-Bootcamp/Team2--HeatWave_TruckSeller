package com.ironhack.CRMManager.ScreenManager.Text;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DynamicLineTest {

    DynamicLine txtObj;
    @BeforeEach
    void setUp() {
        txtObj=new DynamicLine(100,20,1);
        txtObj.addText("0").addText("1").addText("2");
    }


    @Test
    void poll_test_sizeOk() {
        var size=txtObj.getTotalHeight();
        txtObj.poll();
        assertTrue(size-1==txtObj.getTotalHeight());
    }
    @Test
    void print_test_ok() {
        txtObj.alignTextCenter();
        assertEquals((txtObj.getMAX_WIDTH()*txtObj.getTotalHeight())/3,txtObj.countValidCharacters(txtObj.print()));
    }
}