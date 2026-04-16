package com.mountreachsolution.qcut;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mountreachsolution.qcut.common.NetworkChangeListner;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONArray;
import org.json.JSONObject;

public class SplashActvity extends AppCompatActivity {

    ImageView logo;
    NetworkChangeListner networkChangeListner = new NetworkChangeListner();
    SharedPreferences sharedPreferences;
    Boolean loginvale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.iv_splash_logo);
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        loginvale = sharedPreferences.getBoolean("isFirstTime",false);

        // Load image from server
        getImage();

        // Load smooth fade-in animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(fadeIn);

        // Move to Intro screen after delay
        new Handler().postDelayed(() -> {
            if (loginvale) {
                // User has logged in before → check role
                String userrole = sharedPreferences.getString("userrole", "");

                Intent i;
                if (userrole.equalsIgnoreCase("Customer")) {
                    i = new Intent(SplashActvity.this, HomeActvity.class);
                } else if (userrole.equalsIgnoreCase("Salon Owner")) {
                    i = new Intent(SplashActvity.this, ShopHomeActivity.class);
                } else {
                    // If role is empty or unknown → IntroActivity
                    i = new Intent(SplashActvity.this, IntroActivty.class);
                }

                startActivity(i);
            } else {
                // First time → move to IntroActivity
                startActivity(new Intent(SplashActvity.this, IntroActivty.class));
            }

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 4000); // 4 seconds delay

    }

    private void getImage() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url.GetSplashActvity,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataArray = response.getJSONArray("getImage");

                            if (dataArray.length() > 0) {
                                JSONObject obj = dataArray.getJSONObject(0);
                                String imageFile = obj.getString("image"); // splash1.jpg

                                // Load image with Glide
                                Glide.with(SplashActvity.this)
                                        .load(url.address + "image/"+imageFile)
                                        .into(logo);
                            }
                        } catch (Exception e) {
                            Toast.makeText(SplashActvity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SplashActvity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        request.setShouldCache(false); // Always fetch fresh
        queue.add(request);
    }
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListner,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListner);
    }
}
