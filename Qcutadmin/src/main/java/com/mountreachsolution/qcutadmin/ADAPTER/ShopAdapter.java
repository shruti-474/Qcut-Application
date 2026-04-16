package com.mountreachsolution.qcutadmin.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mountreachsolution.qcutadmin.POJO.Shop;
import com.mountreachsolution.qcutadmin.R;
import com.mountreachsolution.qcutadmin.comman.url;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> {
    List<Shop> shopList;
    Context context;

    public ShopAdapter(List<Shop> shopList, Context context) {
        this.shopList = shopList;
        this.context = context;
    }

    @NonNull
    @Override
    public ShopAdapter.ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop, parent, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopAdapter.ShopViewHolder holder, int position) {
        Shop shop = shopList.get(position);
        holder.tvName.setText(shop.getName());
        holder.tvAddress.setText(shop.getAddress());
        holder.tvDay.setText("Open for: "+ shop.getDay());
        holder.tvTime.setText("Time: "+ shop.getTime());
        holder.tvType.setText("Type: " + shop.getType());
        Glide.with(context)
                .load(url.imageaddress+shop.getImage()) // change URL accordingly
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.ivShop);




    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public class ShopViewHolder extends RecyclerView.ViewHolder {
        ImageView ivShop;
        TextView tvName, tvAddress, tvDay, tvTime, tvType;
        CardView cardShop;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            ivShop = itemView.findViewById(R.id.ivShop);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDay = itemView.findViewById(R.id.tvDay);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvType = itemView.findViewById(R.id.tvType);
            cardShop = itemView.findViewById(R.id.cardShop);
        }
    }
}

