package com.ironhack.Sales;

import com.ironhack.CRMManager.CRMManager;
import com.ironhack.CRMManager.User;
import com.ironhack.Constants.OpportunityStatus;
import com.ironhack.Constants.Product;
import com.ironhack.CRMManager.ScreenManager.Text.TextObject;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    public Opportunity(String id, Product product, int quantity, Contact decisionMaker, OpportunityStatus status, User owner,String companyName) {
        setId(id);
        setProduct(product);
        setQuantity(quantity);
        setDecisionMakerID(decisionMaker.getId());
        setStatus(status);
        setOwner(owner.getName());
    }
    public Opportunity( Product product, int quantity, String decisionMaker, OpportunityStatus status, String owner, String companyName) {
        setId(com.ironhack.CRMManager.CRMManager.crmData.getNextID(getClass()));
        setProduct(product);
        setQuantity(quantity);
        setDecisionMakerID(decisionMaker);
        setStatus(status);
        setOwner(owner);
        setAccount_companyName(account_companyName);
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
                .addText("Contact: "+ CRMManager.crmData.getUnknownObject(decisionMakerID).shortPrint())
                .addText("Opp Status: "+status.name())
                .addText("Account: "+account_companyName)
                .addText("Proprietary: "+owner);

    }

    @Override
    public String[] getPrintableAttributes() {
        return new String[]{"ID","Product", "Quantity", "Status", "Account Company Name"};
    }

}
