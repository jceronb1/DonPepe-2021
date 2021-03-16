package com.example.donpepe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;

import com.example.donpepe.models.Product;
import com.example.donpepe.models.Seller;;


public class ProductActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        boolean loggedIn = false;
        TextView productName = (TextView) findViewById(R.id.productNameText);
        ImageView productImage = (ImageView) findViewById(R.id.productImage);
        TextView categoryName = (TextView) findViewById(R.id.categoryNameText);
        TextView price = (TextView) findViewById(R.id.priceText);
        TextView description = (TextView) findViewById(R.id.descriptionText);
        TextView sellerName = (TextView) findViewById(R.id.sellerNameText);
        TextView sellerAddress = (TextView) findViewById(R.id.sellerAddrText);
        Button buyButton = (Button) findViewById(R.id.buyButton);
        if(getIntent().hasExtra("loggedIn")){
            loggedIn = true;
            buyButton.setText("Comprar");
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            buyButton.setText("Sign up");
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                    startActivity(intent);
                }
            });
        }




        productName.setText(getIntent().getStringExtra("name"));
        Drawable drawable = getDrawable(getResources().getIdentifier(
                getIntent().getStringExtra("image"),
                "drawable",
                "com.example.donpepe"
        ));
        productImage.setImageDrawable(drawable);
        categoryName.setText(getIntent().getStringExtra("category"));
        price.setText(String.valueOf(getIntent().getLongExtra("price", 0)));
        description.setText(getIntent().getStringExtra("description"));
        sellerName.setText(getIntent().getStringExtra("seller_name"));
        sellerAddress.setText(getIntent().getStringExtra("seller_address"));

        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}