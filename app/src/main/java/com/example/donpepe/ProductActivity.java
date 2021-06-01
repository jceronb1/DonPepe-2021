package com.example.donpepe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.donpepe.controllers.ProductsController;
import com.example.donpepe.models.Product;
import com.example.donpepe.models.Seller;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;;import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProductActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String productId;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        currentUser = mAuth.getCurrentUser();

        TextView productName = (TextView) findViewById(R.id.productNameText);
        ImageView productImage = (ImageView) findViewById(R.id.productImage);
        TextView categoryName = (TextView) findViewById(R.id.categoryNameText);
        TextView price = (TextView) findViewById(R.id.priceText);
        TextView description = (TextView) findViewById(R.id.descriptionText);
        TextView sellerName = (TextView) findViewById(R.id.sellerNameText);
        TextView sellerAddress = (TextView) findViewById(R.id.sellerAddrText);
        Button buyButton = (Button) findViewById(R.id.buyButton);

        if(currentUser != null){

            buyButton.setText("Comprar");
            buyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sp = getSharedPreferences("myPrefs", MODE_PRIVATE);
                    String token = sp.getString("token", "");
                    Call<ResponseBody> addCall = ProductsController.addToCart(product.getId(), token);
                    addCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
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

        this.productId = getIntent().getStringExtra("productId");
        Call<ResponseBody> showCall = null;
        if(currentUser != null){
            SharedPreferences sp = getSharedPreferences("myPrefs", MODE_PRIVATE);
            String token = sp.getString("token", "");
            showCall = ProductsController.show(this.productId, token);
        }else{
            showCall = ProductsController.show(this.productId);
        }

        ProductActivity me = this;
        showCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Gson gson = new Gson();
                try {
                    product = gson.fromJson(response.body().string(), Product.class);
                    System.out.println("ABOUT TO EVALUATE");
                    if(product.getSeller().getUid().equals(currentUser.getUid())){
                        System.out.println("MAKE SENSE");
                        buyButton.setVisibility(View.INVISIBLE);
                    }
                    productName.setText(product.getName());
                    categoryName.setText(product.getCategory());
                    price.setText(String.valueOf(product.getPrice()));
                    description.setText(product.getDescription());
                    sellerName.setText(product.getSeller().getEmail());
                    sellerAddress.setText(product.getSeller().getAddress());
                    Glide.with(me)
                    .asBitmap()
                    .load(product.getImages().get(0))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Bitmap myBitmap = Bitmap.createScaledBitmap(resource, 120, 160, false);
                            productImage.setImageBitmap(myBitmap);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

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


        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}