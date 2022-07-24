package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Commercial.Account;
import com.ironhack.Commercial.Lead;
import com.ironhack.Commercial.Opportunity;
import com.ironhack.*;
import com.ironhack.Commercial.Printable;
import com.ironhack.Constants.ColorFactory;
import com.ironhack.Constants.ColorFactory.BgColors;
import com.ironhack.Exceptions.*;
import com.ironhack.ScreenManager.Text.TextObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ironhack.Constants.ColorFactory.TextStyle.RESET;
import static com.ironhack.Constants.ColorFactory.TextStyle.UNDERLINE;
import static com.ironhack.ScreenManager.InputReader.COMMAND;
import static com.ironhack.ScreenManager.Screens.Commands.*;

public class TableScreen extends CRMScreen{
    int pages,currentPage;
    Class<? extends Printable> type;
    ArrayList<List<? extends Printable>> masterArr;

    public TableScreen(CRMManager manager, String name, ArrayList<? extends Printable> data) {
        super(manager, manager.getPrinter(), name);
        masterArr = getLists(data);
        addCommand(NEXT).addCommand(PREVIOUS);
        if(!data.isEmpty()) {
            var tClass= data.get(0).getClass();
            if (Opportunity.class.equals(tClass)) {
                addCommand(CLOSE).addCommand(VIEW);
            } else if (Lead.class.equals(tClass)) {
                addCommand(CONVERT).addCommand(DISCARD);
            } else if (Account.class.equals(tClass)) {
                addCommand(VIEW).addCommand(DISCARD);
            } else {
                throw new IllegalStateException("Unexpected value: " + data.get(0).getClass());
            }
        }
        constructScreen();
    }
//----------------------------------------------------------------------------------------------------------CONSTRUCTION
    public void constructScreen() {
        try {
            constructTable(getMaxWidth(), masterArr.get(currentPage)
                    , new String[]{"ID", "Name", "Phone","Mail", "Company"},
                    ColorFactory.BgColors.BLUE,
                    ColorFactory.BgColors.PURPLE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        constructLastLine();
    }

    private void constructLastLine() {
        textObject.addText(new TextObject(TextObject.Scroll.NO,textObject.MAX_WIDTH,textObject.MAX_HEIGHT)
                .addGroupInColumns(4, textObject.MAX_WIDTH, new TextObject[]{
                        new TextObject(),
                        new TextObject(currentPage > 0 ? "[ PREVIOUS ]" : "", getMaxWidth() / 4, 1),
                        new TextObject((currentPage+1) + "/" + pages, getMaxWidth() / 4, 1),
                        new TextObject(currentPage+1 < pages ? "[ NEXT ]" : "", getMaxWidth() / 4, 1)
        }));
    }

    private ArrayList<List<? extends Printable>> getLists(ArrayList<? extends Printable> data) {
        ArrayList<List<? extends Printable>> masterArr= new java.util.ArrayList<>();
        pages= (int) Math.floor(data.size()/15)+(data.size()%15==0?0:1);
        int lastIndex=0;
        for (int i = 0; i < pages; i++) {
            masterArr.add(data.subList(lastIndex, Math.min((i+1)*15, data.size())));
            lastIndex=  Math.min((i+1)*15, data.size());
        }
        return masterArr;
    }

    public void constructTable(int totalSize, List<? extends Printable> tableEntries, String[] columnTitles, BgColors... colors) throws Exception {
        //Calculate min size for each column and total lenght for each line
        int [] totalLineSize=new int[tableEntries.size()];
        int[] columnsMinSize=new int[columnTitles.length];
        boolean fits=true;
        for (int i = 0; i < tableEntries.size(); i++) {
            var currentTxtObj= tableEntries.get(i).toTextObject();
            for (int j = 0; j < currentTxtObj.getTotalHeight(); j++) {
                try {
                    columnsMinSize[j]=Math.max(columnsMinSize[j], textObject.countValidCharacters(currentTxtObj.get(j))+1);
                    totalLineSize[i]+=textObject.countValidCharacters(currentTxtObj.get(j))+1;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //Check if largest line fits in one line
        int totalMinSize=0;
        int largestFieldIndex=0;
        for (int i = 0; i < columnsMinSize.length; i++) {
            int val = columnsMinSize[i];
            totalMinSize += val;
            if (columnsMinSize[largestFieldIndex] < val) largestFieldIndex=i;
        }
        fits=totalMinSize<=totalSize;
        //Solve field too long
        if(!fits){
            int restLength=0;
            for (int k = 0; k < columnsMinSize.length; k++) {
                if (k != largestFieldIndex) restLength += columnsMinSize[k]+1;
            }
            columnsMinSize[largestFieldIndex]=totalSize-restLength-3;
        }
        int remainingSpace= Math.max(0,(totalSize-totalMinSize)/columnTitles.length-1);
        for (int i = -1; i < tableEntries.size(); i++) {
            var sb=new StringBuilder();
            var currentTxtObj= i>=0?tableEntries.get(i).toTextObject():null;
            if(i>=0)sb.append(colors[i%2==0?0:1]);
            for (int j = 0; j < columnTitles.length; j++) {
                String currentField="";
                if(i<0) currentField= UNDERLINE+columnTitles[j];
                else currentField=currentTxtObj.get(j);
                if(!fits&&j==largestFieldIndex&&i>=0){

                    sb.append(currentField, 0, columnsMinSize[j]-5);
                    sb.append("...");
                }else {
                    sb.append(
                            textObject.centerLine(currentField,columnsMinSize[j]+remainingSpace)
                            );
                }
                if(i>=0&&j<currentTxtObj.getTotalHeight()-1)sb.append("|");
                else if (i<0&&j<columnTitles.length-1)sb.append(" ");

            }
            if(i>=0)sb.append(RESET);
            addText(sb.toString());
        }

    }

    //----------------------------------------------------------------------------------------------------PUBLIC METHODS
    @Override
    public String start() {
        printer.clearScreen();
        printer.sendToQueue(getTextObject());
        printer.startPrint();
        String comm = "";
        try {
            comm = COMMAND.getInput(this, printer, commands.toArray(new Commands[0]));
            Commands command;
            if (Commands.valueOf(comm) == NEXT) {
                if (currentPage+1 < pages) currentPage++;

            } else if (Commands.valueOf(comm) == PREVIOUS) {
                if (currentPage > 0) currentPage--;
            } else if (Commands.valueOf(comm) == CONVERT) {
//                crmManager.convertLeadToOpp(Commands.valueOf(comm));
//                crmManager.crmData.getLead()
                //TODO
            }

        } catch (IllegalArgumentException e) {
            printer.showErrorLine(ErrorType.COMMAND_NOK);
        } catch (HelpException help) {
            //TODO SHOW ALL POSIBLE COMMANDS DEPENDING ON T TYPE
            printer.showHintLine("Available commands : ",commands.toArray(new Commands[0]));
        } catch (LogoutException logout) {
            if (this.crmManager.showModalScreen("Confirmation Needed",
                    "Do you want to logout?")) {
                crmManager.currentUser = null;
                return LOGOUT.name();
            }
        } catch (ExitException exit) {
            //If enter EXIT it prompts user for confirmation as entered data will be lost
            if (this.crmManager.showModalScreen("Confirmation Needed",
                    "Do you want to close app?")) {
                crmManager.currentUser = null;
                crmManager.exit = true;
                return EXIT.name();
            }
        } catch (GoBackException back) {
            //If enter EXIT it prompts user for confirmation as entered data will be lost
            if (currentPage > 0) currentPage--;
            else return MENU.name();
        } catch (GoToMenuException back) {
            //If enter EXIT it prompts user for confirmation as entered data will be lost
            return MENU.name();
        } catch (CRMException ignored) {
        }
        constructScreen();
        return start();
    }

}
