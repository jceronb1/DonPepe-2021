package com.example.donpepe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.donpepe.adapters.ProductItemAdapter;
import com.example.donpepe.models.Product;
import com.example.donpepe.models.Seller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    String category;
    ArrayList<String> strArreglo;
    ArrayList<Product> arreglo;
    boolean loggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        if(getIntent().hasExtra("loggedIn")){
            loggedIn = true ;
        }

        this.strArreglo = new ArrayList<String>();
        this.arreglo = new ArrayList<Product>();
        this.category = getIntent().getStringExtra("category");
        initProducts();
        ProductItemAdapter adapter = new ProductItemAdapter(this, arreglo);
        ListView categoryList = (ListView) findViewById(R.id.categoryList);
        categoryList.setAdapter(adapter);

        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(view.getContext(), ProductActivity.class);
                    intent.putExtra("name", arreglo.get(position).name);
                    intent.putExtra("price", arreglo.get(position).price);
                    intent.putExtra("description", arreglo.get(position).description);
                    intent.putExtra("image", arreglo.get(position).image);
                    intent.putExtra("category", arreglo.get(position).category);
                    intent.putExtra("seller_name", arreglo.get(position).seller.name);
                    intent.putExtra("seller_address", arreglo.get(position).seller.address);
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

            int count = 0;
            for(int i = 0; i<productsJsonArray.length(); i++)
            {
                JSONObject jsonObject = productsJsonArray.getJSONObject(i);
                if(this.category.equals(jsonObject.getString("category"))){
                    count++;
                }
            }

            count = 0;
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
                if(product.category.equals(this.category)){
                        strArreglo.add(product.name);
                        arreglo.add(product);
                        count++;
                }
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
}