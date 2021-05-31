package com.example.donpepe.controllers;

import com.example.donpepe.models.Seller;
import com.example.donpepe.models.Session;
import com.example.donpepe.serializers.SignInSerializer;
import com.example.donpepe.serializers.SignUpSerializer;
import com.example.donpepe.services.UsersService;

import java.util.Optional;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class UsersController {

    public static final Retrofit retrofit = new Retrofit.Builder().baseUrl("http://10.0.2.2:3000/").build();
    private static final UsersService usersService = retrofit.create(UsersService.class);

    public static final Call<ResponseBody> signUp(Seller seller){
        String json = SignUpSerializer.asJson(seller);
        RequestBody body = RequestBody.create(json ,MediaType.parse("application/json"));
        return usersService.signUp(body);
    }

    public static final Call<ResponseBody> signIn(String email, String password){
        String json = SignInSerializer.asJson(email, password);
        RequestBody body = RequestBody.create(json ,MediaType.parse("application/json"));
        return usersService.signIn(body);
    }

    public static final Call<ResponseBody> cart(String token){
        return usersService.cart(token);
    }


}
