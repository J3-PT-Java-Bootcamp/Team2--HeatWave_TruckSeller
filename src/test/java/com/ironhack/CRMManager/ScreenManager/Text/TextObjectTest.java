package com.ironhack.CRMManager.ScreenManager.Text;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;
import static com.ironhack.Constants.ColorFactory.CColors.*;
import static com.ironhack.Constants.ColorFactory.SMART_RESET;
import static com.ironhack.Constants.ColorFactory.TextStyle.RESET;
import static com.ironhack.Constants.ColorFactory.TextStyle.UNDERLINE;
import static org.junit.jupiter.api.Assertions.*;

class TextObjectTest {
    TextObject txtObj;
    @BeforeEach
    void setUp() {
        txtObj=new TextObject(100,20);
        txtObj.addText("0").addText("1").addText("2");
    }

    @Test
    void poll_test_sizeOk() {
        var size=txtObj.getTotalHeight();
        txtObj.poll();
        assertTrue(size-1==txtObj.getTotalHeight());
    }
    @Test
    void poll_test_elementOk() {
        assertEquals("0",txtObj.poll());
    }

    @Test
    void addText_test_sizeOk() {
        var size=txtObj.getTotalHeight();
        assertTrue(size+1==txtObj.addText(BLANK_SPACE).getTotalHeight());
    }

    @Test
    void addGroupInColumns() {
    }

    @Test
    void countValidCharacters_test_ok() {
        assertEquals(8,txtObj.countValidCharacters(new StringBuilder().append(RED).append(BRIGHT_BLACK).append(UNDERLINE).append(" patata ")
                .append(SMART_RESET).append(RESET).toString()));
    }

    @Test
    void fillLine_test_ok() {
        assertEquals(txtObj.getMAX_WIDTH(), txtObj.countValidCharacters(txtObj.fillLine("a")));
    }

    @Test
    void centerLineWithoutColors_test_ok() {
        assertEquals(txtObj.getMAX_WIDTH(), txtObj.countValidCharacters(txtObj.centerLineWithoutColors("a",txtObj.getMAX_WIDTH())));
    }

    @Test
    void alignTextMiddle_test_ok() {
        assertEquals(txtObj.getMAX_HEIGHT()-1, txtObj.alignTextMiddle().getTotalHeight());
    }

    @Test
    void alignTextTop_test_ok() {
        assertEquals(txtObj.getMAX_HEIGHT(), txtObj.alignTextTop().getTotalHeight());
    }

    @Test
    void testAlignTextTop_test_ok() {
        assertEquals(800, txtObj.alignTextTop(800).getTotalHeight());
    }

    @Test
    void alignTextCenter_test_ok() {
        txtObj.alignTextCenter();
        boolean fail = false;
        for(String line:txtObj.text)if(line.length()!= txtObj.MAX_WIDTH)fail=true;
        assertFalse(fail);
    }

    @Test
    void getTextColorsModifiers_test_ok() {
        txtObj.setTxtColor(BRIGHT_PURPLE);
        assertTrue(txtObj.getTextColorsModifiers().contains(BRIGHT_PURPLE.toString()));
    }

    @Test
    void testToString_test_ok() {
        txtObj.alignTextCenter();
        assertEquals(txtObj.getMAX_WIDTH()*txtObj.getTotalHeight(),txtObj.countValidCharacters(txtObj.toString()));

    }

    @Test
    void fillAllLines_test_ok() {
        txtObj.fillAllLines();
        boolean fail = false;
        for(String line:txtObj.text)if(txtObj.countValidCharacters(line)!= txtObj.MAX_WIDTH)fail=true;
        assertFalse(fail);
    }

    @Test
    void print_test_ok() {
        txtObj.alignTextCenter();
        assertEquals(txtObj.getMAX_WIDTH()*txtObj.getTotalHeight(),txtObj.countValidCharacters(txtObj.print()));
    }
    @Test
    void smartReplaceReset_test_ok() {
        txtObj.addText(SMART_RESET);
        txtObj.smartReplaceReset();
        assertFalse(txtObj.print().contains(SMART_RESET));
    }
}