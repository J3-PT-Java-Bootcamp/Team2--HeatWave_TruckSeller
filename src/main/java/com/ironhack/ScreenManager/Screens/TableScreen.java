package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Constants.ColorFactory;
import com.ironhack.Constants.ColorFactory.BgColors;
import com.ironhack.Exceptions.ErrorType;
import com.ironhack.ScreenManager.ConsolePrinter;
import com.ironhack.ScreenManager.Text.TextObject;

import java.util.ArrayList;
import java.util.List;

import static com.ironhack.Constants.ColorFactory.TextStyle.*;
import static com.ironhack.ScreenManager.InputReader.COMMAND;
import static com.ironhack.ScreenManager.Text.TextObject.*;

public class TableScreen<T> extends CRMScreen{
    int pages,currentPage;
    ArrayList<List<TextObject>> masterArr;

    public TableScreen(CRMManager manager, ConsolePrinter printer, String name, java.util.ArrayList<T> data) {
        super(manager, printer, name);
        var fakeData = new java.util.ArrayList<TextObject>(java.util.List.of(com.ironhack.FakeLead.getRawLeads(200)));
        masterArr = getLists(fakeData);
        addCommand(Commands.NEXT).addCommand(Commands.PREVIOUS);
        constructScreen();
    }

    private void constructScreen() {
        try {
            constructTable(getMaxWidth(), masterArr.get(currentPage).toArray(new TextObject[0])
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
                        new TextObject(currentPage + "/" + pages, getMaxWidth() / 4, 1),
                        new TextObject(currentPage < pages ? "[ NEXT ]" : "", getMaxWidth() / 4, 1)
        }));
    }

    private ArrayList<List<TextObject>> getLists(java.util.ArrayList<TextObject> fakeData) {
        ArrayList<List<TextObject>> masterArr= new java.util.ArrayList<>();
        pages= fakeData.size()/15 + (fakeData.size()%15==0?0:1);
        int lastIndex=0;
        for (int i = 0; i < pages; i++) {
            masterArr.add(fakeData.subList(lastIndex, Math.min((i+1)*15, fakeData.size())));
            lastIndex=  Math.min((i+1)*15, fakeData.size());
        }
        return masterArr;
    }

    public void constructTable(int totalSize, TextObject[] tableEntries, String[] columnTitles, BgColors... colors) throws Exception {
        //Calculate min size for each column and total lenght for each line
        int [] totalLineSize=new int[tableEntries.length];
        int[] columnsMinSize=new int[columnTitles.length];
        boolean fits=true;
        for (int i = 0; i < tableEntries.length; i++) {
            for (int j = 0; j < tableEntries[i].getTotalHeight(); j++) {
                try {
                    columnsMinSize[j]=Math.max(columnsMinSize[j], textObject.countValidCharacters(tableEntries[i].get(j))+1);
                    totalLineSize[i]+=textObject.countValidCharacters(tableEntries[i].get(j))+1;
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
        for (int i = -1; i < tableEntries.length; i++) {
            var sb=new StringBuilder();
            if(i>=0)sb.append(colors[i%2==0?0:1]);
            for (int j = 0; j < columnTitles.length; j++) {
                String currentField="";
                if(i<0) currentField= UNDERLINE+columnTitles[j];
                else currentField=tableEntries[i].get(j);
                if(!fits&&j==largestFieldIndex&&i>=0){

                    sb.append(currentField, 0, columnsMinSize[j]-5);
                    sb.append("...");
                }else {
                    sb.append(
                            textObject.centerLine(currentField,columnsMinSize[j]+remainingSpace)
                            );
                }
                if(i>=0&&j<tableEntries[i].getTotalHeight()-1)sb.append("|");
                else if (i<0&&j<columnTitles.length-1)sb.append(" ");

            }
            if(i>=0)sb.append(RESET);
            addText(sb.toString());
        }

    }


    @Override
    public String start()throws com.ironhack.Exceptions.CRMException {
        printer.clearScreen();
        printer.sendToQueue(getTextObject());
        printer.startPrint();
        String input = "";
        var comm= COMMAND.getInput(this, printer,commands.toArray(new Commands[0]));
        Commands command;
        try {
            if (Commands.valueOf(comm) == Commands.NEXT) {
                if (currentPage < pages) currentPage++;
                constructScreen();
                return start();
            } else if (Commands.valueOf(comm) == Commands.PREVIOUS) {
                if (currentPage > 0) currentPage--;
                constructScreen();
                return start();
            }
        }catch (IllegalArgumentException e){
            printer.showErrorLine(com.ironhack.Exceptions.ErrorType.COMMAND_NOK);
            constructScreen();
            return start();
        }
        return comm;
    }

}
