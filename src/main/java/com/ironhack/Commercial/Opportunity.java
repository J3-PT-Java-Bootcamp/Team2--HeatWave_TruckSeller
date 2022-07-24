package com.ironhack.Commercial;

import com.ironhack.CRMManager.CRMData;
import com.ironhack.CRMManager.User;
import com.ironhack.Constants.ColorFactory.BgColors;
import com.ironhack.Constants.OpportunityStatus;
import com.ironhack.Constants.Product;
import com.ironhack.ScreenManager.Text.TextObject;


public class Opportunity implements Printable{
    private String id;
    private Product product;
    private int quantity;
    private Contact decisionMaker;
    private OpportunityStatus status;
    private Account account;
    private User owner;

    public Opportunity(String id, Product product, int quantity, Contact decisionMaker, OpportunityStatus status, User owner) {
        setId(id);
        setProduct(product);
        setQuantity(quantity);
        setDecisionMaker(decisionMaker);
        setStatus(status);
        setOwner(owner);
    }
    public Opportunity( Product product, int quantity, Contact decisionMaker, OpportunityStatus status, User owner) {
        setId(com.ironhack.CRMManager.CRMManager.crmData.getNextID(getClass()));
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
    @Override
    public TextObject toTextObject(){
        //TODO
        return new TextObject();
    }

    @Override
    public String shortPrint() {
        return this.account.getCompanyName();
    }

    @Override
    public TextObject printFullObject() {
        return null;
    }
}
