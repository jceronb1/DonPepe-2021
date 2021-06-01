package com.example.donpepe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.donpepe.controllers.MapsController;
import com.example.donpepe.controllers.PurchasesController;
import com.example.donpepe.controllers.SalesController;
import com.example.donpepe.controllers.UsersController;
import com.example.donpepe.helpers.AskPermission;
import com.example.donpepe.models.Purchase;
import com.example.donpepe.services.MapsService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.maps.android.PolyUtil;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback{

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Double currentLat;
    private Double currentLon;
    private Double otherLat;
    private Double otherLon;
    private Purchase purchase;
    private Marker currentMarker;
    private Marker otherMarker;
    private String token;
    private GoogleMap map;
    private String purchaseId;
    private String currentImg;
    private String otherImg;
    private String polyline;
    private boolean viewForSeller = false;
    public static final int REQUEST_MULTIPLE = 9999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        AskPermission.asKMultiple(
                this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                REQUEST_MULTIPLE

        );
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.routeMap);
        mapFragment.getMapAsync(this);
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mLocationRequest = createLocationRequest();
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    System.out.println("REQUESTING LOCATION");
                    android.location.Location location = locationResult.getLastLocation();
                    if (location != null) {
                        updateMyCurrentUserLocation(location);

                        //stopLocationUpdates();
                    }
                }
            };

            purchaseId = getIntent().getStringExtra("purchaseId");
            viewForSeller = getIntent().getBooleanExtra("viewForSeller", false);
            SharedPreferences sp = getSharedPreferences("myPrefs", MODE_PRIVATE);
            token = sp.getString("token", "");
            Call<ResponseBody> showCall;
            if(viewForSeller){
                showCall = SalesController.show(purchaseId, token);
            }else{
                showCall = PurchasesController.show(purchaseId, token);
            }

            showCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Gson gson = new Gson();
                    System.out.println("SHOW CALL ENDED");
                    try {
                        purchase = gson.fromJson(response.body().string(), Purchase.class);
                        if(viewForSeller){
                            System.out.println("FOR SELLER");
                            System.out.println(purchase.getBuyer().getEmail());
                            otherLat = purchase.getBuyer().getLat();
                            otherLon = purchase.getBuyer().getLon();
                            otherImg = purchase.getBuyer().getImageUrl();
                            currentImg = purchase.getSeller().getImageUrl();
                        }else{
                            System.out.println("FOR BUYER");
                            System.out.println(purchase.getSeller().getEmail());
                            otherLat = purchase.getSeller().getLat();
                            otherLon = purchase.getSeller().getLon();
                            otherImg = purchase.getSeller().getImageUrl();
                            currentImg = purchase.getBuyer().getImageUrl();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    startLocationUpdate();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    private void updateMyCurrentUserLocation(Location location){
        if(currentUser != null){
            System.out.println("UPDATING CURRENT LOCATION");
            if(
                    (currentLat == null &&
                    currentLon == null) ||
                    (currentLat != null &&
                    currentLon != null &&
                    currentLat != location.getLatitude() &&
                    currentLon != location.getLongitude())
            ){
                Call<ResponseBody> locationCall = UsersController.updateLocation(location.getLatitude(), location.getLongitude(), token);
                locationCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        System.out.println("LOCATION CALL ENDED");
                        try {
                            String auxBody = response.body().string();
                            JsonParser parser = new JsonParser();
                            JsonElement jsonBody = parser.parse(auxBody);
                            currentLat = jsonBody.getAsJsonObject().get("lat").getAsDouble();
                            currentLon = jsonBody.getAsJsonObject().get("lon").getAsDouble();
                            setupMap();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        }
    }

    private void startLocationUpdate(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000); //tasa de refresco en milisegundos
        locationRequest.setFastestInterval(4500); //máxima tasa de refresco
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        return locationRequest;
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("MAP IS READY");
        if(map == null){
            map = googleMap;
            if(currentLat != null && currentLon != null){
                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLat, currentLon)));
            }else{
                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(4.65, -74.05)));
            }
            map.moveCamera(CameraUpdateFactory.zoomTo(12));
            map.getUiSettings().setZoomGesturesEnabled(true);
            map.getUiSettings().setZoomControlsEnabled(true);
            setupMap();
        }

    }

    private void setupMap(){
        if(map != null){
            map.clear();
            currentMarker = null;
            otherMarker = null;
            System.out.println("SETTING UP MAP");
            if(currentUser != null && currentLat != null && currentLon != null && purchase != null){
                System.out.println("THINGS FOUNDED");
                LatLng position = new LatLng(currentLat, currentLon);
                if(currentMarker != null){
                    currentMarker.setPosition(position);
                }else{
                    Glide.with(this).asBitmap().load(currentImg).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            resource = Bitmap.createScaledBitmap(resource, 120, 160, false);
                            currentMarker = map.addMarker(
                                    new MarkerOptions()
                                            .position(new LatLng(currentLat, currentLon))
                                            .title("You")
                                            .icon(BitmapDescriptorFactory.fromBitmap(resource))
                            );
                        }
                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
                }

                map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(currentLat, currentLon)));

                if(otherLat != null && otherLon != null){
                    System.out.println("Other is not null");
                    position = new LatLng(otherLat, otherLon);
                    if(otherMarker != null){
                        otherMarker.setPosition(position);
                    }else{
                        Glide.with(this).asBitmap().load(otherImg).into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                resource = Bitmap.createScaledBitmap(resource, 120, 160, false);
                                otherMarker = map.addMarker(
                                        new MarkerOptions()
                                                .position(new LatLng(otherLat, otherLon))
                                                .title("Other")
                                                .icon(BitmapDescriptorFactory.fromBitmap(resource))
                                );
                            }
                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
                    }

                    Call<ResponseBody> mapsCall = MapsController.directions(currentLat, currentLon, otherLat, otherLon);
                    mapsCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String auxBody = response.body().string();
                                JsonParser parser = new JsonParser();
                                JsonObject json = (JsonObject) parser.parse(auxBody);
                                JsonArray routesJson = json.get("routes").getAsJsonArray();
                                JsonObject route = routesJson.get(0).getAsJsonObject();
                                JsonObject polyJson = route.get("overview_polyline").getAsJsonObject();
                                polyline = polyJson.get("points").getAsString();
                                map.addPolyline(new PolylineOptions().addAll(PolyUtil.decode(polyline)));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }else{
                    System.out.println("Other IS null");
                }


            }
        }
    }
}