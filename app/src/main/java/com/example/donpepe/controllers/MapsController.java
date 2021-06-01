package com.example.donpepe.controllers;

import com.example.donpepe.services.MapsService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class MapsController {

    public static final Retrofit retrofit = new Retrofit.Builder().baseUrl("https://maps.googleapis.com/maps/").build();
    public static final MapsService mapsService = retrofit.create(MapsService.class);

    public static final Call<ResponseBody> directions(Double originLat, Double originLon, Double desLat, Double desLon){
        String origin = originLat.toString() + "," + originLon.toString();
        String destination = desLat.toString() + "," + desLon.toString();
        return mapsService.directions(origin , destination, "KEY DE MAPAS");
    }
}
