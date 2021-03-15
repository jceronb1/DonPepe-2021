package com.example.donpepe.models;

public class Seller {

    public String name;
    public String email;
    public String password;
    public String address;
    public String image;

    public Seller(String name, String email, String password, String address, String image){

    }

    public Seller(String name, String address){
        this.name = name;
        this.email = "";
        this.password = "";
        this.address = address;
        this.image = "";
    }
}
