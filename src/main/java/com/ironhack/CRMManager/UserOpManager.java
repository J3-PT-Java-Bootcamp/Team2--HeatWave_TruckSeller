package com.ironhack.CRMManager;

import com.ironhack.CRMManager.ScreenManager.ConsolePrinter;
import com.ironhack.CRMManager.ScreenManager.Screens.InputScreen;
import com.ironhack.CRMManager.ScreenManager.Screens.ViewScreen;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Sales.ContactBuilder;
import com.ironhack.Sales.Lead;
import com.ironhack.Sales.OpportunityBuilder;
import com.ironhack.Constants.Product;
import com.ironhack.CRMManager.Exceptions.CRMException;
import com.ironhack.CRMManager.Exceptions.NoCompleteObjectException;
import lombok.Data;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.CRMManager.ScreenManager.InputReader.*;
import static com.ironhack.CRMManager.ScreenManager.Screens.Commands.EXIT;
import static com.ironhack.Constants.ColorFactory.BLANK_SPACE;
import static com.ironhack.CRMManager.Exceptions.ErrorType.COMMAND_NOK;

@Data
public class UserOpManager {
    private final CRMManager manager;
    private final ConsolePrinter printer;

    public void closeOpportunity(String[] caughtInput) {
        //TODO
        if (caughtInput != null && caughtInput.length == 3
                &&(caughtInput[1].equalsIgnoreCase("won")||caughtInput[1].equalsIgnoreCase("lost"))) {
            var opp= crmData.getOpportunity(caughtInput[2].trim().toUpperCase());
            if (opp==null) return ;//fixme
            opp.close(caughtInput[1].equalsIgnoreCase("won"));
            manager.getCurrentUser().removeFromOpportunities(opp.getId());

        }
    }

    public String convertLeadToOpp(String[] caughtInput) {
        if (caughtInput != null && caughtInput.length == 2) {
            var lead = crmData.getLead(caughtInput[1]);
            if (lead != null) {
                try {
                    var oppBuilder = new OpportunityBuilder();
                    var firstScreen = new InputScreen(manager,
                            printer,
                            "New Opportunity",
                            new TextObject("Enter the information offered to customer:"),
                            new String[]{"Product", "Quantity", "Decision Maker", "Account"},
                            PRODUCT_TYPE,
                            INTEGER);
                    var result = firstScreen.start();
                    if (result.equalsIgnoreCase(EXIT.name())) return EXIT.name();

                    var accountName = manager.getScreenManager().accounts_screen(true);
                    var contact = createNewContactBuilder(lead, accountName);

                    var firstData = firstScreen.getValues();
//                    var opp = new Opportunity(Product.valueOf(firstData.get(0)), Integer.parseInt(firstData.get(1)), contact, OpportunityStatus.OPEN, currentUser.getName());
                    oppBuilder.setOwner(manager.getCurrentUser().getName());
                    oppBuilder.setProduct(Product.valueOf(firstData.get(0)));
                    oppBuilder.setQuantity(Integer.parseInt(firstData.get(1)));
                    var opp = oppBuilder.constructOpportunity(accountName, contact);
                    manager.getCurrentUser().addToOpportunityList(opp.getId());
                    manager.getCurrentUser().removeFromLeadList(lead.getId());
                    crmData.addOpportunity(opp);
                    crmData.removeLead(lead.getId());
                    try {
                        manager.saveData();
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

    public void viewObject(String[] caughtInput) {
        if (caughtInput != null && caughtInput.length >= 2) {
            var object = crmData.getUnknownObject(caughtInput[1]);
            if (object == null) return;
            try {
                new ViewScreen(manager, printer, object.shortPrint(), object).start();
            } catch (CRMException e) {
                throw new RuntimeException(e);
            }
        }
    }

//------------------------------------------------------------------------------------------------------INNER METHODS
    private ContactBuilder createNewContactBuilder(Lead lead, String accountName) throws CRMException, NoCompleteObjectException {
        ContactBuilder contact = new ContactBuilder();
        if (manager.getScreenManager().modal_screen("Copy Lead Data ?",
                new TextObject("Do you want to create a new contact\n from the following Lead data?")
                        .addText(BLANK_SPACE).addText(lead.printFullObject())
                        .addText("-- Enter \"NO\" to enter manually a new contact information --"))) {
            contact.setName(lead.getName());
            contact.setPhoneNumber(lead.getPhoneNumber());
            contact.setMail(lead.getMail());
            contact.setCompany(accountName);
        } else {
            var screen = new InputScreen(manager,
                    printer,
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
        }
        return contact;
    }
}
