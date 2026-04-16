package com.mountreachsolution.qcut;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    RadioGroup userTypeGroup;
    RadioButton radioCustomer, radioSalonOwner;
    EditText nameInput, emailInput, phoneInput, addressInput, passwordInput, confirmPasswordInput;
    CheckBox rememberMe;
    AppCompatButton signUpButton;
    TextView signInLink;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        userTypeGroup = findViewById(R.id.userTypeGroup);
        radioCustomer = findViewById(R.id.radioCustomer);
        radioSalonOwner = findViewById(R.id.radioSalonOwner);
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        addressInput = findViewById(R.id.addressInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        rememberMe = findViewById(R.id.rememberMe);
        signUpButton = findViewById(R.id.signUpButton);
        signInLink = findViewById(R.id.signInLink);

        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        // Sign Up Button Click
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get values
                String userType = ((RadioButton) findViewById(userTypeGroup.getCheckedRadioButtonId())).getText().toString();
                String name = nameInput.getText().toString().trim();
                String email = emailInput.getText().toString().trim();
                String phone = phoneInput.getText().toString().trim();
                String address = addressInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String confirmPassword = confirmPasswordInput.getText().toString().trim();

                // Validation
                if (name.isEmpty() || email.isEmpty() || phone.isEmpty() ||
                        address.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!phone.matches("\\d{10}")) {
                    Toast.makeText(SignupActivity.this, "Phone number must be 10 digits", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!email.endsWith("@gmail.com")) {
                    Toast.makeText(SignupActivity.this, "Email must be a valid Gmail address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // All validations passed → Call register method
                progressDialog = new ProgressDialog(SignupActivity.this);
                progressDialog.setMessage("Registering User Please Wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                registerUser(name,email,address,phone,password,"null",userType);
            }
        });

    }


    private void registerUser(String name, String email, String address, String phone,  String password, String image, String userRole) {


        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.RegisterUser,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int success = jsonObject.getInt("success");
                        String message = jsonObject.getString("message");

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                        if (success == 1) {
                            // Registration successful
                            // You can redirect to login or main activity
                            progressDialog.dismiss();
                            Intent i = new Intent(SignupActivity.this,LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Response parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("address", address);
                params.put("mobileno", phone);
                params.put("password", password);
                params.put("image", image);
                params.put("userrole", userRole);
                return params;
            }
        };

        queue.add(stringRequest);
    }

}