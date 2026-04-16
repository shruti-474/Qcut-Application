package com.mountreachsolution.qcut.ADAPTER;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mountreachsolution.qcut.MessageActvity;
import com.mountreachsolution.qcut.POJO.Order;
import com.mountreachsolution.qcut.R;

import java.util.List;

public class AdapterChat extends RecyclerView.Adapter<AdapterChat.ViewHolder> {
    private List<Order> orderList;
    Activity  activity;

    public AdapterChat(List<Order> orderList, Activity activity) {
        this.orderList = orderList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterChat.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterChat.ViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvName.setText(order.getShopName());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, MessageActvity.class);
                i.putExtra("id",order.getId());
                i.putExtra("shopname",order.getShopName());
                i.putExtra("username",order.getFinalName());
                i.putExtra("time",order.getFinalTime());
                i.putExtra("date",order.getFinalDate());
                i.putExtra("total",order.getTotalAmount());
                i.putExtra("methode",order.getPaymentMode());
                i.putExtra("shedul",order.getPaymentStatus());
                i.putExtra("service",order.getFinalService());
                activity.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTapToChat;
        CardView card;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvTapToChat = itemView.findViewById(R.id.tvTapToChat);
            card = itemView.findViewById(R.id.card);
        }
    }
}
