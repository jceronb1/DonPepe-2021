package com.example.donpepe.serializers;

import com.example.donpepe.models.Seller;
import com.google.gson.Gson;

public class SignInSerializer {

    private String uid;
    private String password;

    public SignInSerializer(){

    }

    public SignInSerializer(String uid, String password){
        this.uid = uid;
        this.password = password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static final String asJson(String email, String password){
        SignInSerializer aux = new SignInSerializer(email, password);
        Gson gson = new Gson();
        return gson.toJson(aux);
    }
}
