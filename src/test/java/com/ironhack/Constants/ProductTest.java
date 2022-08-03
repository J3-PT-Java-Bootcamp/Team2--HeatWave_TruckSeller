package com.ironhack.Constants;

import com.ironhack.CRMManager.Exceptions.WrongInputException;
import org.junit.jupiter.api.Test;

import static com.ironhack.Constants.Product.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {


    @Test
    void checkAllProducts_test_ok() throws WrongInputException {
        assertEquals(FLATBED,Product.checkAllProducts("FLAT"));
    }
    @Test
    void checkAllProducts_test_nok() {
        assertThrows(WrongInputException.class,()->Product.checkAllProducts("PATATA"));
    }
}