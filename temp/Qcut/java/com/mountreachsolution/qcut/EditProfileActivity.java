package com.mountreachsolution.qcut;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mountreachsolution.qcut.common.VolleyMultipartRequest;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    ImageView ivBack, getImage;
    CircleImageView profileImage;
    TextView userName, userRole, passwordError;
    EditText etEmail, phoneNumber, password;
    EditText dobDay, dobMonth, dobYear;
    AppCompatButton saveBtn;
    Bitmap bitmap;
    Uri filepath;
    String email1;
    private int pick_image_request = 789;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Top bar icons
        ivBack = findViewById(R.id.ivBack);


        // Profile section
        profileImage = findViewById(R.id.profileImage);
        userName = findViewById(R.id.userName);
        userRole = findViewById(R.id.userRole);
        getImage = findViewById(R.id.getImage);

        // Input fields
        etEmail = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.PhoneNumber);
        password = findViewById(R.id.password);


        // Password error
        passwordError = findViewById(R.id.passwordError);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        email1 = prefs.getString("email", "");

        // Load user data
        getUserData(email1);

        // Image picker
        getImage.setOnClickListener(v -> SelectUserProfileImage());
//
//        // Save button click
//        saveBtn.setOnClickListener(v -> {
//            String stEmail = etEmail.getText().toString().trim();
//            String stNumber = phoneNumber.getText().toString().trim();
//            String stDobDay = dobDay.getText().toString().trim();
//            String stDobMonth = dobMonth.getText().toString().trim();
//            String stDobYear = dobYear.getText().toString().trim();
//
//            // Empty check
//            if (stEmail.isEmpty() || stNumber.isEmpty() || stDobDay.isEmpty() || stDobMonth.isEmpty() || stDobYear.isEmpty()) {
//                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Email format check
//            if (!Patterns.EMAIL_ADDRESS.matcher(stEmail).matches()) {
//                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Phone number check
//            if (!stNumber.matches("\\d{10}")) {
//                Toast.makeText(this, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // Date check
//            int day, month, year;
//            try {
//                day = Integer.parseInt(stDobDay);
//                month = Integer.parseInt(stDobMonth);
//                year = Integer.parseInt(stDobYear);
//
//                Calendar cal = Calendar.getInstance();
//                cal.setLenient(false);
//                cal.set(year, month - 1, day);
//                cal.getTime(); // throws exception if invalid
//            } catch (Exception e) {
//                Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            // All good → update
//            updateProfile(stEmail, stNumber, stDobDay, stDobMonth, stDobYear);
//        });
    }

    private void updateProfile(String newEmail, String mobileNo, String dobDay, String dobMonth, String dobYear) {
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String oldEmail = prefs.getString("email", ""); // Saved old email
        String birthdate = dobYear + "-" +
                String.format("%02d", Integer.parseInt(dobMonth)) + "-" +
                String.format("%02d", Integer.parseInt(dobDay));

        // Log data before sending
        Log.d("DEBUG_INPUT", "Sending Data -> old_email: " + oldEmail +
                ", email: " + newEmail +
                ", mobileno: " + mobileNo +
                ", birthdate: " + birthdate);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.updateUserdata,
                response -> {
                    Log.d("UPDATE_PROFILE", "Server Response: " + response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getInt("success") == 1) {
                            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("email", newEmail);
                            editor.putString("mobileno", mobileNo);
                            editor.putString("birthdate", birthdate);
                            editor.apply();

                            getUserData(newEmail);
                        } else {
                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Response parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("UPDATE_PROFILE_ERR", "Volley Error: " + error.toString());
                    Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("old_email", oldEmail);
                params.put("email", newEmail);
                params.put("mobileno", mobileNo);
                params.put("birthdate", birthdate);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
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
                profileImage.setImageBitmap(bitmap);
                UserImageSaveToDatabase(bitmap, etEmail.getText().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void UserImageSaveToDatabase(Bitmap bitmap, String strTitle) {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url.UplodeImage,
                response -> Toast.makeText(this, "Image saved as profile for " + strTitle, Toast.LENGTH_SHORT).show(),
                error -> {
                    String errorMsg = error.getMessage();
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

    private void getUserData(String email) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.GetprofilData,
                response -> {
                    Log.d("API_RESPONSE", response);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getInt("success") == 1) {
                            JSONObject userData = jsonObject.getJSONObject("userdata");
                            String name = userData.getString("name");
                            String mobile = userData.getString("mobileno");
                            String email0 = userData.getString("email");
                            String image1 = userData.getString("image");
                            String userrole = userData.getString("userrole");

                            userName.setText(name);
                            userRole.setText(userrole);
                            etEmail.setText(email0);
                            phoneNumber.setText(mobile);

                            Glide.with(this)
                                    .load(url.address + "image/" + image1)
                                    .skipMemoryCache(true)
                                    .error(R.drawable.baseline_person_24)
                                    .into(profileImage);
                        } else {
                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("VOLLEY_ERROR", error.toString())) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
