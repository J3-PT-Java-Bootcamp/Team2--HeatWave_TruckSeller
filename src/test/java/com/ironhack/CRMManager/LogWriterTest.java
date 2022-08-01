package com.ironhack.CRMManager;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LogWriterTest {

    @Test
    void logError() throws FileNotFoundException {
        LogWriter.logError("ERR","ERR","$&&$");
        Scanner reader;
        reader = new Scanner(new FileReader(new File("data/errorLog.txt")));
        String res="";
        while (reader.hasNextLine())res=reader.nextLine();
        assertTrue(res.contains("$&&$"));
    }
}