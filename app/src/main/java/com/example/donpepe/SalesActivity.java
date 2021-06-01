package com.example.donpepe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.donpepe.adapters.PurchaseItemAdapter;
import com.example.donpepe.controllers.PurchasesController;
import com.example.donpepe.controllers.SalesController;
import com.example.donpepe.models.Purchase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String token;
    private ArrayList<Purchase> sales = new ArrayList<Purchase>();

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            SharedPreferences sp = getSharedPreferences("myPrefs", MODE_PRIVATE);
            token = sp.getString("token","");
            initSales();
        }

    }

    private void initSales(){
        Call<ResponseBody> indexCall = SalesController.index(token);
        indexCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new Gson();
                try {
                    String auxBody = response.body().string();
                    JsonParser parser = new JsonParser();
                    JsonElement jsonArr = parser.parse(auxBody);
                    for(JsonElement jsonElement : (JsonArray)jsonArr){
                        sales.add( gson.fromJson(jsonElement, Purchase.class) );
                    }
                    PurchaseItemAdapter adapter = new PurchaseItemAdapter(getApplicationContext(), sales, "buyer");
                    ListView purchasesList = (ListView) findViewById(R.id.salesList);
                    purchasesList.setAdapter(adapter);
                    purchasesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(view.getContext(), SaleActivity.class);
                            intent.putExtra("purchaseId", sales.get(position).getId());
                            startActivity(intent);
                        }
                    });

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