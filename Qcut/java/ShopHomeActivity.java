package com.mountreachsolution.qcut;

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
import com.mountreachsolution.qcut.Fragment.BookingFragment;
import com.mountreachsolution.qcut.Fragment.ShopHistory;
import com.mountreachsolution.qcut.Fragment.ShopHome;
import com.mountreachsolution.qcut.Fragment.ShopProfil;

public class ShopHomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shop_home);
        Window window = getWindow();
        getWindow().setNavigationBarColor(ContextCompat.getColor(ShopHomeActivity.this,R.color.white));
        bottomNavigationView = findViewById(R.id.bottomnevigatiomuserhome);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationView.setSelectedItemId(R.id.btHome);

    }
    ShopHome shopHome = new ShopHome();
    ShopHistory shopHistory = new ShopHistory();
    ShopProfil shopProfil = new ShopProfil();
    BookingFragment bookingFragment = new BookingFragment();

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.btHome){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,shopHome).commit();
        }else if(item.getItemId()==R.id.btBooking){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,bookingFragment).commit();
        }else if(item.getItemId()==R.id.btHistroy){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,shopHistory).commit();
        } else if(item.getItemId()==R.id.btProfil){
            getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayoutuserhome,shopProfil).commit();
        }
        return true;
    }
}