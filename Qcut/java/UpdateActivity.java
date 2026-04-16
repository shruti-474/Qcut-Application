package com.mountreachsolution.qcut;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UpdateActivity extends AppCompatActivity {
    Spinner spinnerShopStatus;
    EditText etSetsAvailable;
    Button btnUpdate;
    String email,status,sets;
    Intent i;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        spinnerShopStatus = findViewById(R.id.spinnerShopStatus);
        etSetsAvailable = findViewById(R.id.etSetsAvailable);
        btnUpdate = findViewById(R.id.btnUpdate);
        email = getIntent().getStringExtra("email");
//        Toast.makeText(this,"Login Email :"+email, Toast.LENGTH_SHORT).show();

        // Set Spinner Options
        String[] statusOptions = {"Open", "Close"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, statusOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerShopStatus.setAdapter(adapter);

        progressDialog = new ProgressDialog(UpdateActivity.this);
        progressDialog.setMessage("Changing Status");
        progressDialog.setCanceledOnTouchOutside(false);

        // Update button click
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                validateAndSubmit();
            }
        });

    }

    private void validateAndSubmit() {

            String shopStatustat = spinnerShopStatus.getSelectedItem().toString();
            String setsAvailable = etSetsAvailable.getText().toString().trim();

            if (setsAvailable.isEmpty()) {
                Toast.makeText(this, "Fill all the fields properly", Toast.LENGTH_SHORT).show();
                return;
            }

        try {
            int sets = Integer.parseInt(setsAvailable);
            if (sets > 10) {
                Toast.makeText(this, "Sets number should not exceed 10", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show();
            return;
        }

        checkEmailFromServer(email,shopStatustat,setsAvailable);
    }

    private void checkEmailFromServer(String email,String status,String sets) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", email);

        client.post(url.checkstore, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int success = response.getInt("success");
                    String message = response.getString("message");

                    if (success == 1) {
//                        Toast.makeText(UpdateActivity.this, "Store present", Toast.LENGTH_SHORT).show();
                        updateShop(email,status,sets);
                    } else {
                        Toast.makeText(UpdateActivity.this, "Register shop first", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(UpdateActivity.this, "Parsing error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(UpdateActivity.this, "Server error: " + statusCode, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateShop(String email, String status, String sets) {


        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("status", status);
        params.put("totalsets", sets);

        client.post(url.updateShop, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int success = response.getInt("success");
                    String message = response.getString("message");

                    if (success == 1) {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateActivity.this, "✅ " + message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UpdateActivity.this, "❌ " + message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(UpdateActivity.this, "Parsing error!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(UpdateActivity.this, "Server error: " + statusCode, Toast.LENGTH_SHORT).show();
            }
        });
    }


}