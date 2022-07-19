package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Exceptions.CRMException;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.InputReader;
import com.ironhack.ScreenManager.Text.DynamicLine;
import com.ironhack.ScreenManager.Text.TextObject;

import static com.ironhack.Constants.Constants.LIMIT_X;

public class InputScreen extends CRMScreen{
    InputReader[] inputTypes;
    String[] inputNames;
    java.util.ArrayList<String>outValues;
    TextObject content;
    public InputScreen(CRMManager manager,
                       ConsolePrinter printer,
                       String name,
                       TextObject content,
                       String[] inputNames,
                       InputReader... inputTypesOrdered) {
        super(manager,printer,name);
        this.content=content;
        this.inputTypes=inputTypesOrdered;
        this.inputNames=inputNames;
        this.outValues=new java.util.ArrayList<>();
        addText(content);


    }

    @Override
    public TextObject print() throws com.ironhack.Exceptions.CRMException {
        int i=0;
        String input="";
        do  {
            printer.clearScreen();
            getTextObject().alignTextCenter();
            printer.sendToQueue(this.getTextObject());
            printer.sendToQueue(new DynamicLine(LIMIT_X/2,1,0).addText(inputNames[i]+": ").alignTextCenter());
            printer.startPrint();
            try {
               input =inputTypes[i].getInput(this,printer);
            }catch (CRMException crmException){
                handleBackExceptions(crmException);
                break;
            }catch (Exception e){
                throw new RuntimeException(e);
            }
            this.outValues.add(input);
            addText(content);
            for (int j = 0; j < outValues.size(); j++) {
                addText(inputNames[j]+ ":  "+ outValues.get(j));
            }
            input="";
            i++;
        }while(i < inputTypes.length);
        printer.clearScreen();
        addText("Thanx!");
        getTextObject().alignTextCenter();
        printer.sendToQueue(this.getTextObject());
        printer.startPrint();
        return null;
    }



    @Override
    public void checkCommandInput() {
    }

    public java.util.ArrayList<String> getValues() {
        return this.outValues;
    }
}
