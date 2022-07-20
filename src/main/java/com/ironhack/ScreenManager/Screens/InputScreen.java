package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Exceptions.BackScreenInput;
import com.ironhack.Exceptions.CRMException;
import com.ironhack.Exceptions.HelpException;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.InputReader;
import com.ironhack.ScreenManager.Text.DynamicLine;
import com.ironhack.ScreenManager.Text.TextObject;

import static com.ironhack.Constants.Constants.LIMIT_X;
import static com.ironhack.ScreenManager.InputReader.*;

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
    public String print() throws com.ironhack.Exceptions.CRMException {
        int i=0;
        String input="";
        do  {
            printer.clearScreen();
            printer.sendToQueue(this.getTextObject());
            printer.sendToQueue(new DynamicLine(LIMIT_X/2,1,0).addText(inputNames[i]+": ").alignTextCenter());
            printer.startPrint();
            try {
                input = inputTypes[i].getInput(this, printer);
            }catch(BackScreenInput back) {
                //allow to go back to correct last input
                inputTypes[i].password = null;
                i--;
                outValues.remove(outValues.size() - 1);
                constructContent();
                input = "";
                continue;
            }catch (HelpException help){
                printer.showHintLine(help.hint,help.commands);
                constructContent();
                input="";
                continue;
            } catch (CRMException e){
                //If enter EXIT it prompts user for confirmation as entered data will be lost
                if(this.crmManager.showModal("Confirmation Needed",
                        "You have changes without save, are you sure you want to exit and lose them?")){
                    inputTypes[i].password=null;
                    throw e;
                }
                constructContent();
                input="";
                continue;
            }
            this.outValues.add(input);

            constructContent();
            input="";
            i++;
        }while(i < inputTypes.length);
        printer.clearScreen();
        getTextObject().alignTextCenter();
        printer.sendToQueue(this.getTextObject());
        printer.startPrint();
        return null;
    }

    private void constructContent() {
        addText(content);
        for (int j = 0; j < outValues.size(); j++) {
            addText(inputNames[j]+ ":  "+ (inputTypes[j].equals(NEW_PASSWORD)||inputTypes[j].equals(PASSWORD)?"*".repeat(outValues.get(j).length()):outValues.get(j)));
        }
    }


    @Override
    public void checkCommandInput() {
    }

    public java.util.ArrayList<String> getValues() {
        return this.outValues;
    }
}
