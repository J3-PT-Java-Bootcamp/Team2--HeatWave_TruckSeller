package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.Exceptions.*;
import com.ironhack.CRMManager.ScreenManager.InputReader;
import lombok.Getter;

import static com.ironhack.CRMManager.Exceptions.ErrorType.OK;
@Getter
public enum Commands {
    EXIT("Exit App","EXIT","BYE","TURNOFF"),
    MENU("Go to Menu","MENU"),
    LOGOUT("Log Out","LOGOUT","LOG OUT"),
    CREATE("Create New...","CREATE","NEW","CREATE NEW"),
    CONVERT("CONVERT to Opportunity","CONVERT", "CONVERT LEAD"),
    CLOSE("CLOSE Opportunity","CLOSE","CLOSE OPP", "CLOSE OPPORTUNITY"),
    OPP("View OPPortunities","OPP","OPPORTUNITY","OPPORTUNITIES","VIEW OPPORTUNITY","VIEW OPP"),
    LEAD("View LEADs","LEAD","LEADS","VIEW LEADS","VIEW LEAD"),
    ACCOUNT("View ACCounts","ACC","ACCOUNT","ACCOUNTS","VIEW ACCOUNTS","VIEW ACCOUNT"),
    CONTACTS("View CONTacts","CONT","CONTACT","CONTACTS", "VIEW CONTACTS", "VIEW CONTACT"),
    USERS("View USERs","MANAGE USERS","USERS","USER"),
    LOAD("LOAD Leads Data","LOAD LEADS","LOAD DATA","LOAD"),
    STATS("View STATistics","STAT","STATISTICS","VIEW STATS","VIEW STATISTICS"),
    YES("Confirm","YES","OK","CONFIRM","Y"),
    NO("Cancel","NO","CANCEL","N"),
    BACK("Go BACK","BACK"),
    NEXT("NEXT","NEXT"),
    PREVIOUS("PREVious","PREV","PREVIOUS"),
    HELP("HELP","HELP"),
    VIEW("SELECT/VIEW","VIEW","CHECK","SEE","SELECT"),
    DISCARD("Discard..","DISCARD","DELETE","REMOVE");

    private final String[] keyWords;
    private final String display;

    String[] caughtInput;
    Commands(String display,String... keyWords){
        this.display=display;
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
    private Boolean act(String input, CRMScreen screen, InputReader inputReader) throws CRMException{
        switch (this){
            case EXIT -> throw new ExitException(false);
            case MENU -> {
                if(screen.getCurrentUser()!=null) throw new GoToMenuException();
                throw new GoBackException(screen);
            }
            case LOGOUT -> throw new LogoutException(OK);
            case OPP, NO, YES, NEXT, PREVIOUS, ACCOUNT,CONTACTS, LEAD, STATS, LOAD, USERS, CREATE -> {
                return true;
            }
            case BACK -> throw new GoBackException(screen);
            case HELP -> throw new HelpException(ErrorType.HELP, inputReader.getHint(), screen.commands.toArray(new Commands[0]));
            case VIEW, DISCARD, CONVERT -> {
                if(caughtInput.length!=2){
                    if(! (screen instanceof ViewScreen))throw new WrongInputException(ErrorType.COMMAND_NOK);
                }else {
                    var inputId = caughtInput[1].trim().toUpperCase();
                    char identifier = inputId.toCharArray()[0];
                    if (!CRMManager.crmData.existsObject(inputId)) throw new WrongInputException(ErrorType.ID_NOK);
                }
                return true;
            }
            case  CLOSE-> {
                if(caughtInput.length!=3){
                    if(! (screen instanceof ViewScreen)&&caughtInput.length==2)throw new WrongInputException(ErrorType.COMMAND_NOK);
                }else {
                    var inputId = caughtInput[2].trim().toUpperCase();
                    char identifier = inputId.toCharArray()[0];
                    if (!CRMManager.crmData.existsObject(inputId)) throw new WrongInputException(ErrorType.ID_NOK);
                }
                return true;
            }
        }
        return false;
    }
}
