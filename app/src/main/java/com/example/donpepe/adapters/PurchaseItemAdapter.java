package com.example.donpepe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.donpepe.R;
import com.example.donpepe.models.Product;
import com.example.donpepe.models.Purchase;

import java.util.ArrayList;

public class PurchaseItemAdapter extends BaseAdapter {
    ArrayList<Purchase> purchases;
    LayoutInflater iflter;

    public PurchaseItemAdapter(Context appContext, ArrayList<Purchase> purchases){
        this.purchases = purchases;
        this.iflter = (LayoutInflater.from(appContext));
    }

    @Override
    public int getCount() {
        return this.purchases.size();
    }

    @Override
    public Object getItem(int position) {
        if(position < this.purchases.size()){
            return this.purchases.get(position % this.purchases.size());
        }else{
            return this.purchases.get(this.purchases.size()-1);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = this.iflter.inflate(R.layout.activity_purchase_item, null);
        TextView sellerEmailText = (TextView) convertView.findViewById(R.id.piSellerEmailText);
        TextView sellerPhoneText = (TextView) convertView.findViewById(R.id.piSellerPhoneText);
        TextView purchasePriceText = (TextView) convertView.findViewById(R.id.piPurchasePriceText);
        TextView purchaseSattusText = (TextView) convertView.findViewById(R.id.piPurchaseStatusText);
        ImageView sellerImg = (ImageView) convertView.findViewById(R.id.piSellerPurchaseImg);
        Purchase purchase = null;
        if(position < this.purchases.size()){
            purchase = this.purchases.get(position);
        }else{
            purchase = this.purchases.get(this.purchases.size()-1);
        }
        sellerEmailText.setText(purchase.getSeller().getEmail());
        sellerPhoneText.setText(purchase.getSeller().getPhoneNumber());
        purchasePriceText.setText(String.valueOf(purchase.getTotalPrice()));
        purchaseSattusText.setText(purchase.getStatus());
        Glide.with(convertView).load(purchase.getSeller().getImageUrl()).apply(new RequestOptions().override(200, 250)).into(sellerImg);
        return convertView;
    }
}
