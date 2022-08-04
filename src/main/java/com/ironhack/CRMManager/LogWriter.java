package com.ironhack.CRMManager;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class LogWriter {
    private static final FileWriter writer;

    static {
        try {
            writer = new FileWriter("data/errorLog.txt",true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void logError(String className,String methodName,String notes){
        try {
            writer.write(getDateTime()+" //-> "+className+"."+methodName+"() : "+notes+"\n");
            writer.close();
        } catch (IOException ignored) {
        }
    }

    private static String getDateTime() {
        return ">"+LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))+
               " "+ LocalDateTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM));
    }

}
