package com.example.donpepe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SellActivity extends AppCompatActivity {


    boolean loggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        if(getIntent().hasExtra("loggedIn")){
            loggedIn = true ;
        }

        Button createButton = (Button) findViewById(R.id.createProduct);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("loggedIn", "true");
                startActivity(intent);
            }
        });
    }
}