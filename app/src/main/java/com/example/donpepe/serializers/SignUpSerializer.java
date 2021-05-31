package com.example.donpepe.serializers;

import com.example.donpepe.models.Seller;
import com.google.gson.Gson;

public class SignUpSerializer {

    private String uid;
    private String email;
    private String password;
    private String passwordConfirmation;
    private String phoneNumber;
    private String address;
    private double lat;
    private double lon;
    private String imageUrl;

    public SignUpSerializer() {
    }

    public SignUpSerializer(Seller seller) {
        this.uid = seller.getUid();
        this.email = seller.getEmail();
        this.password = seller.getPassword();
        this.passwordConfirmation = seller.getPasswordConfirmation();
        this.phoneNumber = seller.getPhoneNumber();
        this.address = seller.getAddress();
        this.lat = seller.getLat();
        this.lon = seller.getLon();
        this.imageUrl = seller.getImageUrl();
    }

    public static final String asJson(Seller seller){
        SignUpSerializer aux = new SignUpSerializer(seller);
        Gson gson = new Gson();
        return gson.toJson(aux);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
