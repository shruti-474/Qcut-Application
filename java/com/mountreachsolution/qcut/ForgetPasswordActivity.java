package com.mountreachsolution.qcut;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends AppCompatActivity {
    EditText emailInput, newPassword;
     AppCompatButton updatePasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        // Initialize views
        emailInput = findViewById(R.id.emailInput);
        newPassword = findViewById(R.id.newPassword);
        updatePasswordBtn = findViewById(R.id.updatePassword);

        // Button click listener
        updatePasswordBtn.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = newPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                updatePassword(email, password); // Run your method
            }
        });

    }

    private void updatePassword(String email, String password) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.updatePassword,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int success = jsonObject.getInt("success");
                        String message = jsonObject.getString("message");

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                        if (success == 1) {
                            // Password updated successfully, maybe finish activity
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "JSON parse error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        // Set retry policy if needed
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000, // Timeout in ms
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add request to queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}