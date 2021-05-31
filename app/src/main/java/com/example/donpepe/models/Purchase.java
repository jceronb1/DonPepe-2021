package com.example.donpepe.models;

import java.util.ArrayList;

public class Purchase {

    private String id;
    private int statusCd;
    private Number totalPrice;
    private ArrayList<Product> items = new ArrayList<>();
    private Seller seller;
    private Seller buyer;

    public Purchase(String id, int statusCd, Number totalPrice, ArrayList<Product> products, Seller seller, Seller buyer) {
        this.id = id;
        this.statusCd = statusCd;
        this.totalPrice = totalPrice;
        this.items = products;
        this.seller = seller;
        this.buyer = buyer;
    }

    public String getStatus(){
        String status = "Good";
        if(this.statusCd == 0){
            status = "Preparing shipment";
        }else if(this.statusCd == 1){
             status ="Shipment in progress";
        }else if(this.statusCd == 2){
             status = "Delivered";
        }
        return status;
    }

    public Purchase() {
    }

    public int getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(int statusCd) {
        this.statusCd = statusCd;
    }

    public Number getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Number totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<Product> getItems() {
        return items;
    }

    public void setItems(ArrayList<Product> items) {
        this.items = items;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Seller getBuyer() {
        return buyer;
    }

    public void setBuyer(Seller buyer) {
        this.buyer = buyer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
