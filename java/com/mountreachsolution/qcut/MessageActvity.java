package com.mountreachsolution.qcut;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Context;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MessageActvity extends AppCompatActivity {
    MaterialToolbar toolbar;
    CardView cardAppointment, cardCash, cardOnline;
    Button btnCancel, btnNotCancel, btnGoBackCash, btnGoBackOnline;
    TextView shopname, service, Time, Date, paymentmode, paymentshedull, Total;

    String id, shopName, userName, time, date, total, methode, shedul, serviceText;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_actvity);
        toolbar = findViewById(R.id.toolbar);
        cardAppointment = findViewById(R.id.cardAppointment);
        cardCash = findViewById(R.id.cardCash);
        cardOnline = findViewById(R.id.cardOnline);
        btnCancel = findViewById(R.id.btnCancel);
        btnNotCancel = findViewById(R.id.btnNotCancel);
        btnGoBackCash = findViewById(R.id.btnGoBackCash);
        btnGoBackOnline = findViewById(R.id.btnGoBackOnline);

        shopname = findViewById(R.id.shopname);
        service = findViewById(R.id.service);
        Time = findViewById(R.id.Time);
        Date = findViewById(R.id.Date);
        paymentmode = findViewById(R.id.paymentmode);
        paymentshedull = findViewById(R.id.paymentshedull);
        Total = findViewById(R.id.Total);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        shopName = intent.getStringExtra("shopname");
        userName = intent.getStringExtra("username");
        time = intent.getStringExtra("time");
        date = intent.getStringExtra("date");
        total = intent.getStringExtra("total");
        methode = intent.getStringExtra("methode");
        shedul = intent.getStringExtra("shedul");
        serviceText = intent.getStringExtra("service");

        // Set Toolbar title as shop name
        toolbar.setTitle(shopName);
        shopname.setText(shopName);
        service.setText(serviceText);
        Time.setText("Time: " + time);
        Date.setText("Date: " + date);
        paymentmode.setText("Payment: " + methode);
        paymentshedull.setText("Status: " + shedul);
        Total.setText("Total: " + total);
        progressDialog=new ProgressDialog(MessageActvity.this);
        progressDialog.setMessage("Please Wait......");
        progressDialog.setCanceledOnTouchOutside(false);


        // CANCEL Button Logic
        btnCancel.setOnClickListener(v -> {
            cardAppointment.setVisibility(View.GONE);
            if (methode.equalsIgnoreCase("Online")) {
                cardOnline.setVisibility(View.VISIBLE);
            } else {
                cardCash.setVisibility(View.VISIBLE);
            }
        });
        // NOT CANCEL Button Logic
        btnNotCancel.setOnClickListener(v -> {
            cardAppointment.setVisibility(View.VISIBLE);
            cardCash.setVisibility(View.GONE);
            cardOnline.setVisibility(View.GONE);
        });

        // Go Back Buttons Logic
        btnGoBackCash.setOnClickListener(v -> {
            progressDialog.show();
            deleteAppointment(id); // Delete appointment from backend
        });

        btnGoBackOnline.setOnClickListener(v -> {
            progressDialog.show();
            deleteAppointment(id);// Delete appointment from backend
        });
    }

    private void deleteAppointment(String id) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id", id); // send ID to API

        client.post(url.delAppoinment, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    // response is already a JSONObject
                    String status = response.getString("status");
                    // Optional: message field if your API returns it
                    String message = response.has("message") ? response.getString("message") : "Appointment deleted";

                    Toast.makeText(MessageActvity.this, message, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MessageActvity.this, HomeActvity.class);
                    startActivity(i);
                    finish();
                    progressDialog.dismiss();


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MessageActvity.this, "Response parsing error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(MessageActvity.this, "Failed to delete appointment", Toast.LENGTH_SHORT).show();

            }
        });
    }


}
