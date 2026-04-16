package com.mountreachsolution.qcut;

import static com.mountreachsolution.qcut.common.url.addToFav;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class Shopdetails extends AppCompatActivity {
    ImageView shopImage;
    TextView shopName, shopAddress, shopTiming, shopType, servicesTitle,shopDay,shopSets,shopStatus;
    ChipGroup servicesChipGroup;
    MaterialButton bookAppointmentButton,addFav;
    String id;
    ProgressDialog progressDialog;
     String name, address, time, day, service, image, type,shopemail,rateing,status,sets;
     String userEmail;
    RatingBar ratingBar;
    float backendRating= 4.3F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopdetails);
        id = getIntent().getStringExtra("id");
        shopImage = findViewById(R.id.shopImage);
        shopName = findViewById(R.id.shopName);
        shopAddress = findViewById(R.id.shopAddress);
        shopTiming = findViewById(R.id.shopTiming);
        shopType = findViewById(R.id.shopType);
        shopDay = findViewById(R.id.shopday);
        servicesTitle = findViewById(R.id.servicesTitle);
        servicesChipGroup = findViewById(R.id.servicesChipGroup);
        bookAppointmentButton = findViewById(R.id.bookAppointmentButton);
        ratingBar = findViewById(R.id.shopRating);
        shopSets = findViewById(R.id.shopSets);
        shopStatus=findViewById(R.id.shopStatus);

        addFav=findViewById(R.id.btnFav);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
         userEmail = prefs.getString("email", ""); // second parameter is the default value if not found


        // Set Status Bar color to pink
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.pink));
        }

        // Optional: light status bar icons for contrast (dark icons)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(0); // light icons
        }
        progressDialog = new ProgressDialog(Shopdetails.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        fetchShopDetailsAsync(id);
        bookAppointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Shopdetails.this,BookAppoinment.class);
                i.putExtra("shopname",name);
                i.putExtra("address",address);
                i.putExtra("time",time);
                i.putExtra("day",day);
                i.putExtra("service",service);
                i.putExtra("image",image);
                i.putExtra("type",type);
                i.putExtra("email",shopemail);
                i.putExtra("status",shopemail);
                i.putExtra("sets",sets);
                startActivity(i);
            }
        });

        addFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToFav(Shopdetails.this,name,address,time,day,service,image,type,"null","null",shopemail,id,userEmail);

            }
        });



    }

    public void addToFav(Context context,
                         String namea,
                         String address,
                         String time,
                         String day,
                         String service,
                         String image,
                         String type,
                         String lattitude,
                         String logitude,
                         String shopemail,
                         String shopid,
                         String userEmail) { // pass your PHP URL here

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, addToFav,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            String message = jsonObject.optString("message", "Unknown response");

                            if (success == 1) {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Parsing Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("namea", namea);
                params.put("address", address);
                params.put("time", time);
                params.put("day", day);
                params.put("service", service);
                params.put("image", image);
                params.put("type", type);
                params.put("lattitude", lattitude);
                params.put("logitude", logitude);
                params.put("shopemail", shopemail);
                params.put("shopid", shopid);
                params.put("userEmail", userEmail);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }








    public void fetchShopDetailsAsync(String shopId) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id", shopId); // send POST parameter

        client.post(url.getShpdataById, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray array = response.getJSONArray("getShop");
                    if (array.length() > 0) {
                        JSONObject obj = array.getJSONObject(0);

                        name = obj.getString("name");
                        address = obj.getString("address");
                        time = obj.getString("time");
                        day = obj.getString("day");
                        service = obj.getString("service");
                        image = obj.getString("image");
                        type = obj.getString("type");
                        shopemail = obj.getString("shopemail");
                        rateing=obj.getString("rateing");
                        status=obj.getString("status");
                        sets=obj.getString("sets");
                        String[] servicesArray = service.split(",");

// Remove numbers and dash
                        servicesChipGroup.removeAllViews(); // clear previous chips if any
                        for (String s : servicesArray) {
                            String serviceName = s.split("-")[0]; // take only part before '-'

                            Chip chip = new Chip(Shopdetails.this);
                            chip.setText(serviceName.trim());
                            chip.setClickable(false);
                            chip.setCheckable(false);

                            servicesChipGroup.addView(chip);
                        }

                        shopName.setText(name);
                        shopAddress.setText(address);
                        shopType.setText("Type: " + type);
                        shopTiming.setText("Time: " + time);
                        shopDay.setText("Open for: " + day);
                        shopStatus.setText("Status: " + status);
                        shopSets.setText("Available Seats: " + sets);
                        float ratingValue = 0f;
                        try {
                            ratingValue = Float.parseFloat(rateing);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            ratingValue = 0f; // fallback if parsing fails
                        }
                        ratingBar.setRating(ratingValue); // e.g. 4.3f

                        Glide.with(Shopdetails.this)
                                .load(url.address + "image/" + image)
                                .error(R.drawable.baseline_no_photography_24)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(shopImage);
                        progressDialog.dismiss();



                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Shopdetails.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(Shopdetails.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}