package com.mountreachsolution.qcut.ADAPTER;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolution.qcut.POJO.Order;
import com.mountreachsolution.qcut.R;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AdapterBooking extends RecyclerView.Adapter<AdapterBooking.ViewHolder> {
    private List<Order> orderList;
    Activity activity;

    public AdapterBooking(List<Order> orderList, Activity activity) {
        this.orderList = orderList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AdapterBooking.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBooking.ViewHolder holder, int position) {
        Order booking = orderList.get(position);
        holder.shopName.setText(booking.getShopName());
        holder.shopAddress.setText(booking.getShopAddress());
        holder.userName.setText(booking.getFinalName());
        holder.userEmail.setText(booking.getFinalEmail());
        holder.finalTime.setText("Time: " + booking.getFinalTime());
        holder.finalDate.setText("Date: " + booking.getFinalDate());
        holder.totalAmount.setText("Amount: ₹" + booking.getTotalAmount());
        holder.paymentMode.setText("Payment: " + booking.getPaymentMode());
        holder.paymentStatus.setText("Status: " + booking.getPaymentStatus());

        // New ones
        holder.setNumber.setText("Set No: " + booking.getSetno());
        holder.bookingStatus.setText("Status: " + booking.getStatu());

        // Button actions
        holder.btnConfirm.setOnClickListener(v -> {
            String id=booking.getId();
            String email=booking.getFinalEmail();
            updateStatus(id,"Confirm",position);
            updateStatus2(email,"Confirm");

        });

        holder.btnReject.setOnClickListener(v -> {
            String id=booking.getId();
            String email=booking.getFinalEmail();
            updateStatus(id,"Not Confirm",position);
            updateStatus2(email,"Not Confirm");

        });



    }
    private void updateStatus(String bookingId, String newStatus,int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id", bookingId);
        params.put("statu", newStatus);



        client.post(url.updateBooking, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody);
                    Log.d("API_RESPONSE", response);

                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("success") == 1) {

                        Toast.makeText(activity, "Updated to " + newStatus, Toast.LENGTH_SHORT).show();
                        deleteAppointment(bookingId,position);
                    } else {
                        Toast.makeText(activity, "Update failed!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "Parse error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(activity, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateStatus2(String bookingId, String newStatus) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("finalemail", bookingId);
        params.put("statu", newStatus);



        client.post(url.updateAppoinment, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody);
                    Log.d("API_RESPONSE", response);

                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("success") == 1) {
                        Toast.makeText(activity, "Updated to Appoinment" + newStatus, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "Update Appoinment failed!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "Parse error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(activity, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteAppointment(String id, int position) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id", id);

        client.post(url.delBooking, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

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

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView shopName, shopAddress, userName, userEmail, finalTime, finalDate, totalAmount;
        TextView paymentMode, paymentStatus, setNumber, bookingStatus;
        AppCompatButton btnConfirm, btnReject;
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

            // Newly added
            setNumber = itemView.findViewById(R.id.setNumber);
            bookingStatus = itemView.findViewById(R.id.bookingStatus);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}
