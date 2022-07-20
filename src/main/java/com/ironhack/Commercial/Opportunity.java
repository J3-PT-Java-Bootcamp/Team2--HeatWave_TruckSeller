package com.ironhack.Commercial;

import com.ironhack.CRMManager.CRMData;
import com.ironhack.CRMManager.User;
import com.ironhack.Constants.ColorFactory.BgColors;
import com.ironhack.Constants.OpportunityStatus;
import com.ironhack.Constants.Product;
import com.ironhack.ScreenManager.Text.TextObject;


public class Opportunity {
    private String id;
    private Product product;
    private int quantity;
    private Contact decisionMaker;
    private OpportunityStatus status;
    private Account account;
    private User owner;

    public Opportunity(CRMData data, Product product, int quantity, Contact decisionMaker, OpportunityStatus status, User owner) {
        setId(data.getNextID(this.getClass()));
        setProduct(product);
        setQuantity(quantity);
        setDecisionMaker(decisionMaker);
        setStatus(status);
        setOwner(owner);
    }

    public Opportunity(Account account) {
        setAccount(account);
    }

    public Opportunity() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Contact getDecisionMaker() {
        return decisionMaker;
    }

    public void setDecisionMaker(Contact decisionMaker) {
        this.decisionMaker = decisionMaker;
    }

    public OpportunityStatus getStatus() {
        return status;
    }

    public void setStatus(OpportunityStatus status) {
        this.status = status;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void close(Boolean won){
        //TODO change state from OPEN to CLOSED_WON or CLOSED_LOST depending on the "won" param
    }
    public TextObject toTextObject(){
        TextObject opportunityLine = new TextObject();
        //TODO opportunityLine.addText(value) for each value to be printed in console when opportunities list is shown
        opportunityLine.setAllTextBackground(getStatus()==OpportunityStatus.OPEN? BgColors.BRIGHT_BLUE: BgColors.RED);
        return opportunityLine;
    }
}
