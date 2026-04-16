package com.mountreachsolution.qcut;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BillActivity extends AppCompatActivity {
    CircleImageView shopImage;
    TextView shopName, shopAddress;
    TextView finalName, finalEmail;
    LinearLayout servicesContainer, billContainer;
    TextView finalDate, finalTime;
    TextView platformFee, totalAmount;
    RadioGroup paymentMethod;
    RadioButton rbSpotCash, rbOnline;

    TextView tvsetNo;
    String onlySetNumber;
    androidx.appcompat.widget.AppCompatButton btnBook;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        // Initialize Views
        shopImage = findViewById(R.id.shopImage);
        shopName = findViewById(R.id.shopName);
        shopAddress = findViewById(R.id.shopAddress);
        tvsetNo = findViewById(R.id.setNumber);

        finalName = findViewById(R.id.finalName);
        finalEmail = findViewById(R.id.finalEmail);

        servicesContainer = findViewById(R.id.servicesContainer);
        billContainer = findViewById(R.id.billContainer);

        finalDate = findViewById(R.id.finalDate);
        finalTime = findViewById(R.id.finalTime);

        platformFee = findViewById(R.id.platformFee);
        totalAmount = findViewById(R.id.totalAmount);

//        paymentMethod = findViewById(R.id.paymentMethod);
//        rbSpotCash = findViewById(R.id.rbSpotCash);
//        rbOnline = findViewById(R.id.rbOnline);

        btnBook = findViewById(R.id.btnBook);

        Intent intent = getIntent();
        String fName = intent.getStringExtra("finalName");
        String fEmail = intent.getStringExtra("finalEmail");

        String sName = intent.getStringExtra("shopName");
        String sAddress = intent.getStringExtra("shopAddress");
        String sImage = intent.getStringExtra("shopImage");

        String set = intent.getStringExtra("chosenSet"); // e.g. "Set 1"
         onlySetNumber = set.replaceAll("\\D+", "");  // ✅ keeps only digits

        String fDate = intent.getStringExtra("finalDate");
        String fTime = intent.getStringExtra("finalTime");
        String fServices = intent.getStringExtra("finalServices"); // comma-separated

        // Set values
        finalName.setText(fName);
        finalEmail.setText(fEmail);
        shopName.setText(sName);
        shopAddress.setText(sAddress);
        tvsetNo.setText("Set No: " + onlySetNumber);
        finalDate.setText("Date: " + fDate);
        finalTime.setText("Time: " + fTime);

        Glide.with(this).load(url.imageaddress + sImage).into(shopImage);

        // Populate Services
        if (fServices != null && !fServices.isEmpty()) {
            String[] servicesArray = fServices.split(",");
            int totalPrice = 0;
            for (String service : servicesArray) {
                String[] parts = service.split("-");
                String serviceName = parts[0];
                int price = 0;
                if (parts.length > 1) price = Integer.parseInt(parts[1]);

                // Add to servicesContainer
                TextView serviceTv = new TextView(this);
                serviceTv.setText(serviceName);
                serviceTv.setTextSize(14f);
                servicesContainer.addView(serviceTv);

                // Add to billContainer
                LinearLayout row = new LinearLayout(this);
                row.setOrientation(LinearLayout.HORIZONTAL);
                TextView tvName = new TextView(this);
                tvName.setText(serviceName);
                tvName.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
                TextView tvPrice = new TextView(this);
                tvPrice.setText(String.valueOf(price));
                row.addView(tvName);
                row.addView(tvPrice);
                billContainer.addView(row);

                totalPrice += price;
            }

            // Add platform fee
            int fee = 20;
            totalPrice += fee;
            platformFee.setText(String.valueOf(fee));
            totalAmount.setText(String.valueOf(totalPrice));
        }

        // Directly book without showing payment dialog
        progressDialog = new ProgressDialog(BillActivity.this);
        progressDialog.setMessage("Making Booking");
        progressDialog.setCanceledOnTouchOutside(false);
        btnBook.setOnClickListener(v -> lodinserver());
    }

    private void lodinserver() {
        progressDialog.show();
        // Get values from UI
        String shopNameStr = shopName.getText().toString().trim();
        String shopAddressStr = shopAddress.getText().toString().trim();
        String shopEmailStr = getIntent().getStringExtra("shopEmail");

        String finalNameStr = finalName.getText().toString().trim();
        String finalEmailStr = finalEmail.getText().toString().trim();
        String setno = tvsetNo.getText().toString().trim();
        String finalDateStr = finalDate.getText().toString().replace("Date: ", "").trim();
        String finalTimeStr = finalTime.getText().toString().replace("Time: ", "").trim();

        StringBuilder servicesBuilder = new StringBuilder();
        for (int i = 0; i < servicesContainer.getChildCount(); i++) {
            TextView tv = (TextView) servicesContainer.getChildAt(i);
            servicesBuilder.append(tv.getText().toString().trim());
            if (i != servicesContainer.getChildCount() - 1) servicesBuilder.append(",");
        }
        String servicesStr = servicesBuilder.toString();

        String totalAmountStr = totalAmount.getText().toString().trim();

        // Always mark Cash Pending (since no payment gateway)
        String paymentModeStr = "Cash";
        String paymentStatusStr = "Pending";
        String status="Not Confirm";

        // Create Volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url.lodbill,
                response -> {
                    try {
                        response = response.trim();
                        JSONObject obj = new JSONObject(response);
                        int success = obj.optInt("success", 0);

                        if (success == 1) {
                            progressDialog.dismiss();
                            lodinserver2();
                            Toast.makeText(BillActivity.this, "Booking saved successfully", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(BillActivity.this, HomeActvity.class);
                            startActivity(i);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(BillActivity.this, "Booking failed", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(BillActivity.this, "Invalid server response", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(BillActivity.this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("shopName", shopNameStr);
                params.put("shopAddress", shopAddressStr);
                params.put("shopEmail", shopEmailStr);
                params.put("finalUserName", finalNameStr);
                params.put("finalEmail", finalEmailStr);
                params.put("finalDate", finalDateStr);
                params.put("finalTime", finalTimeStr);
                params.put("services", servicesStr);
                params.put("totalAmount", totalAmountStr);
                params.put("paymentMode", paymentModeStr);
                params.put("paymentStatus", paymentStatusStr);
                params.put("sets",onlySetNumber);
                params.put("statu", status);
                return params;
            }
        };

        queue.add(request);
    }
    private void lodinserver2() {
        progressDialog.show();
        // Get values from UI
        String shopNameStr = shopName.getText().toString().trim();
        String shopAddressStr = shopAddress.getText().toString().trim();
        String shopEmailStr = getIntent().getStringExtra("shopEmail");

        String finalNameStr = finalName.getText().toString().trim();
        String finalEmailStr = finalEmail.getText().toString().trim();
        String setno = tvsetNo.getText().toString().trim();
        String finalDateStr = finalDate.getText().toString().replace("Date: ", "").trim();
        String finalTimeStr = finalTime.getText().toString().replace("Time: ", "").trim();

        StringBuilder servicesBuilder = new StringBuilder();
        for (int i = 0; i < servicesContainer.getChildCount(); i++) {
            TextView tv = (TextView) servicesContainer.getChildAt(i);
            servicesBuilder.append(tv.getText().toString().trim());
            if (i != servicesContainer.getChildCount() - 1) servicesBuilder.append(",");
        }
        String servicesStr = servicesBuilder.toString();

        String totalAmountStr = totalAmount.getText().toString().trim();

        // Always mark Cash Pending (since no payment gateway)
        String paymentModeStr = "Cash";
        String paymentStatusStr = "Pending";
        String status="Not Confirm";

        // Create Volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url.loadBokking,
                response -> {
                    try {
                        response = response.trim();
                        JSONObject obj = new JSONObject(response);
                        int success = obj.optInt("success", 0);

                        if (success == 1) {
                            progressDialog.dismiss();
                            Toast.makeText(BillActivity.this, "Booking saved successfully", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(BillActivity.this, HomeActvity.class);
                            startActivity(i);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(BillActivity.this, "Booking failed", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        Toast.makeText(BillActivity.this, "Invalid server response", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(BillActivity.this, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("shopName", shopNameStr);
                params.put("shopAddress", shopAddressStr);
                params.put("shopEmail", shopEmailStr);
                params.put("finalUserName", finalNameStr);
                params.put("finalEmail", finalEmailStr);
                params.put("finalDate", finalDateStr);
                params.put("finalTime", finalTimeStr);
                params.put("services", servicesStr);
                params.put("totalAmount", totalAmountStr);
                params.put("paymentMode", paymentModeStr);
                params.put("paymentStatus", paymentStatusStr);
                params.put("sets",onlySetNumber);
                params.put("statu", status);
                return params;
            }
        };

        queue.add(request);
    }
}
