package com.example.donpepe.models;

public class Product {

    public String name;
    public String description;
    public Number price;
    public String image;
    public Seller seller;
    public String category;

    public Product(String name, String description, Number price, String  image, Seller seller, String category){
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.seller = seller;
        this.category = category;
    }
}
