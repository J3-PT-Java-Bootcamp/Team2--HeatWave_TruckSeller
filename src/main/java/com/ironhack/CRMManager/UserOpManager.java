package com.ironhack.CRMManager;

import com.ironhack.CRMManager.Exceptions.*;
import com.ironhack.CRMManager.ScreenManager.Screens.Commands;
import com.ironhack.CRMManager.ScreenManager.Screens.InputScreen;
import com.ironhack.CRMManager.ScreenManager.Screens.ViewScreen;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Constants.IndustryType;
import com.ironhack.Constants.Product;
import com.ironhack.Sales.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Objects;

import static com.ironhack.CRMManager.CRMData.saveData;
import static com.ironhack.CRMManager.CRMManager.*;
import static com.ironhack.CRMManager.Exceptions.ErrorType.COMMAND_NOK;
import static com.ironhack.CRMManager.Exceptions.ErrorType.ID_NOK;
import static com.ironhack.CRMManager.ScreenManager.InputReader.*;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.*;
import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;
import static com.ironhack.Constants.Constants.FAV_MAX;

@Data
public class UserOpManager {
    public void closeOpportunity(User currentUser, String[] caughtInput) {
        //TODO
        if (caughtInput != null && caughtInput.length == 3
                && (caughtInput[1].equalsIgnoreCase("won") || caughtInput[1].equalsIgnoreCase("lost"))) {
            var opp = crmData.getOpportunity(caughtInput[2].trim().toUpperCase());
            if (opp == null) return;//fixme
            opp.close(caughtInput[1].equalsIgnoreCase("won"));
            var favs= currentUser.getFavourites();
            if(favs!=null&&!favs.isEmpty()&&favs.contains(opp.getId()))currentUser.getFavourites().remove(opp.getId());
            screenManager.confirming_screen(currentUser, opp.shortPrint() + " Closed!",opp.printFullObject().toString(),true);
            currentUser.removeFromOpportunities(opp.getId());
        }
    }

    public String createNewAccount(User currentUser, String... importedData) throws CRMException {

        var newAccountScreen = new InputScreen(currentUser,
                "New Account",
                new TextObject("Enter data for the new Account: ").addText(BLANK_SPACE),
                new String[]{"Company name", "Industry Type", "Employees", "City", "Country"},
                OPEN, INDUSTRY_TYPE, INTEGER, OPEN, OPEN);
        String strRes = null;
        try {
            strRes = newAccountScreen.start();
            if (Objects.equals(strRes, EXIT.name())) return EXIT.name();
        } catch (GoBackException | GoToMenuException | LogoutException | ExitException exit) {
            throw exit;
        } catch (CRMException e) {
            LogWriter.logError(getClass().getSimpleName(),
                    "createNewAccount", "Received a unexpected exception.. " + e.getErrorType());
            return "";
        }
        var userVal = newAccountScreen.getValues();

        if (userVal != null && !userVal.isEmpty() && userVal.size() >= 5) {
            var account = new AccountBuilder();

            account.setCompanyName(userVal.get(0));
            account.setIndustryType(IndustryType.valueOf(userVal.get(1)));
            account.setEmployeeCount(Integer.parseInt(userVal.get(2)));
            account.setCity(userVal.get(3));
            account.setCountry(userVal.get(4));
            try {
                var finalAccount = account.constructAccount();
                crmData.addAccount(finalAccount);
                screenManager.confirming_screen(currentUser, "User " + userVal.get(0) + " password was properly updated.",
                        strRes,
                        true);
                return finalAccount.getCompanyName();
            } catch (NoCompleteObjectException e) {
                throw new RuntimeException(e);
            }
        }
        return createNewAccount(currentUser, importedData);
    }

    public String convertLeadToOpp(User currentUser, String[] caughtInput) {
        if (caughtInput != null && caughtInput.length == 2) {
            var lead = crmData.getLead(caughtInput[1]);
            if (lead != null) {
                try {
                    var oppBuilder = new OpportunityBuilder();
                    var firstScreen = new InputScreen(currentUser,
                            "New Opportunity",
                            new TextObject("Enter the information offered to customer:"),
                            new String[]{"Product", "Quantity"},
                            PRODUCT_TYPE,
                            INTEGER);
                    firstScreen.setHintLine("Enter the product and quantity, product could be: BOX, HYBRID or FLATBED trucks. ");
                    var result = firstScreen.start();
                    if (result.equalsIgnoreCase(EXIT.name())) return EXIT.name();

                    var accountName = screenManager.show_AccountsScreen(true, currentUser);
                    var contact = createNewContactBuilder(currentUser, lead, accountName);

                    var firstData = firstScreen.getValues();
                    oppBuilder.setOwner(currentUser.getName());
                    oppBuilder.setProduct(Product.valueOf(firstData.get(0)));
                    oppBuilder.setQuantity(Integer.parseInt(firstData.get(1)));
                    var opp = oppBuilder.constructOpportunity(accountName, contact);
                    currentUser.addToOpportunityList(opp.getId());
                    currentUser.removeFromLeadList(lead.getId());
                    crmData.addOpportunity(opp);
                    crmData.removeLead(lead.getId());
                    try {
                        if(!screenManager.isTest())saveData();
                    } catch (Exception e) {
                        LogWriter.logError(getClass().getSimpleName(),
                                "convertLeadToOpp->saveData", "Received a unexpected exception.. " + e.getMessage());
                    }

                    screenManager.confirming_screen(currentUser,
                            "Lead %s was properly converted to Opportunity: ".formatted(lead.getId()),
                            opp.printFullObject().toString(),
                            true);
                    return opp.getId();
                } catch (CRMException | NoCompleteObjectException e) {
                    LogWriter.logError(getClass().getSimpleName(),
                            "convertLeadToOpp", "Received a unexpected exception.. " + e.getClass() + e.getMessage());
                    return null;
                }
            }

            printer.showErrorLine(COMMAND_NOK);
        }
        return null;
    }

