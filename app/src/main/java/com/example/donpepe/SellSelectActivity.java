package com.example.donpepe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SellSelectActivity extends AppCompatActivity {

    boolean loggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_select);
        if(getIntent().hasExtra("loggedIn")){
            loggedIn = true ;
        }

        ImageButton hc = (ImageButton) findViewById(R.id.selectHomeButton);
        ImageButton tc = (ImageButton) findViewById(R.id.selectTechCategory);
        ImageButton pc = (ImageButton) findViewById(R.id.selectPetsCategory);
        ImageButton vc = (ImageButton) findViewById(R.id.selectVehiclesCategory);

        hc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SellActivity.class);
                intent.putExtra("loggedIn", "true");
                intent.putExtra("selectedCategory", "Home");
                startActivity(intent);
            }
        });

        tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SellActivity.class);
                intent.putExtra("loggedIn", "true");
                intent.putExtra("selectedCategory", "Tech");
                startActivity(intent);
            }
        });

        pc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SellActivity.class);
                intent.putExtra("loggedIn", "true");
                intent.putExtra("selectedCategory", "Pets");
                startActivity(intent);
            }
        });

        vc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SellActivity.class);
                intent.putExtra("loggedIn", "true");
                intent.putExtra("selectedCategory", "Vehicles");
                startActivity(intent);
            }
        });
    }
}