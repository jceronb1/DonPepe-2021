package com.example.donpepe.controllers;

import com.example.donpepe.services.ProductsService;
import com.example.donpepe.services.PurchasesService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class PurchasesController {

    public static final Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:3000/").build();
    private static final PurchasesService purchasesService = retrofit.create(PurchasesService.class);

    public static final Call<ResponseBody> index(String token){
        return purchasesService.index(token);
    }

    public static final Call<ResponseBody> show(String id, String token){
        return purchasesService.show(id, token);
    }

    public static final Call<ResponseBody> create(String token){
        return purchasesService.create(token);
    }
}
