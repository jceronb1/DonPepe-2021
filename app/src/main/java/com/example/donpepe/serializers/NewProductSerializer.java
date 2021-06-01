package com.example.donpepe.serializers;

import com.example.donpepe.models.Product;
import com.example.donpepe.models.Seller;
import com.google.gson.Gson;

import java.util.ArrayList;

public class NewProductSerializer {

    private String name;
    private String description;
    private Number price;
    private ArrayList<String> images;
    private String category;

    public NewProductSerializer(Product x, String category){
        this.name = x.getName();
        this.description = x.getDescription();
        this.price = x.getPrice();
        this.images = x.getImages();
        this.category = category;
    }

    public static final String asJson(Product newProduct, String category){
        Gson gson = new Gson();
        NewProductSerializer aux = new NewProductSerializer(newProduct, category);
        return gson.toJson(aux);
    }

}
