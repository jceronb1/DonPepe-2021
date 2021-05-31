package com.example.donpepe.services;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SalesService {

    @GET("api/sales")
    Call<ResponseBody> index(@Header("X-DONPEPE-TOKEN") String token);

    @GET("api/sales/{id}")
    Call<ResponseBody> show(@Path("id") String id, @Header("X-DONPEPE-TOKEN") String token);

    @POST("api/sales/{id}/update_status")
    Call<ResponseBody> updateStatus(@Path("id") String id, @Header("X-DONPEPE-TOKEN") String token);
}
