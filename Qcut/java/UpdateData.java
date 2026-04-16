package com.mountreachsolution.qcut;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UpdateData extends AppCompatActivity {

    private EditText etServices;
    private AppCompatButton btnUpdateServices;
    
    String  email;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_data);

        etServices = findViewById(R.id.etServices);
        btnUpdateServices = findViewById(R.id.btnUpdateServices);
        btnUpdateServices.setOnClickListener(v -> {
            String services = etServices.getText().toString().trim();
            SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
            email = prefs.getString("email", "");

            // Validation
            if (services.isEmpty()) {
                etServices.setError("Service list cannot be empty");
                etServices.requestFocus();
                return;
            }

            if (services.endsWith(",")) {
                etServices.setError("Service list should not end with ','");
                etServices.requestFocus();
                return;
            }

            // ✅ If all validations pass, run your update method
            progressDialog = new ProgressDialog(UpdateData.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            updateServicesOnServer(services,email);
        });

    }

    private void updateServicesOnServer(String services, String email) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("shopemail", email);
        params.put("service", ","+services);



        client.post(url.updateservice, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    boolean success = response.getBoolean("success");
                    String message = response.getString("message");
                    progressDialog.dismiss();
                    Toast.makeText(UpdateData.this, message, Toast.LENGTH_SHORT).show();
                    etServices.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(UpdateData.this, "Parsing error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(UpdateData.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}