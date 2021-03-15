package com.example.donpepe.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.donpepe.MainActivity;
import com.example.donpepe.R;
import com.example.donpepe.models.Product;

import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;

public class ProductItemAdapter extends BaseAdapter {

    ArrayList<Product> products;
    LayoutInflater iflter;

    public ProductItemAdapter(Context appContext, ArrayList<Product> products ){
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
        view = this.iflter.inflate(R.layout.activity_product_item, null);
        TextView itemName = (TextView) view.findViewById(R.id.itemName);
        ImageView itemImage = (ImageView) view.findViewById(R.id.itemImage);
        Drawable drawable;
        if(position < this.products.size()){
            itemName.setText(this.products.get(position).name);
             drawable = view.getResources().getDrawable(view.getResources().getIdentifier(
                    this.products.get(position).image,
                    "drawable",
                    "com.example.donpepe"
            ));
        }else{
            itemName.setText(this.products.get(this.products.size()-1).name);
             drawable = view.getResources().getDrawable(view.getResources().getIdentifier(
                    this.products.get(this.products.size()-1).image,
                    "drawable",
                    "com.example.donpepe"
            ));
        }
        itemImage.setImageDrawable(drawable);
        return view;
    }
}
