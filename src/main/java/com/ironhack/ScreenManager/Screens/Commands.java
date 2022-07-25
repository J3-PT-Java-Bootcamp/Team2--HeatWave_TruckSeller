package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Exceptions.*;
import com.ironhack.ScreenManager.InputReader;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static com.ironhack.Exceptions.ErrorType.OK;
public enum Commands {
    EXIT("exit"),
    MENU("menu"),
    LOGOUT("logout","log out"),
    OPP("opp","opportunity","opportunities","view opportunity","view opp"),
    LEAD("lead","leads","view leads","view lead"),
    ACCOUNT("account","accounts","view accounts","view account"),
    USERS("manage users","users","user"),
    LOAD("load leads","load data","load"),
    STATS("stats","statistics","view stats","view statistics"),
    YES("yes","ok","confirm","y"),
    NO("no","cancel","n"),
    BACK("back"),
    NEXT("next"),
    PREVIOUS("prev","previous"),
    HELP("help"),
    CREATE("create","new"),
    CONVERT("convert"),
    CLOSE("close"),
    VIEW("view","check","see"),
    DISCARD("discard","delete","remove");

    final String[] commands;

    String[] caughtInput;
    Commands(String... commands){
        this.commands=commands;
    }
    public boolean check(String input, CRMScreen screen, InputReader inputReader) throws CRMException {
        caughtInput=input.trim().toLowerCase().split(" ");
        for(String command:commands){
            if(input.contains(command)) {
                return act(input, screen,inputReader);
            }
        }
        return false;
    }
    public String[] getCaughtInput(){
        return caughtInput;
    }
    private Boolean act(String input, CRMScreen screen, InputReader inputReader) throws CRMException{
        switch (this){
            case EXIT -> {
                    throw new ExitException(false);
            }
            case MENU -> {
                if(screen.crmManager.currentUser!=null) throw new GoToMenuException();
                throw new GoBackException(screen);
            }
            case LOGOUT -> {
                throw new LogoutException(OK);
            }
            case OPP, NO, YES, NEXT, PREVIOUS, ACCOUNT, LEAD, STATS, LOAD, USERS -> {
                return true;
            }
            case BACK -> {
                throw new GoBackException(screen);
            }
            case HELP -> {
                throw new HelpException(ErrorType.HELP, inputReader.getHint(), screen.commands.toArray(new Commands[0]));
            }
            case VIEW, DISCARD, CLOSE, CONVERT -> {
                caughtInput =input.split(" ");
                if(caughtInput.length!=2)throw new WrongInputException(ErrorType.COMMAND_NOK);
                var inputId= caughtInput[1].trim().toUpperCase();
                char identifier= inputId.toCharArray()[0];
                if(identifier=='L'){
                    if(!CRMManager.crmData.getLeadMap().containsKey(inputId)) throw new WrongInputException(ErrorType.ID_NOK);
                }else if(identifier=='O') {
                    if (!CRMManager.crmData.getOpportunityMap().containsKey(inputId)) throw new WrongInputException(ErrorType.ID_NOK);
                }else if(identifier=='C'){
                    if(!CRMManager.crmData.getContactMap().containsKey(inputId)) throw new WrongInputException(ErrorType.ID_NOK);
                }else{
                    if(!CRMManager.crmData.getAccountMap().containsKey(inputId)) throw new WrongInputException(ErrorType.ID_NOK);
                }
                return true;
            }
            case CREATE->{
                caughtInput=input.split(" ");
                return true;
            }
        }
        return false;
    }
}
