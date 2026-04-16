package com.mountreachsolution.qcut.ADAPTER;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mountreachsolution.qcut.POJO.Order;
import com.mountreachsolution.qcut.POJO.POJOHis;
import com.mountreachsolution.qcut.R;

import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.ViewHolder> {
    List<POJOHis>pojoHis;
    Activity activity;

    public AdapterHistory(List<POJOHis> pojoHis, Activity activity) {
        this.pojoHis = pojoHis;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterHistory.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHistory.ViewHolder holder, int position) {
        POJOHis order = pojoHis.get(position);

        holder.shopName.setText(order.getShopName());
        holder.shopAddress.setText(order.getShopAddress());
        holder.userName.setText(order.getFinalName());
        holder.userEmail.setText(order.getFinalEmail());

        holder.finalTime.setText("Time: " + order.getFinalTime());
        holder.finalDate.setText("Date: " + order.getFinalDate());

        holder.totalAmount.setText("Amount: ₹" + order.getTotalAmount());
        holder.AStatus.setText("Appiontment Status: " + order.getAppoinmentstatus());

    }

    @Override
    public int getItemCount() {
        return pojoHis.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName, shopAddress, userName, userEmail,
                finalTime, finalDate, totalAmount,AStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.shopName);
            shopAddress = itemView.findViewById(R.id.shopAddress);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            finalTime = itemView.findViewById(R.id.finalTime);
            finalDate = itemView.findViewById(R.id.finalDate);
            totalAmount = itemView.findViewById(R.id.totalAmount);

            AStatus = itemView.findViewById(R.id.status);
        }
    }
}
