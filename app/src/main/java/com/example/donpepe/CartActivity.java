package com.example.donpepe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.donpepe.adapters.CartItemAdapter;
import com.example.donpepe.adapters.ProductItemAdapter;
import com.example.donpepe.controllers.ProductsController;
import com.example.donpepe.controllers.PurchasesController;
import com.example.donpepe.controllers.SalesController;
import com.example.donpepe.controllers.UsersController;
import com.example.donpepe.models.Product;
import com.example.donpepe.models.Seller;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser;
    String token;
    ArrayList<Product> products =  new ArrayList<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://donpepe-4e523-default-rtdb.firebaseio.com/");
    private DatabaseReference ref;

    @Override
    protected void onStart() {
        super.onStart();
        initProducts();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        currentUser = mAuth.getCurrentUser();
        SharedPreferences sp = getSharedPreferences("myPrefs", MODE_PRIVATE);
        token = sp.getString("token","");

        Button buyButton = (Button) findViewById(R.id.cartBuyButton);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> buyCall = PurchasesController.create(token);
                buyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        for(Product x: products){
                            ref = database.getReference(x.getSeller().getUid() + "/sales").child(x.getId());
                            ref.setValue(0);
                        }
                        Intent intent = new Intent(getApplicationContext(), PurchasesActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void initProducts(){
        products = new ArrayList<>();
        Call<ResponseBody> cartCall = UsersController.cart(token);
        cartCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new Gson();
                try {
                    String auxBody = response.body().string();
                    JsonParser parser = new JsonParser();
                    JsonElement jsonObj = parser.parse(auxBody);
                    JsonElement jsonItems = jsonObj.getAsJsonObject().get("items");
                    for(JsonElement jsonElement: (JsonArray) jsonItems){
                        products.add(gson.fromJson(jsonElement.getAsJsonObject().get("product"), Product.class));
                    }
                    CartItemAdapter adapter = new CartItemAdapter(getApplicationContext(), products, token);
                    ListView cartList = (ListView) findViewById(R.id.cartList);
                    cartList.setAdapter(adapter);
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