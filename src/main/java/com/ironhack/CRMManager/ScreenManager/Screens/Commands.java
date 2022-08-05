package com.ironhack.CRMManager.ScreenManager.Screens;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.Exceptions.*;
import com.ironhack.CRMManager.ScreenManager.InputReader;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Sales.Printable;
import lombok.Getter;

import java.util.List;

import static com.ironhack.CRMManager.Exceptions.ErrorType.OK;
@Getter
public enum Commands implements Printable {
    EXIT("Exit App","Close application", false, "EXIT","BYE","TURNOFF"),
    MENU("Go to Menu","Go back to Main Menu", false, "MENU"),
    LOGOUT("Log Out","Logout and go to Login screen", false, "LOGOUT","LOG OUT"),
    CREATE("Create New...","Create new object depending on current screen", false, "CREATE","NEW"),
    CONVERT("CONVERT to Opportunity","Converts selected Lead into Opportunity", true, "CONVERT"),
    CLOSE("CLOSE Opportunity","Close selected Opportunity ", true, "CLOSE"),
    OPP("View OPPortunities", "View user's active Opportunities",false, "OPP","OPPORTUNITY"),
    LEAD("View LEADs","View user's assigned Leads",false,"LEAD",  "LEADS"),
    ACCOUNT("View ACCounts","View saved Accounts",false, "ACC", "ACCOUNT"),
    CONTACTS("View CONTacts","View related Contact",false, "CONT",  "CONTACT"),
    USERS("View USERs","Manage users and passwords",false,"MANAGE",  "USERS","USER"),
    LOAD("LOAD Leads Data","Load lead data from csv file",false,"LOAD"),
    STATS("View STATistics","View all users statistics",false,"STAT", "STATISTICS"),
    YES("Confirm","Confirm option...",false,"YES", "OK","CONFIRM"),
    NO("Cancel","Cancel option...",false,"NO", "CANCEL"),
    BACK("Go BACK","Turn back to the previous screen or input",false,"BACK"),
    NEXT("NEXT","Go to next page in table screens",false,"NEXT","NXT"),
    PREVIOUS("PREVious","Go to previous page in table screens",false,"PREV","PREVIOUS"),
    HELP("HELP","Shows help hint...",false,"HELP"),
    VIEW("SELECT/VIEW","Shows all data from the selected object",true,"VIEW", "SELECT"),
    DISCARD("Discard..","Delete the selected object",true,"DISCARD", "DELETE","REMOVE"),
    FAV("Add to Favourites","Adds selected object to favourites",true,"FAV","SAVE"),
    README("READ commands info", "Open this commands help", false, "READ","COMMAND");

    private final String[] keyWords;
    private final String description;
    private final String display;
    private final boolean needsId;

    String[] caughtInput;
    Commands(String display, String description, boolean needsId, String... keyWords){
        this.display=display;
        this.needsId = needsId;
        this.keyWords = keyWords;
        this.description=description;
    }
    public boolean check(String input, CRMScreen screen, InputReader inputReader) throws CRMException {
        caughtInput=input.trim().split(" ");
        for(String key: keyWords){
            if(input.contains(key.toUpperCase())) {
                return act(screen,inputReader);
            }
        }
        return false;
    }
    private Boolean act(CRMScreen screen, InputReader inputReader) throws CRMException{

        switch (this){
            case EXIT -> throw new ExitException();
            case MENU -> {
                if(screen.getCurrentUser()!=null) throw new GoToMenuException();
                throw new GoBackException();
            }
            case LOGOUT -> throw new LogoutException(OK);
            case OPP, NO, YES, NEXT, PREVIOUS, ACCOUNT,CONTACTS, LEAD, STATS, LOAD, USERS, CREATE,README -> {
                return true;
            }
            case BACK -> throw new GoBackException();
            case HELP -> throw new HelpException(ErrorType.HELP, inputReader.getHint(), screen.commands.toArray(new Commands[0]));
            case VIEW,FAV, DISCARD, CONVERT -> {
                if(caughtInput.length!=2){
                    if(! (screen instanceof ViewScreen))throw new WrongInputException(ErrorType.COMMAND_NOK);
                }else {
                    var inputId = caughtInput[1].trim().toUpperCase();
                    if (!CRMManager.crmData.existsObject(inputId)) throw new WrongInputException(ErrorType.ID_NOK);
                }
                return true;
            }
            case  CLOSE-> {
                if(caughtInput.length!=3){
                    if(! (screen instanceof ViewScreen)&&caughtInput.length==2)throw new WrongInputException(ErrorType.COMMAND_NOK);
                }else {
                    var inputId = caughtInput[2].trim().toUpperCase();
                    if (!CRMManager.crmData.existsObject(inputId)) throw new WrongInputException(ErrorType.ID_NOK);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String getId() {
        return name();
    }

    @Override
    public TextObject toTextObject() {
        return new TextObject(shortPrint()).addText(description).addText(List.of(keyWords).toString());
    }

    @Override
    public String shortPrint() {
        return display;
    }

    @Override
    public TextObject printFullObject() {
        return null;
    }

    @Override
    public String[] getPrintableAttributes() {
        return new String[]{"Command", "Description","Keywords"};
    }
}
