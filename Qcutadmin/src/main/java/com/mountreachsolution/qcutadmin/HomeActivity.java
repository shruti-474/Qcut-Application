package com.mountreachsolution.qcutadmin;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mountreachsolution.qcutadmin.Fragment.AppoinmentFFragment;
import com.mountreachsolution.qcutadmin.Fragment.SalonFragment;
import com.mountreachsolution.qcutadmin.Fragment.UserFragment;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        Window window = getWindow();
        getWindow().setNavigationBarColor(ContextCompat.getColor(HomeActivity.this,R.color.white));
        bottomNavigationView = findViewById(R.id.bottomnevigatiomuserhome);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.btAllUser);

    }

    UserFragment userFragment = new UserFragment();
    AppoinmentFFragment  appoinmentFFragment = new AppoinmentFFragment();
   SalonFragment salonFragment = new SalonFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btAllUser){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,userFragment).commit();
        }else if(item.getItemId()==R.id.btHistroy){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,appoinmentFFragment).commit();
        } else if(item.getItemId()==R.id.btStore){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,salonFragment).commit();
        }
        return true;
    }
}