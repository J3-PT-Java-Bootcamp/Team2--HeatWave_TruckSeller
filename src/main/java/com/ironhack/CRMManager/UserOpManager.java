package com.ironhack.CRMManager;

import com.ironhack.CRMManager.Exceptions.CRMException;
import com.ironhack.CRMManager.Exceptions.NoCompleteObjectException;
import com.ironhack.CRMManager.ScreenManager.Screens.InputScreen;
import com.ironhack.CRMManager.ScreenManager.Screens.ViewScreen;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Constants.IndustryType;
import com.ironhack.Constants.Product;
import com.ironhack.Sales.AccountBuilder;
import com.ironhack.Sales.ContactBuilder;
import com.ironhack.Sales.Lead;
import com.ironhack.Sales.OpportunityBuilder;
import lombok.Data;

import java.util.Objects;

import static com.ironhack.CRMManager.CRMManager.*;
import static com.ironhack.CRMManager.Exceptions.ErrorType.COMMAND_NOK;
import static com.ironhack.CRMManager.ScreenManager.InputReader.*;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.EXIT;
import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;

@Data
public class UserOpManager {

    public void closeOpportunity(User currentUser,String[] caughtInput) {
        //TODO
        if (caughtInput != null && caughtInput.length == 3
                &&(caughtInput[1].equalsIgnoreCase("won")||caughtInput[1].equalsIgnoreCase("lost"))) {
            var opp= crmData.getOpportunity(caughtInput[2].trim().toUpperCase());
            if (opp==null) return ;//fixme
            opp.close(caughtInput[1].equalsIgnoreCase("won"));
            currentUser.removeFromOpportunities(opp.getId());

        }
    }

    public String createNewAccount(User currentUser,String... importedData) {

        var newAccountScreen = new InputScreen(currentUser,
                "New Account",
                new TextObject("Enter data for the new Account: ").addText(BLANK_SPACE),
                new String[]{"Company name", "Industry Type", "Employees", "City", "Country"},
                OPEN, INDUSTRY_TYPE, INTEGER, OPEN, OPEN);
        String strRes = null;
        try {
            strRes = newAccountScreen.start();
            if (Objects.equals(strRes, EXIT.name())) return EXIT.name();
        } catch (CRMException e) {
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
                screenManager.confirming_screen(currentUser,"User " + userVal.get(0) + " password was properly updated.",
                        strRes,
                        true);
                return finalAccount.getCompanyName();
            } catch (NoCompleteObjectException e) {
                throw new RuntimeException(e);
            }
        }
        return createNewAccount(currentUser,importedData);
    }

    public String convertLeadToOpp(User currentUser,String[] caughtInput) {
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
                    var result = firstScreen.start();
                    if (result.equalsIgnoreCase(EXIT.name())) return EXIT.name();

                    var accountName =screenManager.show_AccountsScreen(true,currentUser);
                    var contact = createNewContactBuilder(currentUser,lead, accountName);

                    var firstData = firstScreen.getValues();
//                    var opp = new Opportunity(Product.valueOf(firstData.get(0)), Integer.parseInt(firstData.get(1)), contact, OpportunityStatus.OPEN, currentUser.getName());
                    oppBuilder.setOwner(currentUser.getName());
                    oppBuilder.setProduct(Product.valueOf(firstData.get(0)));
                    oppBuilder.setQuantity(Integer.parseInt(firstData.get(1)));
                    var opp = oppBuilder.constructOpportunity(accountName, contact);
                    currentUser.addToOpportunityList(opp.getId());
                    currentUser.removeFromLeadList(lead.getId());
                    crmData.addOpportunity(opp);
                    crmData.removeLead(lead.getId());
                    try {
                        crmData.saveData();
                    } catch (Exception ignored) {
                    }
                    return opp.getId();
                } catch (CRMException | NoCompleteObjectException e) {
                    throw new RuntimeException(e);
                }
            }

            printer.showErrorLine(COMMAND_NOK);
        }
        return null;
    }

    public void viewObject(User currentUser,String[] caughtInput) {
        if (caughtInput != null && caughtInput.length >= 2) {
            var object = crmData.getUnknownObject(caughtInput[1]);
            if (object == null) return;
            try {
                new ViewScreen(currentUser, object.shortPrint(), object).start();
            } catch (CRMException e) {
                throw new RuntimeException(e);
            }
        }
    }

//------------------------------------------------------------------------------------------------------INNER METHODS
    private ContactBuilder createNewContactBuilder(User currentUser,Lead lead, String accountName) throws CRMException, NoCompleteObjectException {
        ContactBuilder contact = new ContactBuilder();
        if (screenManager.modal_screen(currentUser,"Copy Lead Data ?",
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
