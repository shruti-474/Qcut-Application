package com.mountreachsolution.qcut;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowInsetsController;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mountreachsolution.qcut.Fragment.ChatFragment;
import com.mountreachsolution.qcut.Fragment.HomeFragment;
import com.mountreachsolution.qcut.Fragment.Mapsfragment;
import com.mountreachsolution.qcut.Fragment.ProfilFragment;

public class HomeActvity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_actvity);
        Window window = getWindow();
        getWindow().setNavigationBarColor(ContextCompat.getColor(HomeActvity.this,R.color.white));
        bottomNavigationView = findViewById(R.id.bottomnevigatiomuserhome);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.btHome);

    }

    HomeFragment homeFragment = new HomeFragment();
    Mapsfragment mapsfragment = new Mapsfragment();
    ChatFragment chatFragment = new ChatFragment();
    ProfilFragment profilFragment = new ProfilFragment();
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btHome){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,homeFragment).commit();
        }else if(item.getItemId()==R.id.btLocation){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,mapsfragment).commit();
        } else if(item.getItemId()==R.id.btMessage){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,chatFragment).commit();
        }else if(item.getItemId()==R.id.btProfil){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,profilFragment).commit();
        }
        return true;
    }
}