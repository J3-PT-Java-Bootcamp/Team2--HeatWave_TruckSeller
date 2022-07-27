package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Exceptions.CRMException;
import com.ironhack.Exceptions.ExitException;
import com.ironhack.Exceptions.HelpException;
import com.ironhack.Exceptions.LogoutException;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.InputReader;
import com.ironhack.ScreenManager.Text.DynamicLine;
import com.ironhack.ScreenManager.Text.TextObject;

import static com.ironhack.Constants.Constants.CLOSE_WITHOUT_SAVE;
import static com.ironhack.Constants.Constants.LIMIT_X;
import static com.ironhack.ScreenManager.InputReader.NEW_PASSWORD;
import static com.ironhack.ScreenManager.InputReader.PASSWORD;
import static com.ironhack.ScreenManager.Screens.Commands.EXIT;


/**
 * InputScreen is a CRMScreen that prompts 1..* inputs provided by inputTypes/inputNames
 * results are stored in "outValues"
 */
public class InputScreen extends CRMScreen{
    final InputReader[] inputTypes;//Type of data that will prompt to user, ordered
    final String[] inputNames;//Names of data that will prompt to user, ordered
    final java.util.ArrayList<String>outValues;//User answers
    final TextObject content;//Text Content
    private int inputIndex;
    
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
        this.inputIndex=0;
        constructScreen();

    }

    public void simulateInputs(String... inputs){

        for (String input: inputs){
            this.outValues.add(input);
            inputIndex++;
        }

    }
    @Override
    public String start() throws CRMException {
        String input="";
        do  {
            printer.clearScreen();
            printer.sendToQueue(this.getTextObject());
            printer.sendToQueue(new DynamicLine(LIMIT_X/2,
                    1,
                    0).addText(inputNames[inputIndex]+": ").alignTextCenter());
            printer.startPrint();
            try {
                input = inputTypes[inputIndex].getInput(this, printer);
            }catch(com.ironhack.Exceptions.GoBackException back) {
                //allow to go back to correct last input
                inputTypes[inputIndex].password = null;
                if (outValues.isEmpty()){
                    if (crmManager.currentUser == null) {
                        constructScreen();
                        continue;
                    }
//                    return
                    //TODO RETURN TO Table Screen
                }
                if (inputIndex > 0) inputIndex--;
                else return EXIT.name();
                if(!outValues.isEmpty()){
                outValues.remove(outValues.size() - 1);
                }
                constructScreen();
                input = "";
                continue;
            }catch (HelpException help){
                printer.showHintLine(help.hint,help.commands);
                constructScreen();
                input="";
                continue;
            }catch (LogoutException logout) {
                if (this.crmManager.currentUser != null){
                    if (this.crmManager.showModalScreen("Confirmation Needed",
                            new TextObject(CLOSE_WITHOUT_SAVE))) {
                        inputTypes[inputIndex].password = null;
                        crmManager.currentUser = null;
                        return EXIT.name();
                    }
                }else{
                    inputTypes[inputIndex].password = null;
                    constructScreen();
                    continue;
                }
            } catch (ExitException e){
                //If enter EXIT it prompts user for confirmation as entered data will be lost
                if(this.crmManager.showModalScreen("Confirmation Needed",
                        new TextObject(CLOSE_WITHOUT_SAVE))){
                    inputTypes[inputIndex].password=null;
                    crmManager.currentUser=null;
                    crmManager.exit=true;
                    return EXIT.name();
                }
                constructScreen();
                input="";
                continue;
            }
            this.outValues.add(input);
            constructScreen();
            input="";
            inputIndex++;
        }while(inputIndex < inputTypes.length);
        return printOutValues();
    }

    @Override
    public void constructScreen() {
        int maxHeight= textObject.MAX_HEIGHT-2;
        constructTitle(getName());
        var container= new TextObject(textObject.MAX_WIDTH,maxHeight)
                .addText(content).addText("-".repeat((textObject.MAX_WIDTH/3)*2)).setBgcolor(textObject.bgColor).setTxtColor(textObject.txtColor);
        for (int j = 0; j < outValues.size(); j++) {
            container.addText(inputNames[j]+ ":  "
                    + (inputTypes[j].equals(NEW_PASSWORD)||
                    inputTypes[j].equals(PASSWORD)?"*"
                    .repeat(outValues.get(j).length()):outValues.get(j)));
        }
        textObject.addText(container.alignTextMiddle()).alignTextTop(maxHeight);
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



    public java.util.ArrayList<String> getValues() {
        return this.outValues;
    }
}
