package com.ironhack.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.Exceptions.*;
import com.ironhack.ScreenManager.InputReader;

import static com.ironhack.Exceptions.ErrorType.OK;
public enum Commands {
    EXIT("EXIT"),
    MENU("MENU"),
    LOGOUT("LOGOUT","LOG OUT"),
    OPP("OPP","OPPORTUNITY","OPPORTUNITIES","VIEW OPPORTUNITY","VIEW OPP"),
    LEAD("LEAD","LEADS","VIEW LEADS","VIEW LEAD"),
    ACCOUNT("ACCOUNT","ACCOUNTS","VIEW ACCOUNTS","VIEW ACCOUNT"),
    USERS("MANAGE USERS","USERS","USER"),
    LOAD("LOAD LEADS","LOAD DATA","LOAD"),
    STATS("STATS","STATISTICS","VIEW STATS","VIEW STATISTICS"),
    YES("YES","OK","CONFIRM","Y"),
    NO("NO","CANCEL","N"),
    BACK("BACK"),
    NEXT("NEXT"),
    PREVIOUS("PREV","PREVIOUS"),
    HELP("HELP"),
    CREATE("CREATE","NEW"),
    CONVERT("CONVERT"),
    CLOSE("CLOSE"),
    VIEW("VIEW","CHECK","SEE","SELECT"),
    DISCARD("DISCARD","DELETE","REMOVE");

    final String[] keyWords;

    String[] caughtInput;
    Commands(String... keyWords){
        this.keyWords = keyWords;
    }
    public boolean check(String input, CRMScreen screen, InputReader inputReader) throws CRMException {
        caughtInput=input.trim().split(" ");
        for(String key: keyWords){
            if(input.contains(key.toUpperCase())) {//TODO better method that "contains" to check also if it has more text than allowed
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
