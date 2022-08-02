package com.ironhack.Sales;

import com.ironhack.CRMManager.ScreenManager.Text.TextObject;


public interface Printable {
    String getId();
    TextObject toTextObject();
    String shortPrint();
    TextObject printFullObject();
     String[] getPrintableAttributes();

}
