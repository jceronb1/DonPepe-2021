package com.example.donpepe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.donpepe.adapters.CartItemAdapter;
import com.example.donpepe.adapters.ProductItemAdapter;
import com.example.donpepe.models.Product;
import com.example.donpepe.models.Seller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ArrayList<String> strArreglo;
    ArrayList<Product> arreglo;
    boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        this.strArreglo = new ArrayList<String>();
        this.arreglo = new ArrayList<Product>();
        initProducts();
        CartItemAdapter adapter = new CartItemAdapter(this, arreglo);
        ListView cartList = (ListView) findViewById(R.id.cartList);
        cartList.setAdapter(adapter);

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
}