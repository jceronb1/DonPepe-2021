package com.example.donpepe.services;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PurchasesService {

    @GET("api/purchases")
    Call<ResponseBody> index(@Header("X-DONPEPE-TOKEN") String token);

    @GET("api/purchases/{id}")
    Call<ResponseBody> show(@Path("id") String id, @Header("X-DONPEPE-TOKEN") String token);

    @POST("api/purchases")
    Call<ResponseBody> create(@Header("X-DONPEPE-TOKEN") String token);
}