    public void viewObject(User currentUser, String[] caughtInput) throws CRMException {
        boolean stop = false;
        if (caughtInput != null && caughtInput.length >= 2) {
            do {
                var object = crmData.getUnknownObject(caughtInput[1]);
                if (object == null) {
                    printer.showErrorLine(ID_NOK);
                    return;
                }
                ViewScreen viewScreen = new ViewScreen(currentUser, object.shortPrint(), object);
                var res= viewScreen.start();
                switch (Commands.valueOf(res)) {
                    case FAV -> userOpManager.addToFavourites(currentUser,new String[]{res,object.getId()});
                    case CONVERT -> {
                        userOpManager.convertLeadToOpp(currentUser, new String[]{res, object.getId()});
                        stop=true;
                    }
                    case CLOSE -> {
//                        try{
                            userOpManager.closeOpportunity(currentUser, new String[]{CLOSE.name(),CLOSE.getCaughtInput()[1], object.getId()});
                            viewObject(currentUser,caughtInput);
//                        } catch (GoBackException ignored) {
//                        }catch (Exception e){throw new RuntimeException();}
                    }
                    case OPP -> screenManager.show_OpportunitiesScreen(currentUser,
                            crmData.getAccount(object.getId()).getOpportunities());
                    case ACCOUNT -> {
                        var id = "";
                        if(object instanceof Opportunity)id= ((Opportunity) object).getAccount_companyName().toUpperCase();
                        userOpManager.viewObject(currentUser, new String[]{ACCOUNT.name(), id});
                    }
                    case CONTACTS -> {
                        var id = "";
                        if(object instanceof Opportunity)id= ((Opportunity) object).getDecisionMakerID();
                        userOpManager.viewObject(currentUser, new String[]{CONTACTS.name(), id});
                    }
                    case DISCARD -> stop = discardObject(currentUser, object);
                }
            } while (!stop);
        }
    }

    boolean discardObject(User currentUser, Printable object) {
        if ((!currentUser.isAdmin() && object.getClass() != User.class) || object.getClass() == CRMData.class)
            return false;
        if(screenManager.modal_screen(currentUser,
                " Delete %s ? ".formatted(object.shortPrint()),
                new TextObject("Do you want to delete this %s ?".formatted(object.getClass().getSimpleName()))
                        .addText(BLANK_SPACE).addText(object.printFullObject()))){

            currentUser.removeUnknown(object.getId());
            crmData.removeUnknownObject(object.getId());
            screenManager.confirming_screen(currentUser,
                    "%s : %s was deleted!".formatted(object.getClass().getSimpleName(),object.getId()),
                    object.printFullObject().toString(),
                    true);
            return true;
        }
        return false;
    }

    public void addToFavourites(User currentUser, String[] caughtInput) {
        String varWord= "added to";
        Printable unknownObject = crmData.getUnknownObject(caughtInput[1]);
        ArrayList<String> favs = currentUser.getFavourites();
        if (favs != null) {
            if (favs.contains(caughtInput[1])) {
                if (!screenManager.modal_screen(currentUser, "Quit from Favourites",
                        new TextObject("%s is already saved in favourites. Do you want to quit it?"
                                .formatted(unknownObject.printFullObject())))) {
                    return;
                }
                    varWord = "deleted from";
            } else if (favs.size() >= FAV_MAX && !screenManager.modal_screen(currentUser, "Favourites is full",
                    new TextObject("Favourites list is full, confirm if you want to add %s and delete the older element showed below:".formatted(unknownObject.shortPrint()))
                            .addText(crmData.getUnknownObject(favs.get(0)).toTextObject()))) {
                return;
            }
        }
        currentUser.addToFavourites(caughtInput[1]);
        screenManager.confirming_screen(currentUser,
                unknownObject.shortPrint()+" was properly "+varWord+" the favourites list",
                unknownObject.printFullObject().toString(),
                true
                );
    }

    //------------------------------------------------------------------------------------------------------INNER METHODS
    private ContactBuilder createNewContactBuilder(User currentUser, Lead lead, String accountName) throws CRMException {
        ContactBuilder contact = new ContactBuilder();
        if (screenManager.modal_screen(currentUser, "Copy Lead Data ?",
                new TextObject("Do you want to create a new contact\n from the following Lead data?")
                        .addText(BLANK_SPACE).addText(lead.printFullObject())
                        .addText("-- Enter \"NO\" to enter manually a new contact information --"))) {
            contact.setName(lead.getName());
            contact.setPhoneNumber(lead.getPhoneNumber());
            contact.setMail(lead.getMail());
            contact.setCompany(accountName);
            return contact;
        }
        var screen = new InputScreen(currentUser,
                "-- New Contact --",
                new TextObject("Enter the information about Contact for current Opportunity:")
                        .addText(BLANK_SPACE).addText("Company : " + accountName),
                new String[]{"Name", "Phone Number", "Mail"},
                OPEN,
                PHONE,
                MAIL);
        screen.start();
        var val = screen.getValues();
        contact.setName(val.get(0));
        contact.setPhoneNumber(val.get(1));
        contact.setMail(val.get(2));
        contact.setCompany(accountName);

        return contact;
    }
}
