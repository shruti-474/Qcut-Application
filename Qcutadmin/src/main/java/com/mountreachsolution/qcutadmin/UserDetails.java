package com.mountreachsolution.qcutadmin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolution.qcutadmin.comman.url;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetails extends AppCompatActivity {
    CircleImageView userImage, shopImage;
    TextView userName, userAddress, userEmail, userMobile, userRole;
    TextView shopName, shopAddress, shopEmail, shopLatLong, shopService, shopDay, shopTime, shopType;
    Button btnRemoveAccess;
    CardView shopCard;
    ProgressDialog progressDialog;

    String userId, role,email;

    String shopid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // User Views
        userImage = findViewById(R.id.userImage);
        userName = findViewById(R.id.userName);
        userAddress = findViewById(R.id.userAddress);
        userEmail = findViewById(R.id.userEmail);
        userMobile = findViewById(R.id.userMobile);
        userRole = findViewById(R.id.userRole);
        btnRemoveAccess = findViewById(R.id.btnRemoveAccess);

        // Shop Views
        shopCard = findViewById(R.id.shopCard);
        shopImage = findViewById(R.id.shopImage);
        shopName = findViewById(R.id.shopName);
        shopAddress = findViewById(R.id.shopAddress);
        shopEmail = findViewById(R.id.shopEmail);
        shopLatLong = findViewById(R.id.shopLatLong);
        shopService = findViewById(R.id.shopService);
        shopDay = findViewById(R.id.shopDay);
        shopTime = findViewById(R.id.shopTime);
        shopType = findViewById(R.id.shopType);

        // Get Data from Intent
        Intent i = getIntent();
        userId = i.getStringExtra("id");
        String name = i.getStringExtra("name");
         email = i.getStringExtra("email");
        String mobile = i.getStringExtra("mobile");
        String roleData = i.getStringExtra("userrole");
        String address = i.getStringExtra("address");
        String image = i.getStringExtra("image");

        // Set data
        userName.setText(name);
        userEmail.setText(email);
        userMobile.setText(mobile);
        userRole.setText(roleData);
        userAddress.setText(address);

        Glide.with(this)
                .load(url.imageaddress + image)
                .placeholder(R.drawable.baseline_person_24)
                .into(userImage);

        role = roleData;

        if ("Salon Owner".equalsIgnoreCase(role)) {
            loadShop(email);
        } else {
            shopCard.setVisibility(CardView.GONE); // hide if not Salon Owner
        }

        btnRemoveAccess.setOnClickListener(v -> {
            deleteUser(userId);
        });
    }

    private void deleteUser(String userId) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Deleting user...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id", userId);

        client.post(url.delUser, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody);
                    JSONObject json = new JSONObject(response);

                    String status = json.getString("status");
                    String message = json.getString("message");

                    Toast.makeText(UserDetails.this, message, Toast.LENGTH_SHORT).show();

                    if (status.equalsIgnoreCase("success")) {
                        // Optional: finish activity or refresh list
//                        Intent i = new Intent(UserDetails.this,HomeActivity.class);
//                        startActivity(i);
//                        finish();
                        DeletShop(email);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(UserDetails.this, "Parse error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                String errMsg = (responseBody != null) ? new String(responseBody) : error.getMessage();
                Toast.makeText(UserDetails.this, "Error: " + errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DeletShop(String email) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", email);

        client.post(url.delShop, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody);
                    JSONObject json = new JSONObject(response);

                    String status = json.getString("status");
                    String message = json.getString("message");

                    Toast.makeText(UserDetails.this, message, Toast.LENGTH_SHORT).show();

                    if (status.equalsIgnoreCase("success")) {
                        // Optional: finish activity or refresh list
                       Intent i = new Intent(UserDetails.this,HomeActivity.class);
                       startActivity(i);
                       finish();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(UserDetails.this, "Parse error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                String errMsg = (responseBody != null) ? new String(responseBody) : error.getMessage();
                Toast.makeText(UserDetails.this, "Error: " + errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadShop(String email) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Shop...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("userEmail", email);

        client.post(url.getShopUser, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressDialog.dismiss();
                try {
                    String response = new String(responseBody);  // raw string response
                    Log.d("ShopResponse", response);

                    JSONObject json = new JSONObject(response);
                    JSONArray array = json.getJSONArray("getShop");

                    if (array.length() > 0) {
                        JSONObject obj = array.getJSONObject(0);

                        shopName.setText("Name: " + obj.getString("name"));
                        shopAddress.setText("Address: " + obj.getString("address"));
                        shopEmail.setText("Email: " + email);
                        shopLatLong.setText("Lat, Long: " + obj.getString("lattitude") + ", " + obj.getString("lobgitude"));
                        shopService.setText("Service: " + obj.getString("service"));
                        shopDay.setText("Day: " + obj.getString("day"));
                        shopTime.setText("Time: " + obj.getString("time"));
                        shopType.setText("Type: " + obj.getString("type"));
                        shopid = obj.getString("id");



                        Glide.with(UserDetails.this)
                                .load(url.imageaddress + obj.getString("image"))
                                .placeholder(R.drawable.baseline_store_24)
                                .into(shopImage);

                        shopCard.setVisibility(View.VISIBLE);
                    } else {
                        shopCard.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    Log.e("ShopParseError", e.toString());
                    Toast.makeText(UserDetails.this, "Parse error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                String errMsg = (responseBody != null) ? new String(responseBody) : error.getMessage();
                Log.e("ShopError", errMsg);
                Toast.makeText(UserDetails.this, "Error: " + errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
