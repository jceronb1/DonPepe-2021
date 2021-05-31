package com.example.donpepe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.donpepe.adapters.ProductItemAdapter;
import com.example.donpepe.controllers.ProductsController;
import com.example.donpepe.models.Product;
import com.example.donpepe.models.Seller;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser;
    String category;
    int page = 1;
    HashMap<Integer, Integer> pageItems = new HashMap<>();
    ArrayList<Product> products = new ArrayList<Product>();;
    boolean loggedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        currentUser = mAuth.getCurrentUser();

        this.category = getIntent().getStringExtra("category");
        initProducts();
    }

    private void initProducts(){
        nextProductsPage();
    }

    private void nextProductsPage(){
        Call<ResponseBody> byCategoryCall = ProductsController.byCategory(page, category);
        byCategoryCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                page += 1;
                Gson gson = new Gson();
                try {
                    String auxBody = response.body().string();
                    JsonParser parser = new JsonParser();
                    JsonElement jsonArr = parser.parse(auxBody);
                    for(JsonElement jsonElement : (JsonArray)jsonArr){
                        products.add( gson.fromJson(jsonElement, Product.class) );
                    }
                    ProductItemAdapter adapter = new ProductItemAdapter(getApplicationContext(), products);
                    ListView categoryList = (ListView) findViewById(R.id.categoryList);
                    categoryList.setAdapter(adapter);
                    int totalItems = pageItems.values().stream().mapToInt(element -> element).sum();
                    System.out.println("MOVING TO " + (totalItems-1));
                    categoryList.setSelection(totalItems-1);
                    pageItems.put(page,((JsonArray) jsonArr).size());
                    categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(view.getContext(), ProductActivity.class);
                            intent.putExtra("productId", products.get(position).getId());
                            startActivity(intent);
                        }
                    });

                    AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            int lastItem = firstVisibleItem + visibleItemCount;
                            if(lastItem == totalItemCount && pageItems.get(page) != 0){
                                categoryList.setOnScrollListener(null);
                                System.out.println("CALLING NEXT PRODUCTS PAGE " + page );
                                nextProductsPage();
                            }
                        }
                    };
                    categoryList.setOnScrollListener(listener);
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