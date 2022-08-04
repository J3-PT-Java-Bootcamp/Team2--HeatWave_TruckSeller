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
    CREATE("Create New...","Create new object depending on current screen", false, "CREATE","NEW","CREATE NEW"),
    CONVERT("CONVERT to Opportunity","Converts selected Lead into Opportunity", true, "CONVERT", "CONVERT LEAD"),
    CLOSE("CLOSE Opportunity","Close selected Opportunity (need to specify WON or LOST + ID) ", true, "CLOSE","CLOSE OPP", "CLOSE OPPORTUNITY"),
    OPP("View OPPortunities", "View user's active Opportunities",false, "OPP","OPPORTUNITY","OPPORTUNITIES","VIEW OPPORTUNITY","VIEW OPP"),
    LEAD("View LEADs","View user's assigned Leads",false,"LEAD",  "LEADS","VIEW LEADS","VIEW LEAD"),
    ACCOUNT("View ACCounts","View saved Accounts",false, "ACC", "ACCOUNT","ACCOUNTS","VIEW ACCOUNTS","VIEW ACCOUNT"),
    CONTACTS("View CONTacts","View related Contact",false, "CONT",  "CONTACT","CONTACTS", "VIEW CONTACTS", "VIEW CONTACT"),
    USERS("View USERs","Manage users and passwords",false,"MANAGE USERS",  "USERS","USER"),
    LOAD("LOAD Leads Data","Load lead data from csv file",false,"LOAD LEADS",  "LOAD DATA","LOAD"),
    STATS("View STATistics","View all users statistics",false,"STAT", "STATISTICS","VIEW STATS","VIEW STATISTICS"),
    YES("Confirm","Confirm option...",false,"YES", "OK","CONFIRM","Y"),
    NO("Cancel","Cancel option...",false,"NO", "CANCEL","N"),
    BACK("Go BACK","Turn back to the previous screen or input",false,"BACK"),
    NEXT("NEXT","Go to next page in table screens",false,"NEXT","NXT"),
    PREVIOUS("PREVious","Go to previous page in table screens",false,"PREV","PREVIOUS"),
    HELP("HELP","Shows help hint...",false,"HELP"),
    VIEW("SELECT/VIEW","Shows all data from the selected object or Select ",false,"VIEW", "CHECK","SEE","SELECT"),
    DISCARD("Discard..","Delete the selected objecct",false,"DISCARD", "DELETE","REMOVE"),
    FAV("Add to Favourites","Adds selected object to favourites",true,"FAV","FAVOURITE"),
    README("Open READme..", "READ", false, "README");

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
            if(input.contains(key.toUpperCase())) {//TODO better method that "contains" to check also if it has more text than allowed
                return act(input, screen,inputReader);
            }
        }
        return false;
    }
    private Boolean act(String input, CRMScreen screen, InputReader inputReader) throws CRMException{
        switch (this){
            case EXIT -> throw new ExitException();
            case MENU -> {
                if(screen.getCurrentUser()!=null) throw new GoToMenuException();
                throw new GoBackException();
            }
            case LOGOUT -> throw new LogoutException(OK);
            case OPP, NO, YES, NEXT, PREVIOUS, ACCOUNT,CONTACTS, LEAD, STATS, LOAD, USERS, CREATE -> {
                return true;
            }
            case BACK -> throw new GoBackException();
            case HELP -> throw new HelpException(ErrorType.HELP, inputReader.getHint(), screen.commands.toArray(new Commands[0]));
            case VIEW,FAV, DISCARD, CONVERT -> {
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
