package com.mountreachsolution.qcut.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mountreachsolution.qcut.POJO.Shop;
import com.mountreachsolution.qcut.R;
import com.mountreachsolution.qcut.Shopdetails;
import com.mountreachsolution.qcut.common.url;

import java.util.ArrayList;
import java.util.List;

public class CateAdapter extends RecyclerView.Adapter<CateAdapter.ViewHolder> implements Filterable {
    private List<Shop> shopList;
    private List<Shop> shopListFull; // copy of original list
    private Context context;

    public CateAdapter(List<Shop> shopList, Context context) {
        this.shopList = new ArrayList<>(shopList);
        this.shopListFull = new ArrayList<>(shopList); // keep original list
        this.context = context;
    }

    @NonNull
    @Override
    public CateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CateAdapter.ViewHolder holder, int position) {
        Shop shop = shopList.get(position);

        holder.tvName.setText(shop.getName());
        holder.tvAddress.setText(shop.getAddress());
        holder.tvDay.setText("Open for: " + shop.getDay());
        holder.tvTime.setText("Time: " + shop.getTime());
        holder.tvType.setText("Type: " + shop.getType());

        // ✅ Show services too (optional, if you have TextView for services in item_shop.xml)
        // holder.tvServices.setText("Services: " + shop.getServices());

        Glide.with(context)
                .load(url.imageaddress + shop.getImage())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.ivShop);

        holder.cardShop.setOnClickListener(v -> {
            Intent i = new Intent(context, Shopdetails.class);
            i.putExtra("id", shop.getId());
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    @Override
    public Filter getFilter() {
        return shopFilter;
    }

    private Filter shopFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Shop> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(shopListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Shop item : shopListFull) {
                    // ✅ Match name, address, type, OR services
                    if (item.getName().toLowerCase().contains(filterPattern) ||
                            item.getAddress().toLowerCase().contains(filterPattern) ||
                            item.getType().toLowerCase().contains(filterPattern) ||
                            (item.getService() != null && item.getService().toLowerCase().contains(filterPattern))) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            shopList.clear();
            shopList.addAll((List<Shop>) results.values);
            notifyDataSetChanged();
        }
    };

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
            // If you add services in XML, also bind here: tvServices = itemView.findViewById(R.id.tvServices);
        }
    }
}
