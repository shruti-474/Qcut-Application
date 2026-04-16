package com.mountreachsolution.qcut.ADAPTER;

import android.app.Activity;
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
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import com.mountreachsolution.qcut.POJO.POJOFav;
import com.mountreachsolution.qcut.POJO.Shop;
import com.mountreachsolution.qcut.R;
import com.mountreachsolution.qcut.Shopdetails;
import com.mountreachsolution.qcut.common.url;

import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {
    List<POJOFav>pojoFavs;
    Activity activity;

    public FavAdapter(List<POJOFav> pojoFavs, Activity activity) {
        this.pojoFavs = pojoFavs;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FavAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_shop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.ViewHolder holder, int position) {
        POJOFav shop = pojoFavs.get(position);
        holder.tvName.setText(shop.getName());
        holder.tvAddress.setText(shop.getAddress());
        holder.tvDay.setText("Open for: "+ shop.getDay());
        holder.tvTime.setText("Time: "+ shop.getTime());
        holder.tvType.setText("Type: " + shop.getType());
        Glide.with(activity)
                .load(url.imageaddress+shop.getImage()) // change URL accordingly
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.ivShop);
        holder.cardShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, Shopdetails.class);
                i.putExtra("id",shop.getShopid());
                activity.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pojoFavs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivShop;
        TextView tvName, tvAddress, tvDay, tvTime, tvType;
        CardView cardShop;
        public ViewHolder(@NonNull View itemView) {
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
