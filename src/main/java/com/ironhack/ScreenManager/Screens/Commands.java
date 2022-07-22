package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Exceptions.*;
import com.ironhack.ScreenManager.InputReader;

import static com.ironhack.Exceptions.ErrorType.OK;

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
    PREVIOUS("prev","previous"),
    HELP("help"),
    VIEW("view","check","see"),
    DISCARD("discard","delete","remove"),
    DELAY("delay","aplace","snooze","skip");

    final String[] commands;

    String[] caughtInput;
    Commands(String... commands){
        this.commands=commands;
    }
    public boolean check(String input, CRMScreen screen, InputReader inputReader) throws CRMException {
        input=input.trim().toLowerCase();
        for(String command:commands){
            if(input.contains(command)) {
                return act(input, screen,inputReader);
            }
        }
        return false;
    }
    public String[] getCaughtInput(){
        var inp= caughtInput.clone();
        caughtInput=null;
        return inp;
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
            case OPP, NO, YES, NEXT, PREVIOUS, ACCOUNT, LEAD -> {
                return true;
            }
            case CONVERT -> {
                caughtInput =input.split(" ");
                var leadID=caughtInput[1];
                var lead= CRMManager.crmData.getLead(leadID);
                if (lead == null)throw new WrongInputException(ErrorType.ID_NOK);
                var opp=lead.convertToOpp();
                //FIXME how to send the new opportunity to keep enterin data?
                //TODO move to opp screen
                return true;
            }
            case CLOSE -> {
                caughtInput =input.split(" ");
                var commandList= caughtInput;
                if(commandList.length!=3)throw new WrongInputException(ErrorType.COMMAND_NOK);
                var closeType=commandList[1];
                var oppID= commandList[2];
                var opp= CRMManager.crmData.getOpportunity(oppID);
                if (opp==null)throw new WrongInputException(ErrorType.ID_NOK);
                if (closeType.equalsIgnoreCase("lost"))opp.close(false);
                else if (closeType.equalsIgnoreCase("won")) opp.close(true);
                return true;
            }
            case VIEW -> {
                caughtInput =input.split(" ");
                if(caughtInput.length!=2)throw new WrongInputException(ErrorType.COMMAND_NOK);
                return true;

            }
            case DISCARD -> {
                caughtInput = input.split(" ");
                if (caughtInput.length != 2) throw new WrongInputException(ErrorType.COMMAND_NOK);
                return true;
            }
            case DELAY -> {
                caughtInput =input.split(" ");
                if (caughtInput.length != 2) throw new WrongInputException(ErrorType.COMMAND_NOK);
                return true;
            }


            case BACK -> {
                throw new GoBackException(screen);
            }
            case HELP -> {
                throw new HelpException(ErrorType.HELP, inputReader.getHint(), screen.commands.toArray(new Commands[0]));
            }
        }
        return false;
    }
}
