package com.example.donpepe.models;

public class Session {

    private String token;

    public Session(){};

    public Session(String token){

    }

    public String getToken(){
        return this.token;
    }

    public void setToken(String token){
        this.token = token;
    }

}
