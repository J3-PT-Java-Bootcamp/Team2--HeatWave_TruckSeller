package com.ironhack.CRMManager.ScreenManager.Text;

import com.ironhack.Constants.ColorFactory.BgColors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.ironhack.Constants.ColorFactory.CColors.BRIGHT_PURPLE;
import static com.ironhack.Constants.ColorFactory.CColors.BRIGHT_RED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WindowObjectTest {
    WindowObject txtObj;
    @BeforeEach
    void setUp() {
        txtObj=new WindowObject(100,20,1,1);
        txtObj.addText("0").addText("1").addText("2");
    }
    @Test
    void getTextModifications_test() {
        txtObj.setTxtColor(BRIGHT_PURPLE);
        assertTrue(txtObj.getTextModifications().contains(BRIGHT_PURPLE.toString()));
    }

    @Test
    void setTitle() {
        txtObj.setTitle("PATATA");
        assertEquals("PATATA",txtObj.title);
    }

    @Test
    void setTitleColor() {
        txtObj.setTitle("PATATEST");
        txtObj.setTitleColor(BRIGHT_RED);
        assertTrue(txtObj.print().contains(String.valueOf(BRIGHT_RED)));
    }

    @Test
    void setFrameColor() {
        txtObj.setFrameColor(BgColors.BRIGHT_GREEN);
        assertTrue(txtObj.print().contains(BgColors.BRIGHT_GREEN.toString()));
    }

    @Test
    void print_test_ok() {
        txtObj.alignTextCenter();
        assertTrue(txtObj.windowWidth*txtObj.windowHeight>txtObj.countValidCharacters(txtObj.print()));

    }
}