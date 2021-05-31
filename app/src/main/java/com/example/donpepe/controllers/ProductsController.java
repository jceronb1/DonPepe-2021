package com.example.donpepe.controllers;

import com.example.donpepe.services.ProductsService;
import com.example.donpepe.services.UsersService;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class ProductsController {

    public static final Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:3000/").build();
    private static final ProductsService productsService = retrofit.create(ProductsService.class);

    public static final Call<ResponseBody> index(int page){
        return productsService.index(page);
    }

    public static final Call<ResponseBody> index(int page, String token){
        return productsService.index(page, token);
    }

    public static final Call<ResponseBody> show(String id, String token){
        return productsService.show(id, token);
    }

    public static final Call<ResponseBody> show(String id ){
        return productsService.show(id);
    }

    public static final Call<ResponseBody> byCategory(int page, String category){
        return productsService.byCategory(page, category);
    }

    public static final Call<ResponseBody> addToCart(String id, String token){
        return productsService.addToCart(id, token);
    }
    public static final Call<ResponseBody> removeFromCart(String id, String token){
        return productsService.removeFromCart(id, token);
    }





}
