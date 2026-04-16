package com.mountreachsolution.qcutadmin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class LogInActvity extends AppCompatActivity {
    EditText emailInput, passwordInput;
    AppCompatButton loginButton;
    CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_actvity);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        rememberMe = findViewById(R.id.rememberMe);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (username.equals("Admin") && password.equals("Admin")) {
                    // ✅ Save login state if Remember Me is checked
                    if (rememberMe.isChecked()) {
                        SharedPreferences prefs = getSharedPreferences("AdminLogin", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();
                    }

                    // Move to HomeActivity
                    Intent intent = new Intent(LogInActvity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LogInActvity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
