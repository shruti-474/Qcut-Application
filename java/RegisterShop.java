package com.mountreachsolution.qcut;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolution.qcut.common.VolleyMultipartRequest;
import com.mountreachsolution.qcut.common.url;

import android.Manifest;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class RegisterShop extends AppCompatActivity {

    EditText etName, etAddress, etStartTime, etEndTime, etDay, etType, etService, etLatitude, etLongitude,etrating;
    ImageView ivShopImage;
    Button btAddImage;
    AppCompatButton registerShop;
    private int pick_image_request = 789;
    // Public Strings to store data
    public String salonName, salonAddress, openTime, salonDay, salonType, salonService, salonLocation;
    double latitude, longitude;

    private FusedLocationProviderClient fusedLocationProviderClient;
    ProgressDialog progressDialog;
    Bitmap bitmap;
    Uri filepath;
    String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_shop);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);

        progressDialog=new ProgressDialog(RegisterShop.this);
        progressDialog.setMessage("Register Shop");
        progressDialog.setCanceledOnTouchOutside(false);


// Retrieve data
        String id       = prefs.getString("id", "");
        String name     = prefs.getString("name", "");
        String mobileno = prefs.getString("mobileno", "");
         email    = prefs.getString("email", "");
        String address  = prefs.getString("address", "");
        String image    = prefs.getString("image", "");
        String userrole = prefs.getString("userrole", "");

        etName = findViewById(R.id.etname);
        etAddress = findViewById(R.id.etAddress);
        etStartTime = findViewById(R.id.etStratTime);
        etEndTime = findViewById(R.id.etEndTime);
        etDay = findViewById(R.id.etDay);
        etType = findViewById(R.id.etType);
        etService = findViewById(R.id.etService);
        etLatitude = findViewById(R.id.etlatitude);
        etLongitude = findViewById(R.id.etLongitude);
        ivShopImage = findViewById(R.id.ivshopimage);
        btAddImage = findViewById(R.id.btaddimage);
        registerShop = findViewById(R.id.registershop);
        etrating =findViewById(R.id.etRating);
        btAddImage.setOnClickListener(v -> SelectUserProfileImage());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Start Time Picker
        etStartTime.setOnClickListener(v -> showTimePicker(etStartTime));

        // End Time Picker
        etEndTime.setOnClickListener(v -> showTimePicker(etEndTime));

        // Get Current Location
        getCurrentLocation();

        // Salon Opened For (Type)
        etType.setOnClickListener(v -> {
            String[] types = {"Men", "Women", "Kids", "Unisex"};
            new AlertDialog.Builder(this)
                    .setTitle("Select Salon Type")
                    .setItems(types, (dialog, which) -> etType.setText(types[which]))
                    .show();
        });

        // Register Shop Button
        registerShop.setOnClickListener(v -> {
            if (validateInputs()) {
                // Store Data
                salonName = etName.getText().toString();
                salonAddress = etAddress.getText().toString();
                openTime = etStartTime.getText().toString() + " to " + etEndTime.getText().toString();
                salonDay = etDay.getText().toString();
                salonType = etType.getText().toString();
                salonService = etService.getText().toString();
                salonLocation = etLatitude.getText().toString() + "," + etLongitude.getText().toString();

                // Call register method
                progressDialog.show();
                registerShopOnServer(
                        RegisterShop.this,
                        salonName,
                        salonAddress,
                        openTime,    // "8 AM to 9 PM"
                        salonDay,
                        salonService,
                        "null", // you can pass image file name or URL
                        salonType,
                        String.valueOf(latitude),
                        String.valueOf(longitude),
                        email
                );

            }
        });
    }

    private void SelectUserProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image For Profile"), pick_image_request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pick_image_request && resultCode == RESULT_OK && data != null) {
            filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                ivShopImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void UserImageSaveToDatabase(Bitmap bitmap, String strTitle) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url.shopimage,
                response -> {
                    // ✅ Success response
                    progressDialog.dismiss();
                    Toast.makeText(this, "Image saved as profile for " + strTitle, Toast.LENGTH_SHORT).show();
                    // Move back
                    onBackPressed();   // Or finish();
                },
                error -> {
                    String errorMsg = error.getMessage();
                    progressDialog.dismiss();
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        errorMsg = new String(error.networkResponse.data);
                    }
                    Log.e("UploadError", errorMsg);
                    Toast.makeText(this, "Upload Error: " + errorMsg, Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("tags", strTitle);
                return parms;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> parms = new HashMap<>();
                long imagename = System.currentTimeMillis();
                parms.put("pic", new DataPart(imagename + ".jpeg", getFileDataFromBitmap(bitmap)));
                return parms;
            }
        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    private byte[] getFileDataFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void showTimePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute1) -> {
                    String amPm = (hourOfDay >= 12) ? "PM" : "AM";
                    int hourFormatted = (hourOfDay == 0 || hourOfDay == 12) ? 12 : hourOfDay % 12;
                    editText.setText(hourFormatted + ":" + String.format("%02d", minute1) + " " + amPm);
                }, hour, minute, false);
        timePickerDialog.show();
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                etLatitude.setText(String.valueOf(latitude));
                etLongitude.setText(String.valueOf(longitude));
            }
        });
    }

    private boolean validateInputs() {
        // Service validation format check
        String serviceText = etService.getText().toString().trim();
        if (!serviceText.matches("([A-Za-z ]+-[0-9]+)(, *[A-Za-z ]+-[0-9]+)*")) {
            etService.setError("Format must be: Haircut-100,Beard Trim-200");
            return false;
        }
        return true;
    }



    public void registerShopOnServer(Context context,
                                     String name,
                                     String address,
                                     String time,
                                     String day,
                                     String service,
                                     String image,
                                     String type,
                                     String latitude,
                                     String longitude,
                                     String shopemail) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        // Put all required POST params
        params.put("namea", name);
        params.put("address", address);
        params.put("time", time); // Example: 8 AM to 9 PM
        params.put("day", day);
        params.put("service", service); // Haircut-100,Beard Trim-200
        params.put("image", image); // Image URL or filename
        params.put("type", type);   // Men, Women, Kids, Unisex
        params.put("lattitude", latitude);
        params.put("logitude", longitude);
        params.put("shopemail", shopemail);
        params.put("rating", etrating.getText().toString().trim());


        client.post(url.RegisterShop, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int success = response.getInt("success");
                    String message = response.getString("message");

                    if (success == 1) {
                        UserImageSaveToDatabase(bitmap,email);
                        Toast.makeText(context, "✅ " + message, Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(context, "❌ " + message, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(context, "Error parsing server response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                Toast.makeText(context, "Failed: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
