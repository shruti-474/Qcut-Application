package com.mountreachsolution.qcut;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class IntroActivty extends AppCompatActivity {
     ImageView backgroundImage;
     TextView heading, subheading;
     AppCompatButton btnLogin, btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_activty);
        backgroundImage = findViewById(R.id.backgroundImage);
        heading = findViewById(R.id.heading);
        subheading = findViewById(R.id.subheading);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);

        // Button Click Actions
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivty.this, LoginActivity.class);
            startActivity(intent);
        });

        btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(IntroActivty.this, SignupActivity.class);
            startActivity(intent);
        });

    }
}