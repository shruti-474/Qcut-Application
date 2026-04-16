package com.mountreachsolution.qcut.ADAPTER;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolution.qcut.R;
import com.mountreachsolution.qcut.POJO.Order;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_card, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.shopName.setText(order.getShopName());
        holder.shopAddress.setText(order.getShopAddress());
        holder.userName.setText(order.getFinalName());
        holder.userEmail.setText(order.getFinalEmail());
        holder.finalTime.setText("Time: " + order.getFinalTime());
        holder.finalDate.setText("Date: " + order.getFinalDate());
        holder.totalAmount.setText("Amount: ₹" + order.getTotalAmount());
        holder.paymentMode.setText("Payment: " + order.getPaymentMode());
        holder.paymentStatus.setText("Status: " + order.getPaymentStatus());
        holder.tvsetno.setText("Set No: " + order.getSetno());
        holder.tvbookstatus.setText("Booking: " + order.getStatu());

        // Show Pay button only if booking is confirmed and not already paid online
        if (order.getStatu().equals("Confirm") && !order.getPaymentMode().equals("Online")) {
            holder.btnpay.setVisibility(View.VISIBLE);
            holder.btnpay.setOnClickListener(v -> showPaymentDialog(holder, order, position));
        } else {
            holder.btnpay.setVisibility(View.GONE);
        }
    }

    private void showPaymentDialog(OrderViewHolder holder, Order order, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
        builder.setTitle("Scan to Pay");

        View dialogView = LayoutInflater.from(holder.itemView.getContext())
                .inflate(R.layout.dialog_qr_payment, null);

        ImageView qrImage = dialogView.findViewById(R.id.qrImage);
        Button btnDone = dialogView.findViewById(R.id.btnDone);

        qrImage.setImageResource(R.drawable.qr);

        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        btnDone.setOnClickListener(view -> {
            alertDialog.dismiss();
            pay(order.getId(), "Payment Done", "Online", position);
            Toast.makeText(holder.itemView.getContext(), "Payment Completed!", Toast.LENGTH_SHORT).show();
        });

        alertDialog.show();
    }

    private void pay(String id, String paymentStatus, String paymentMode, int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("paymentStatus", paymentStatus);
        params.put("paymentMode", paymentMode);

        client.post(url.updatepay, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));
                    if (json.getInt("success") == 1) {
                        // Update local data and refresh only this item
                        Order order = orderList.get(position);
                        order.setPaymentStatus(paymentStatus);
                        order.setPaymentMode(paymentMode);
                        notifyItemChanged(position);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(null, "Payment failed. Please try again!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView shopName, shopAddress, userName, userEmail,
                finalTime, finalDate, totalAmount, paymentMode, paymentStatus, tvsetno, tvbookstatus;
        AppCompatButton btnpay;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            shopName = itemView.findViewById(R.id.shopName);
            shopAddress = itemView.findViewById(R.id.shopAddress);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            finalTime = itemView.findViewById(R.id.finalTime);
            finalDate = itemView.findViewById(R.id.finalDate);
            totalAmount = itemView.findViewById(R.id.totalAmount);
            paymentMode = itemView.findViewById(R.id.paymentMode);
            paymentStatus = itemView.findViewById(R.id.paymentStatus);
            tvsetno = itemView.findViewById(R.id.setNumber);
            tvbookstatus = itemView.findViewById(R.id.bookingStatus);
            btnpay = itemView.findViewById(R.id.btnpay);
        }
    }
}
