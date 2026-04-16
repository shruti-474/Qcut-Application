package com.mountreachsolution.qcutadmin;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.mountreachsolution.qcutadmin.comman.NetworkChangeListner;

public class MainActivity extends AppCompatActivity {
    ImageView logo;
    NetworkChangeListner networkChangeListner = new NetworkChangeListner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.iv_splash_logo);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(fadeIn);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences("AdminLogin", MODE_PRIVATE);
                boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

                if (isLoggedIn) {
                    // ✅ Already logged in → go to HomeActivity
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                } else {
                    // Not logged in → go to LoginActivity
                    startActivity(new Intent(MainActivity.this, LogInActvity.class));
                }
                finish();
            }
        }, 3000);
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
