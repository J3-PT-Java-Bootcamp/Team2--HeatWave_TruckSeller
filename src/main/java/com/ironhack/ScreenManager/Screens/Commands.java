package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Exceptions.*;


public enum Commands {
    EXIT("exit"),
    MENU("menu"),
    LOGOUT("logout","log out"),
    OPP("opp","opportunity","opportunities","view opportunity","view opp"),
    LEAD("lead","leads","view leads","view lead"),
    ACCOUNT("account","accounts","view accounts","view account"),
    CONVERT("convert"),
    CLOSE("close"),
    YES("yes","ok","confirm"),
    NO("no","cancel"),
    BACK("back"),
    NEXT("next"),
    PREVIOUS("prev","previous");

    final String[] commands;
    Commands(String... commands){
        this.commands=commands;
    }
    public boolean check(String input, CRMScreen screen) throws CRMException {
        input=input.trim().toLowerCase();
        for(String command:commands){
            if(input.contains(command)) {
                return act(input, screen);
            }
        }
        return false;
    }

    private Boolean act(String input, CRMScreen screen) throws CRMException{
        switch (this){

            case EXIT -> {
                    throw new ExitException(false);
            }
            case MENU -> {
                throw new BackToMenuScreen();
            }
            case LOGOUT -> {
                throw new ExitException(true);
            }
            case OPP -> {
                return true;
            }
            case LEAD -> {
                return true;
            }
            case ACCOUNT -> {
                return true;
            }
            case CONVERT -> {
                var leadID=input.split(" ")[1];
                var lead= CRMManager.crmData.getLead(leadID);
                if (lead == null)throw new WrongInputException(ErrorType.ID_NOK);
                var opp=lead.convertToOpp();
                //FIXME how to send the new opportunity to keep enterin data?
                return true;
            }
            case CLOSE -> {
                var commandList= input.split(" ");
                if(commandList.length!=3)throw new WrongInputException(ErrorType.COMMAND_NOK);
                var closeType=commandList[1];
                var oppID= commandList[2];
                var opp= CRMManager.crmData.getOpportunity(oppID);
                if (opp==null)throw new WrongInputException(ErrorType.ID_NOK);
                if (closeType.equalsIgnoreCase("lost"))opp.close(false);
                else if (closeType.equalsIgnoreCase("won")) opp.close(true);
                return true;
            }
            case YES ->{
                return true;
            }

            case NO -> {
                return true;
            }
            case BACK -> {
                throw new BackScreenInput(screen);
            }
            case NEXT -> {
                return true;
            }
            case PREVIOUS -> {
                return true;
            }
            default -> throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}
