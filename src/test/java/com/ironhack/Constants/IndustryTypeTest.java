package com.ironhack.Constants;

import com.ironhack.CRMManager.Exceptions.WrongInputException;
import org.junit.jupiter.api.Test;

import static com.ironhack.Constants.IndustryType.ECOMMERCE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IndustryTypeTest {
    @Test
    void testToString() {
        assertEquals("e-Commerce",ECOMMERCE.toString());
    }

    @Test
    void checkAllIndustries_test_ok() throws WrongInputException {
        assertEquals(ECOMMERCE,IndustryType.checkAllIndustries("COMM"));
    }
    @Test
    void checkAllIndustries_test_nok() {
        assertThrows(WrongInputException.class,()->IndustryType.checkAllIndustries("patata"));
    }

}