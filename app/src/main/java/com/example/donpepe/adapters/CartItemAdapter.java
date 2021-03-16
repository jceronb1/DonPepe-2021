package com.example.donpepe.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donpepe.R;
import com.example.donpepe.models.Product;

import java.util.ArrayList;

public class CartItemAdapter extends BaseAdapter {

    ArrayList<Product> products;
    LayoutInflater iflter;

    public CartItemAdapter(Context appContext, ArrayList<Product> products){
        this.products = products;
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

            itemName.setText(this.products.get(position).name);
            itemPrice.setText(String.valueOf(this.products.get(position).price));
            drawable = view.getResources().getDrawable(view.getResources().getIdentifier(
                    this.products.get(position).image,
                    "drawable",
                    "com.example.donpepe"
            ));
        View finalView = view;
        deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(finalView.getContext(), "The item is removed...", Toast.LENGTH_SHORT);
                }
            });
        itemImage.setImageDrawable(drawable);
        return view;
    }
}
