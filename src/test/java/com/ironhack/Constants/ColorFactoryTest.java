package com.ironhack.Constants;

import org.junit.jupiter.api.Test;

import static com.ironhack.Constants.ColorFactory.*;
import static com.ironhack.Constants.ColorFactory.TextStyle.RESET;
import static com.ironhack.Constants.ColorFactory.TextStyle.UNDERLINE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ColorFactoryTest {

    @Test
    void rainbowCharacters_test_ok() {
        var line = rainbowCharacters("a",3);
        assertTrue(containsSpecialCharacters(line));
    }

    @Test
    void isASpecialCharacter_test_ok() {
        assertTrue(isASpecialCharacter(COLOR_CHAR));
    }
    @Test
    void isASpecialCharacter_test_nok() {
        assertFalse(isASpecialCharacter('a'));
    }

    @Test
    void containsSpecialCharacters_test_ok() {
        assertTrue(containsSpecialCharacters(UNDERLINE+"PATATA"+RESET));
    }
    @Test
    void containsSpecialCharacters_test_nok() {
        assertFalse(containsSpecialCharacters("PATATA"));
    }
}