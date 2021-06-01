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
import retrofit2.http.Query;

public interface ProductsService {

    @GET("api/products")
    Call<ResponseBody> index(@Query("page") int page);

    @GET("api/products")
    Call<ResponseBody> index(@Query("page") int page, @Header("X-DONPEPE-TOKEN") String token);

    @GET("api/products/{id}")
    Call<ResponseBody> show(@Path("id") String id, @Header("X-DONPEPE-TOKEN") String token);

    @GET("api/products/{id}")
    Call<ResponseBody> show(@Path("id") String id);

    @GET("api/products")
    Call<ResponseBody> byCategory(@Query("page") int page, @Query("category") String category);

    @POST("api/products")
    Call<ResponseBody> create(@Body RequestBody body, @Header("X-DONPEPE-TOKEN") String token);

    @POST("api/products/{id}/add_to_cart")
    Call<ResponseBody> addToCart(@Path("id") String id, @Header("X-DONPEPE-TOKEN") String token);

    @POST("api/products/{id}/remove_from_cart")
    Call<ResponseBody> removeFromCart(@Path("id") String id, @Header("X-DONPEPE-TOKEN") String token);
}
