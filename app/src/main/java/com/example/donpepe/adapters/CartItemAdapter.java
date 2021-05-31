package com.example.donpepe.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.donpepe.R;
import com.example.donpepe.controllers.ProductsController;
import com.example.donpepe.controllers.UsersController;
import com.example.donpepe.models.Product;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartItemAdapter extends BaseAdapter {

    ArrayList<Product> products;
    LayoutInflater iflter;
    String token;

    public CartItemAdapter(Context appContext, ArrayList<Product> products, String token){
        this.products = products;
        this.token = token;
        this.iflter = (LayoutInflater.from(appContext));
    }
    @Override
    public int getCount() {
        return this.products.size();
    }

    @Override
    public Object getItem(int position) {
        if(position < this.products.size()){
            return this.products.get(position % this.products.size());
        }else{
            return this.products.get(this.products.size()-1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = this.iflter.inflate(R.layout.activity_cart_item, null);
        TextView itemName = (TextView) view.findViewById(R.id.cartItemName);
        ImageView itemImage = (ImageView) view.findViewById(R.id.cartItemImage);
        TextView itemPrice = (TextView) view.findViewById(R.id.cartItemPrice);
        Button deleteButton = (Button) view.findViewById(R.id.deleteButton);
        Drawable drawable;
        if(position < this.products.size()){
            itemName.setText(this.products.get(position).getName());
            itemPrice.setText(String.valueOf("$" + this.products.get(position).getPrice() + " COP"));
            Glide.with(view).load(this.products.get(position).getImages().get(0)).apply(new RequestOptions().override(200, 250)).into(itemImage);
        }else{
            itemName.setText(this.products.get(this.products.size()-1).getName());
            itemPrice.setText(String.valueOf("$" + this.products.get(this.products.size()-1).getPrice() + " COP"));
            Glide.with(view).load(this.products.get(this.products.size()-1).getImages().get(0)).apply(new RequestOptions().override(200, 250)).into(itemImage);
        }
        View finalView = view;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteButton.setEnabled(false);
                Call<ResponseBody> removeCall = null;
                if(position < products.size()){
                    removeCall = ProductsController.removeFromCart(products.get(position).getId(), token);
                }else{
                    removeCall = ProductsController.removeFromCart(products.get(products.size()-1).getId(), token);
                }
                removeCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(position < products.size()){
                            products.remove(position);
                        }else{
                            products.remove(products.size() -1);
                        }
                        notifyDataSetChanged();
                        Toast.makeText(finalView.getContext(), "The item was removed...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
        return finalView;
    }
}
