package com.example.donpepe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private String token;
    private int page = 1;
    private HashMap<Integer, Integer> pageItems = new HashMap<>();
    ArrayList<Product> products = new ArrayList<>();
    private Menu theMenu;

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        updateUi();
    }

    private void updateUi(){

        if(currentUser != null){
            SharedPreferences sp = getSharedPreferences("myPrefs", MODE_PRIVATE);
            token = sp.getString("token", null);
            System.out.println("WE FOUND A TOKEN CAP");
            System.out.println(token);
        }
        setupMenuItems();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initProducts();

        ImageButton homeButton = (ImageButton) findViewById(R.id.homeButton);
        ImageButton techButton = (ImageButton) findViewById(R.id.techCategory);
        ImageButton petsButton = (ImageButton) findViewById(R.id.petsCategory);
        ImageButton vehiclesButton = (ImageButton) findViewById(R.id.vehiclesCategory);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                intent.putExtra("category", "home");
                startActivity(intent);
            }
        });

        techButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
                intent.putExtra("category", "Tech");
                if(currentUser != null){
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
                if(currentUser != null){
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
                if(currentUser != null){
                    intent.putExtra("loggedIn", "true");
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu);
        theMenu = menu;
        setupMenuItems();
        return true;
    }

    void setupMenuItems(){
        if(theMenu != null){
            MenuItem itemCart = theMenu.getItem(0);
            MenuItem itemSales = theMenu.getItem(1);
            MenuItem itemPurchases = theMenu.getItem(2);
            MenuItem itemSell = theMenu.getItem(3);
            MenuItem itemSignout = theMenu.getItem(4);
            MenuItem itemSignin = theMenu.getItem(5);


            if(currentUser != null){
                itemSignout.setVisible(true);
                itemSell.setVisible(true);
                itemPurchases.setVisible(true);
                itemSales.setVisible(true);
                itemCart.setVisible(true);
                itemSignin.setVisible(false);
            }else{
                itemSignout.setVisible(false);
                itemSell.setVisible(false);
                itemPurchases.setVisible(false);
                itemSales.setVisible(false);
                itemCart.setVisible(false);
                itemSignin.setVisible(true);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemClicked = item.getItemId();
        if(itemClicked == R.id.itemSignOut){
            mAuth.signOut();
            currentUser = null;
            updateUi();
        }else if(itemClicked == R.id.itemSell){
            Intent intent = new Intent(getApplicationContext(), SellSelectActivity.class);
            startActivity(intent);
        }else if(itemClicked == R.id.itemPurchases){
            Intent intent = new Intent(getApplicationContext(), PurchasesActivity.class);
            startActivity(intent);
        }else if(itemClicked == R.id.itemSales){
            Intent intent = new Intent(getApplicationContext(), SalesActivity.class);
            startActivity(intent);
        }else if(itemClicked == R.id.itemCart){
            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
            startActivity(intent);
        }else if(itemClicked == R.id.itemSignIn){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        return true;
    };

    private void initProducts(){
        nextProductsPage();
    }


    private void nextProductsPage(){
        Call<ResponseBody> indexCall = null;
        if(currentUser != null){
            indexCall = ProductsController.index(page, token);
        }else{
            indexCall = ProductsController.index(page);
        }
        indexCall.enqueue(new Callback<ResponseBody>() {
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
                    ListView homeList = (ListView) findViewById(R.id.homeList);
                    homeList.setAdapter(adapter);
                    int totalItems = pageItems.values().stream().mapToInt(element -> element).sum();
                    System.out.println("MOVING TO " + (totalItems-1));
                    homeList.setSelection(totalItems-1);
                    pageItems.put(page,((JsonArray) jsonArr).size());
                    homeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                                homeList.setOnScrollListener(null);
                                System.out.println("CALLING NEXT PRODUCTS PAGE " + page );
                                nextProductsPage();
                            }
                        }
                    };
                    homeList.setOnScrollListener(listener);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(getIntent().hasExtra("loggedIn")){

            return ;
        }
    }
}