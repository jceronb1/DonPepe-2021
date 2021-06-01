package com.example.donpepe.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MapsService {

    @GET("api/directions/json")
    Call<ResponseBody> directions(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key);
}
