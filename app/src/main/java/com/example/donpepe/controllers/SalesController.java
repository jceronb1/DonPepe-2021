package com.example.donpepe.controllers;

import com.example.donpepe.services.ProductsService;
import com.example.donpepe.services.SalesService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class SalesController {
    public static final Retrofit retrofit = new Retrofit.Builder().baseUrl("https://donpepe.herokuapp.com/").build();
    private static final SalesService salesService = retrofit.create(SalesService.class);

    public static final Call<ResponseBody> index(String token){
        return salesService.index(token);
    }

    public static final Call<ResponseBody> show(String id, String token){
        return salesService.show(id, token);
    }

    public static final Call<ResponseBody> updateStatus(String id, String token){
        return salesService.updateStatus(id, token );
    }
}
