package com.example.donpepe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Button saveAddrButton = (Button) findViewById(R.id.saveAddressButton);
        saveAddrButton.setVisibility(View.INVISIBLE);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        EditText searchAddrText = (EditText) findViewById(R.id.searchAddrText);

        searchAddrText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Button saveAddrButton = (Button) findViewById(R.id.saveAddressButton);
                saveAddrButton.setVisibility(View.VISIBLE);
                System.out.println("On Editor Action");
                Geocoder geocoder = new Geocoder(getBaseContext());
                String addr = searchAddrText.getText().toString();
                if(!addr.isEmpty()){
                    try {
                        List<Address> addresses = geocoder.getFromLocationName(addr,2);
                        if (addresses != null && !addresses.isEmpty()) {
                            Address addressResult =addresses.get(0);
                            System.out.println(addressResult.getFeatureName());
                            LatLng position = new LatLng(addressResult.getLatitude(),addressResult.getLongitude());
                            if (map != null){
                                Marker marker = map.addMarker(new MarkerOptions().position(position).title("Marcador dirección usuario"));
                                map.moveCamera(CameraUpdateFactory.newLatLng(position));
                                saveAddrButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        String addressName = new String();
                                        EditText searchAddrText = (EditText) findViewById(R.id.searchAddrText);
                                        addressName = searchAddrText.getText().toString();
                                        intent.putExtra("address", addressName);
                                        intent.putExtra("lat", marker.getPosition().latitude);
                                        intent.putExtra("lon", marker.getPosition().longitude);
                                        setResult(RESULT_OK, intent);
                                        finish();
                                    }
                                });
                            }
                        } else {
                            System.out.println("Empty list of dirs");
                            Toast.makeText(MapsActivity.this, "Dirección no encontrada",Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch(IOException e){
                        System.out.println("Paila mi perro Exception");
                        e.printStackTrace();
                    }
                }else{
                    System.out.println("Paila mi perro empty");
                    Toast.makeText(getApplicationContext(), "Empty dir not allowed", Toast.LENGTH_SHORT);
                }
                return true;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("MAP READY");
        map = googleMap;
        LatLng bogota = new LatLng(4.65, -74.05);
        //map.addMarker(new MarkerOptions().position(bogota).title("Marcador en Bogota"));
        map.moveCamera(CameraUpdateFactory.newLatLng(bogota));

        map.moveCamera(CameraUpdateFactory.zoomTo(15));
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
    }
}