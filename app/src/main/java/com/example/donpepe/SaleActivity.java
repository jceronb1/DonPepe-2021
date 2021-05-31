package com.example.donpepe;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.donpepe.adapters.ProductItemAdapter;
import com.example.donpepe.controllers.PurchasesController;
import com.example.donpepe.controllers.SalesController;
import com.example.donpepe.models.Purchase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaleActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String token;
    private String purchaseId;
    private Purchase purchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            SharedPreferences sp = getSharedPreferences("myPrefs", MODE_PRIVATE);
            token = sp.getString("token", "");
            purchaseId = getIntent().getStringExtra("purchaseId");
            updateUi();
        }
    }

    private void updateUi(){
        Call<ResponseBody> showCall = PurchasesController.show(purchaseId, token);
        Activity me = this;
        showCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new Gson();
                try {
                    Purchase purchase = gson.fromJson(response.body().string(), Purchase.class);
                    TextView sellerEmailtext = (TextView) findViewById(R.id.sBuyerEmailText);
                    TextView sellerPhoneText = (TextView) findViewById(R.id.sBuyerPhoneText);
                    TextView purchasePriceText = (TextView) findViewById(R.id.sSalePriceText);
                    TextView purchaseStatusText = (TextView) findViewById(R.id.sSaleStatusText);
                    ImageView sellerImg = (ImageView) findViewById(R.id.sBuyerPurchaseImg);
                    Button updateStatusButton = (Button) findViewById(R.id.updateStatusButton);
                    Button routeButton = (Button) findViewById(R.id.routeButton);

                    if(purchase.getStatusCd() == 0){
                        updateStatusButton.setText("UPDATE TO SHIPMENT IN PROGRESS");
                    }
                    if(purchase.getStatusCd() == 1){
                        updateStatusButton.setText("UPDATE TO DELIVERED");
                    }
                    if(purchase.getStatusCd() == 2){
                        updateStatusButton.setEnabled(false);
                        updateStatusButton.setVisibility(View.INVISIBLE);
                        routeButton.setEnabled(false);
                        routeButton.setVisibility(View.INVISIBLE);
                    }else{
                        updateStatusButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Call<ResponseBody> updateCall = SalesController.updateStatus(purchase.getId(), token);
                                updateCall.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        updateUi();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }
                        });

                        routeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(v.getContext(), RouteActivity.class);
                                intent.putExtra("purchaseId", purchase.getId());
                                startActivity(intent);
                            }
                        });
                    }

                    sellerEmailtext.setText(purchase.getBuyer().getEmail());
                    sellerPhoneText.setText(purchase.getBuyer().getPhoneNumber());
                    purchasePriceText.setText(String.valueOf(purchase.getTotalPrice()));
                    purchaseStatusText.setText(purchase.getStatus());
                    Glide.with(me).load(purchase.getBuyer().getImageUrl()).apply(new RequestOptions().override(200, 250)).into(sellerImg);
                    ProductItemAdapter adapter = new ProductItemAdapter(getApplicationContext(), purchase.getItems());
                    ListView itemsList = findViewById(R.id.sItemsList);
                    itemsList.setAdapter(adapter);
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