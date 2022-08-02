package com.ironhack.CRMManager;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LogWriterTest {

    @Test
    void logError() throws FileNotFoundException {
        LogWriter.logError("ERR","ERR","$&&$");
        Scanner reader;
        reader = new Scanner(new FileReader("data/errorLog.txt"));
        var res=new StringBuilder();
        while (reader.hasNextLine())res.append(reader.nextLine());
        assertTrue(res.toString().contains("$&&$"));
    }
}