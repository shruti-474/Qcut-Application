package com.mountreachsolution.qcut.ADAPTER;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolution.qcut.AppointmentsActivity;
import com.mountreachsolution.qcut.POJO.Order;
import com.mountreachsolution.qcut.R;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class AdaptershopAppoinment extends RecyclerView.Adapter<AdaptershopAppoinment.ViewHolder> {
    private List<Order> orderList;
    Activity activity;

    public AdaptershopAppoinment(List<Order> orderList, Activity activity) {
        this.orderList = orderList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdaptershopAppoinment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopappoinment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptershopAppoinment.ViewHolder holder, int position) {
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

        // ✅ Done button click
        holder.btnDone.setOnClickListener(v -> {
            ProgressDialog progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("Processing...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            insertHistory(order, progressDialog, position);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName, shopAddress, userName, userEmail,
                finalTime, finalDate, totalAmount, paymentMode, paymentStatus,tvsetno,tvbookstatus;
        AppCompatButton btnDone;

        public ViewHolder(@NonNull View itemView) {
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
            btnDone = itemView.findViewById(R.id.btnDone);
        }
    }

    // 🔹 Insert into POJOHis first
    private void insertHistory(Order order, ProgressDialog progressDialog, int position) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.addToHistory,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int success = jsonObject.getInt("success");
                        if (success == 1) {
                            // ✅ Now delete appointment
                            deleteAppointment(order.getId(), progressDialog, position);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(activity, "Insert Failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                        Toast.makeText(activity, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("shopName", order.getShopName());
                params.put("shopAddress", order.getShopAddress());
                params.put("shopEmail", order.getShopEmail());
                params.put("finalUserName", order.getFinalName());
                params.put("finalEmail", order.getFinalEmail());
                params.put("finalDate", order.getFinalDate());
                params.put("finalTime", order.getFinalTime());
                params.put("services", order.getFinalService());
                params.put("totalAmount", order.getTotalAmount());
                params.put("paymentMode", order.getPaymentMode());
                params.put("paymentStatus", order.getPaymentStatus());
                params.put("Astatus", "Done"); // mark as completed
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(activity);
        queue.add(stringRequest);
    }

    // 🔹 Delete appointment after inserting POJOHis
    private void deleteAppointment(String id, ProgressDialog progressDialog, int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id", id);

        client.post(url.delAppoinment, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();
                try {
                    String status = response.getString("status");
                    String message = response.has("message") ? response.getString("message") : "Appointment deleted";

                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                    // ✅ Remove from list & refresh RecyclerView
                    orderList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, orderList.size());

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "Response parsing error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(activity, "Failed to delete appointment", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
