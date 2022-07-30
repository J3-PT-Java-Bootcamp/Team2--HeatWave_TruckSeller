package com.ironhack.Sales;

import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import com.ironhack.Constants.OpportunityStatus;
import com.ironhack.Constants.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.ironhack.CRMManager.CRMManager.crmData;
import static com.ironhack.Constants.OpportunityStatus.OPEN;

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
        setStatus(OPEN);
        setOwner(owner);
        setAccount_companyName(companyName);
    }




    public void close(Boolean won){
        //TODO change state from OPEN to CLOSED_WON or CLOSED_LOST depending on the "won" param
    }

//-------------------------------------------------------------------------------------------------------------PRINTABLE
    @Override
    public TextObject toTextObject(){

        return new TextObject(id).addText(product.name()).addText(String.valueOf(quantity))
                .addText(status.name()).addText(account_companyName);
    }

    @Override
    public String shortPrint() {
        return this.id+" - "+ this.account_companyName;
    }

    @Override
    public TextObject printFullObject() {

        return new TextObject("Id: "+id)
                .addText("Product: "+product.toString())
                .addText("Quantity: "+quantity)
                .addText("Contact: "+ crmData.getUnknownObject(decisionMakerID).shortPrint())
                .addText("Opp Status: "+status.name())
                .addText("Account: "+account_companyName)
                .addText("Proprietary: "+owner);

    }

    @Override
    public String[] getPrintableAttributes() {
        return new String[]{"ID","Product", "Quantity", "Status", "Account Company Name"};
    }

}
