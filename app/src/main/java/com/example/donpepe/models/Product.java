package com.example.donpepe.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Product {

    private String id;
    private String name;
    private String description;
    private Number price;
    private ArrayList<String> images;
    private Seller seller;
    private String category;

    private String image;


    public Product(String name, String description, Number price, String  image, Seller seller, String category){
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.images = new ArrayList<>();
        this.images.add(this.image);
        this.seller = seller;
        this.category = category;
    }

    public Product(){

    }

    public  String getId(){
        return this.id;
    }

    public  void setId(String id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;

    }

    public void setImage(String image) {
        this.images = new ArrayList<>();
        this.images.add(image);
        this.image = image;

    }
}
