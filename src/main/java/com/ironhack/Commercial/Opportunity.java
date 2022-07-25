package com.ironhack.Commercial;

import com.ironhack.CRMManager.User;
import com.ironhack.Constants.OpportunityStatus;
import com.ironhack.Constants.Product;
import com.ironhack.ScreenManager.Text.TextObject;
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
//TODO:
//      -   Builder pattern design to allow obtain each value after create the opportunityBuiilder
//      -   Printable methods
//      -   any constructor used should obtain id from CRMManager.crmData.nextID()


    /*
     *Constructor used only for tests
     */
    public Opportunity(String id, Product product, int quantity, Contact decisionMaker, OpportunityStatus status, User owner) {
        setId(id);
        setProduct(product);
        setQuantity(quantity);
        setDecisionMakerID(decisionMaker.getId());
        setStatus(status);
        setOwner(owner.getName());
    }
    public Opportunity( Product product, int quantity, Contact decisionMaker, OpportunityStatus status, User owner) {
        setId(com.ironhack.CRMManager.CRMManager.crmData.getNextID(getClass()));
        setProduct(product);
        setQuantity(quantity);
        setDecisionMakerID(decisionMaker.getId());
        setStatus(status);
        setOwner(owner.getName());
    }




    public void close(Boolean won){
        //TODO change state from OPEN to CLOSED_WON or CLOSED_LOST depending on the "won" param
    }

//-------------------------------------------------------------------------------------------------------------PRINTABLE
    @Override
    public TextObject toTextObject(){
        //TODO return a new text object with one attribute per line
        return new TextObject();
    }

    @Override
    public String shortPrint() {
        return this.id+" - "+ this.account_companyName;
    }

    @Override
    public TextObject printFullObject() {

        //TODO return a new text object with an attribute each line
        // with attribute name formatted as follows:
        // "ID: " + this.id;
        // "Product: " + this.product.name();
        // etc..
        // ..
        // .
        return null;
    }
}
