package com.mountreachsolution.qcut;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mountreachsolution.qcut.common.NetworkChangeListner;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText emailInput, passwordInput;
    CheckBox rememberMe;
    AppCompatButton loginButton;
    TextView forgotPassword, createAccount;
    ProgressDialog progressDialog;
    NetworkChangeListner networkChangeListner = new NetworkChangeListner();
     long backPressedTime = 0; // store last back press time
     Toast backToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        rememberMe = findViewById(R.id.rememberMe);
        loginButton = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forgotPassword);
        createAccount = findViewById(R.id.createAccount);

        // Login button click event
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                // Validation
                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("Registering User Please Wait");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    loginUser(email, password);
                }
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(i);
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(i);
            }
        });

    }

    private void loginUser(String loginId, String password) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.LoginUser,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int success = jsonObject.getInt("success");

                        if (success == 1) {
                            JSONObject userData = jsonObject.getJSONObject("userdata");

                            String id       = userData.getString("id");
                            String name     = userData.getString("name");
                            String mobileno = userData.getString("mobileno");
                            String email    = userData.getString("email");
                            String address  = userData.getString("address");
                            String image    = userData.getString("image");
                            String userrole = userData.getString("userrole");

                            // Example: Save to SharedPreferences
                            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("id", id);
                            editor.putString("name", name);
                            editor.putString("mobileno", mobileno);
                            editor.putString("email", email);
                            editor.putString("address", address);
                            editor.putString("image", image);
                            editor.putString("userrole", userrole);
                            editor.putBoolean("isFirstTime", true);
                            editor.apply();

                            progressDialog.dismiss();
                            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                            Intent i;
                            if (userrole.equalsIgnoreCase("Customer")) {
                                i = new Intent(LoginActivity.this, HomeActvity.class);
                            } else if (userrole.equalsIgnoreCase("Salon Owner")) {
                                i = new Intent(LoginActivity.this, ShopHomeActivity.class);
                            } else {
                                i = new Intent(LoginActivity.this, HomeActvity.class);
                            }

                            startActivity(i);
                            finish();


                        } else {
                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Parsing Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("login_id", loginId);
                params.put("password", password);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
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
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) { // 2 seconds check
            if (backToast != null) backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}