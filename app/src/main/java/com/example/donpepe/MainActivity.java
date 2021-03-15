package com.example.donpepe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.donpepe.adapters.ProductItemAdapter;
import com.example.donpepe.models.Product;
import com.example.donpepe.models.Seller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> strArreglo;
    ArrayList<Product> arreglo;
    boolean loggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getIntent().hasExtra("loggedIn")){
            loggedIn = true ;
        }

        this.strArreglo = new ArrayList<String>();
        this.arreglo = new ArrayList<Product>();
        initProducts();
        ProductItemAdapter adapter = new ProductItemAdapter(this, arreglo);
        ListView homeList = (ListView) findViewById(R.id.homeList);
        homeList.setAdapter(adapter);

        ImageButton homeButton = (ImageButton) findViewById(R.id.homeButton);
        ImageButton techButton = (ImageButton) findViewById(R.id.techCategory);
        ImageButton petsButton = (ImageButton) findViewById(R.id.petsCategory);
        ImageButton vehiclesButton = (ImageButton) findViewById(R.id.vehiclesCategory);

        homeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject json = null;
                try {
                    json = new JSONObject(loadJSONFromAsset());
                    JSONArray  productsJsonArray = json.getJSONArray("products");
                    JSONObject jsonObject = productsJsonArray.getJSONObject(position);
                    JSONObject jsonObjectSeller = jsonObject.getJSONObject("seller");
                    Seller seller = new Seller(
                            jsonObjectSeller.getString("name"),
                            jsonObjectSeller.getString("address")
                    );

                    Intent intent = new Intent(view.getContext(), ProductActivity.class);
                    intent.putExtra("name", jsonObject.getString("name"));
                    intent.putExtra("price", jsonObject.getLong("price"));
                    intent.putExtra("description", jsonObject.getString("description"));
                    intent.putExtra("image", jsonObject.getString("image"));
                    intent.putExtra("category", jsonObject.getString("category"));
                    intent.putExtra("seller_name", seller.name);
                    intent.putExtra("seller_address", seller.address);
                    System.out.println(loggedIn);
                    System.out.println(getIntent().getStringExtra("loggedIn"));
                    if(loggedIn){
                        intent.putExtra("loggedIn", "true");
                    }
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                intent.putExtra("category", "home");
                if(loggedIn){
                    intent.putExtra("loggedIn", "true");
                }
                startActivity(intent);
            }
        });

        techButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                intent.putExtra("category", "Tech");
                if(loggedIn){
                    intent.putExtra("loggedIn", "true");
                }
                startActivity(intent);
            }
        });

        petsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                intent.putExtra("category", "Pets");
                if(loggedIn){
                    intent.putExtra("loggedIn", "true");
                }
                startActivity(intent);
            }
        });

        vehiclesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                intent.putExtra("category", "Vehicles");
                if(loggedIn){
                    intent.putExtra("loggedIn", "true");
                }
                startActivity(intent);
            }
        });
    }

    private void initProducts(){
        JSONObject json = null;
        try {
            json = new JSONObject(loadJSONFromAsset());
            JSONArray productsJsonArray = json.getJSONArray("products");

            for(int i = 0; i<productsJsonArray.length(); i++)
            {
                JSONObject jsonObject = productsJsonArray.getJSONObject(i);
                JSONObject jsonObjectSeller = jsonObject.getJSONObject("seller");
                Seller seller = new Seller(
                        jsonObjectSeller.getString("name"),
                        jsonObjectSeller.getString("address")
                );
                Product product = new Product(
                        jsonObject.getString("name"),
                        jsonObject.getString("description"),
                        jsonObject.getLong("price"),
                        jsonObject.getString("image"),
                        seller,
                        jsonObject.getString("category")
                );
                strArreglo.add(product.name);
                arreglo.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {  String json = null;
        try {
            InputStream is = this.getAssets().open("products.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();  return null;
        }
        return json;
    }

    @Override
    public void onBackPressed() {
        System.out.println(">JLJSDLKJASKLDJASLKASDLJ");
        if(getIntent().hasExtra("loggedIn")){

            return ;
        }
    }
}