package com.ironhack.Commercial;

import com.ironhack.ScreenManager.Text.TextObject;
import lombok.experimental.SuperBuilder;


public interface Printable {

    TextObject toTextObject();
    String shortPrint();
    TextObject printFullObject();
     String[] getPrintableAttributes();

}
