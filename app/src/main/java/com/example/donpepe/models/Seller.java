package com.example.donpepe.models;

public class Seller {

    private String uid;
    private String email;
    private String password;
    private String passwordConfirmation;
    private String phoneNumber;
    private String address;
    private double lat;
    private double lon;
    private String imageUrl;

    private String name;
    private String image;

    public Seller(String uid, String name, String email, String password, String address, String image){

    }

    public Seller(String uid, String name, String email, String password, String passwordConfirmation, String address, String image){

    }

    public Seller(String uid, String name, String address){
        this.name = name;
        this.email = "";
        this.password = "";
        this.address = address;
        this.image = "";
    }

    // methodsToUse

    public Seller(String uid, String email, String password, String passwordConfirmation, String phoneNumber, String address, double lat, double lon, String imageUrl){
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.imageUrl = imageUrl;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
