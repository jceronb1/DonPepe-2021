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
import com.example.donpepe.models.Purchase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String token;
    private String purchaseId;
    private Purchase purchase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Button followButton = (Button) findViewById(R.id.followButton);
            followButton.setVisibility(View.INVISIBLE);
            SharedPreferences sp = getSharedPreferences("myPrefs", MODE_PRIVATE);
            token = sp.getString("token", "");
            purchaseId = getIntent().getStringExtra("purchaseId");
            Call<ResponseBody> showCall = PurchasesController.show(purchaseId, token);
            Activity me = this;
            showCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Gson gson = new Gson();
                    try {
                        Purchase purchase = gson.fromJson(response.body().string(), Purchase.class);
                        TextView sellerEmailtext = (TextView) findViewById(R.id.pSellerEmailText);
                        TextView sellerPhoneText = (TextView) findViewById(R.id.pSellerPhoneText);
                        TextView purchasePriceText = (TextView) findViewById(R.id.pPurchasePriceText);
                        TextView purchaseStatusText = (TextView) findViewById(R.id.pPurchaseStatusText);
                        ImageView sellerImg = (ImageView) findViewById(R.id.pSellerPurchaseImg);
                        Button followButton = (Button) findViewById(R.id.followButton);
                        sellerEmailtext.setText(purchase.getSeller().getEmail());
                        sellerPhoneText.setText(purchase.getSeller().getPhoneNumber());
                        purchasePriceText.setText(String.valueOf(purchase.getTotalPrice()));
                        purchaseStatusText.setText(purchase.getStatus());
                        Glide.with(me).load(purchase.getSeller().getImageUrl()).apply(new RequestOptions().override(200, 250)).into(sellerImg);
                        ProductItemAdapter adapter = new ProductItemAdapter(getApplicationContext(), purchase.getItems());
                        ListView itemsList = findViewById(R.id.pItemsList);
                        itemsList.setAdapter(adapter);
                        System.out.println("getting status");
                        if(purchase.getStatusCd() != 2){
                            System.out.println("getting is not 2");
                            followButton.setVisibility(View.VISIBLE);
                            followButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    System.out.println("calling click");
                                    Intent intent = new Intent(v.getContext(), RouteActivity.class);
                                    intent.putExtra("purchaseId", purchase.getId());
                                    intent.putExtra("viewForSeller", false);
                                    startActivity(intent);
                                }
                            });
                        }else{
                            System.out.println("getting is 2");
                            followButton.setVisibility(View.INVISIBLE);
                        }
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
}