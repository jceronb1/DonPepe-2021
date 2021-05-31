package com.example.donpepe.services;

import com.example.donpepe.models.Seller;
import com.example.donpepe.models.Session;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UsersService {

    @POST("api/users/signup")
    Call<ResponseBody> signUp(@Body RequestBody body);

    @POST("api/users/signin")
    Call<ResponseBody> signIn(@Body RequestBody body);

    @GET("api/users/cart")
    Call<ResponseBody> cart(@Header("X-DONPEPE-TOKEN") String token);

}
