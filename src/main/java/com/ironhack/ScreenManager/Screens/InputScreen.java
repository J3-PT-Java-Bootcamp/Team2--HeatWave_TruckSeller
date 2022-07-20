package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Exceptions.*;
import com.ironhack.ScreenManager.*;
import com.ironhack.ScreenManager.Text.*;

import static com.ironhack.Constants.Constants.*;
import static com.ironhack.ScreenManager.InputReader.*;

/**
 * InputScreen is a CRMScreen that prompts 1..* inputs provided by inputTypes/inputNames
 * results are stored in "outValues"
 */
public class InputScreen extends CRMScreen{
    InputReader[] inputTypes;//Type of data that will prompt to user, ordered
    String[] inputNames;//Names of data that will prompt to user, ordered
    java.util.ArrayList<String>outValues;//User answers 
    TextObject content;//Text Content
    
    //-------------------------------------------------------------------------------------------------------CONSTRUCTOR
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
    public String start() throws com.ironhack.Exceptions.CRMException {
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
                if(this.crmManager.showModalScreen("Confirmation Needed",
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
        return printOutValues();
    }

    private String printOutValues() {
        var sb= new StringBuilder();
        for (int j = 0; j < outValues.size(); j++) {
            sb.append(inputNames[j]);
            sb.append( ":  ");
            sb.append( (inputTypes[j].equals(NEW_PASSWORD)||inputTypes[j].equals(PASSWORD)?"*"
                    .repeat(outValues.get(j).length()):outValues.get(j)));
            sb.append('\n');
        }
        return sb.toString();
    }

    private void constructContent() {
        addText(content);
        for (int j = 0; j < outValues.size(); j++) {
            addText(inputNames[j]+ ":  "
                    + (inputTypes[j].equals(NEW_PASSWORD)||
                    inputTypes[j].equals(PASSWORD)?"*"
                    .repeat(outValues.get(j).length()):outValues.get(j)));
        }
    }


    @Override
    public void checkCommandInput() {
    }

    public java.util.ArrayList<String> getValues() {
        return this.outValues;
    }
}
