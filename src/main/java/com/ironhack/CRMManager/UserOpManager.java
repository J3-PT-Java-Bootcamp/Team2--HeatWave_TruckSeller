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

import java.util.Objects;

import static com.ironhack.CRMManager.CRMData.saveData;
import static com.ironhack.CRMManager.CRMManager.*;
import static com.ironhack.CRMManager.Exceptions.ErrorType.COMMAND_NOK;
import static com.ironhack.CRMManager.Exceptions.ErrorType.ID_NOK;
import static com.ironhack.CRMManager.ScreenManager.InputReader.*;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.*;
import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;

@Data
public class UserOpManager {
    public Opportunity closeOpportunity(User currentUser, String[] caughtInput) {
        //TODO
        if (caughtInput != null && caughtInput.length == 3
                && (caughtInput[1].equalsIgnoreCase("won") || caughtInput[1].equalsIgnoreCase("lost"))) {
            var opp = crmData.getOpportunity(caughtInput[2].trim().toUpperCase());
            if (opp == null) return null;//fixme
            opp.close(caughtInput[1].equalsIgnoreCase("won"));
            screenManager.confirming_screen(currentUser, opp.shortPrint() + " Closed!",opp.printFullObject().toString(),true);
            currentUser.removeFromOpportunities(opp.getId(), (caughtInput[1].equalsIgnoreCase("won")));
            return opp;
        }
        return null;
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
                var res= new ViewScreen(currentUser, object.shortPrint(), object).start();
                switch (Commands.valueOf(res)) {

                    case CONVERT -> {
                        userOpManager.convertLeadToOpp(currentUser, new String[]{res, object.getId()});
                        stop=true;
                    }
                    case CLOSE -> {
                        userOpManager.closeOpportunity(currentUser, new String[]{res, object.getId()});
                        viewObject(currentUser,caughtInput);
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
                    case DISCARD -> {
                        if(screenManager.modal_screen(currentUser,
                                "Discard Lead?",
                                new TextObject("Do you want to delete this lead?")
                                        .addText(BLANK_SPACE).addText(object.printFullObject()))){
                            currentUser.removeUnknown(object.getId());
                            crmData.removeUnknownObject(object.getId());
                            screenManager.confirming_screen(currentUser,
                                    "Lead %s was deleted!".formatted(object.getId()),
                                    object.printFullObject().toString(),
                                    true);
                            stop=true;

                        }
                    }
                }
            } while (!stop);
        }
    }

    //------------------------------------------------------------------------------------------------------INNER METHODS
    private ContactBuilder createNewContactBuilder(User currentUser, Lead lead, String accountName) throws CRMException, NoCompleteObjectException {
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
