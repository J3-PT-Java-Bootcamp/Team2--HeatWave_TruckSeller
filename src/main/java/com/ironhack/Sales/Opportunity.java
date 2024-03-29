package com.ironhack.Sales;

import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Constants.OpportunityStatus;
import com.ironhack.Constants.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.CRMManager.ScreenManager.InputReader.OPEN;
import static com.ironhack.CRMManager.ScreenManager.InputReader.PRODUCT_TYPE;
import static com.ironhack.CRMManager.ScreenManager.InputReader.toCamelCase;
import static com.ironhack.Constants.OpportunityStatus.*;

@NoArgsConstructor
@Data
public class Opportunity implements Printable{
    private String id;
    private Product product;
    private int quantity;
    private String decisionMakerID;
    private OpportunityStatus status;
    private String account_companyName;//Saves only the companyName as it is the key in the account map
    private String owner;//Saves only username as it is the key in the user map

    /*
     *Constructor used only for tests
     */

    public Opportunity( Product product, int quantity, String decisionMaker, String owner, String companyName) {
        setId(crmData.getNextID(getClass()));
        setProduct(product);
        setQuantity(quantity);
        setDecisionMakerID(decisionMaker);
        setOwner(owner);
        setStatus(OpportunityStatus.OPEN);
        setAccount_companyName(companyName);
    }




    public void close(Boolean won){
        this.status=won?CLOSE_WON:CLOSE_LOST;
        }


//-------------------------------------------------------------------------------------------------------------PRINTABLE
    @Override
    public TextObject toTextObject(){

        return new TextObject(id).addText(PRODUCT_TYPE.formatOutput(product.name())).addText(String.valueOf(quantity))
        .addText(toCamelCase(status.name())).addText(toCamelCase(account_companyName))
                .addText(toCamelCase(crmData.getContact(decisionMakerID).shortPrint()));

    }

    @Override
    public String shortPrint() {
        return OPEN.formatOutput(this.account_companyName)+" ("+quantity+")"+product.toString();
    }

    @Override
    public TextObject printFullObject() {

        return new TextObject("Id: "+id)
                .addText("Product: "+product.toString())
                .addText("Quantity: "+quantity)
                .addText("Contact: "+ OPEN.formatOutput(crmData.getUnknownObject(decisionMakerID).shortPrint()))
                .addText("Opp Status: "+OPEN.formatOutput(status.name()))
                .addText("Account: "+OPEN.formatOutput(account_companyName))
                .addText("Proprietary: "+toCamelCase(owner));

    }

    @Override
    public String[] getPrintableAttributes() {
        return new String[]{"ID","Product", "Quantity", "Status", "Company","Contact"};
    }

}
