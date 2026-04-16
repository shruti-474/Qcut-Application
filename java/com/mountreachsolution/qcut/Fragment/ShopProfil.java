package com.mountreachsolution.qcut.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mountreachsolution.qcut.AppointmentsActivity;
import com.mountreachsolution.qcut.EditProfileActivity;
import com.mountreachsolution.qcut.FavoriteActivity;
import com.mountreachsolution.qcut.LoginActivity;
import com.mountreachsolution.qcut.PrivacyActivity;
import com.mountreachsolution.qcut.R;
import com.mountreachsolution.qcut.RegisterShop;
import com.mountreachsolution.qcut.UpdateActivity;
import com.mountreachsolution.qcut.UpdateData;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ShopProfil extends Fragment {

    LinearLayout appointmentsRow, favoriteRow, privacyRow,updateRow;
    ImageView editIcon;
    TextView logoutText,tvusername,tvRole;

    CircleImageView profileImage;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_shop_profil, container, false);
        appointmentsRow = view.findViewById(R.id.appointmentsRow);
        favoriteRow = view.findViewById(R.id.favoriteRow);
        privacyRow = view.findViewById(R.id.privacyRow);
        editIcon = view.findViewById(R.id.editIcon);
        logoutText = view.findViewById(R.id.logoutText);
        tvusername = view.findViewById(R.id.profileName);
        tvRole = view.findViewById(R.id.profileJob);
        profileImage = view.findViewById(R.id.profileImage);
        updateRow=view.findViewById(R.id.updaterow);

        SharedPreferences prefs = getContext().getSharedPreferences("UserData", MODE_PRIVATE);

// Retrieve data
        String id       = prefs.getString("id", "");
        String name     = prefs.getString("name", "");
        String mobileno = prefs.getString("mobileno", "");
        String email    = prefs.getString("email", "");
        String address  = prefs.getString("address", "");
        String image    = prefs.getString("image", "");
        String userrole = prefs.getString("userrole", "");

        tvusername.setText(name);
        tvRole.setText(userrole);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        getUserData(email);



        // Click Listeners
        appointmentsRow.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RegisterShop.class);
            startActivity(intent);
        });

        favoriteRow.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UpdateData.class);
            startActivity(intent);
        });

        privacyRow.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PrivacyActivity.class);
            startActivity(intent);
        });

        updateRow.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UpdateActivity.class);
            intent.putExtra("email",email);
            startActivity(intent);
        });


        editIcon.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });

        logoutText.setOnClickListener(v -> {
            // Clear saved user data
            progressDialog.show();
            SharedPreferences prefss = getContext().getSharedPreferences("UserData", MODE_PRIVATE);
            prefss.edit().clear().apply();

            // Move to login screen

            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            progressDialog.dismiss();
        });
        return view;
    }

    private void getUserData(String email) {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url.GetprofilData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // You get the response as a String here
                        Log.d("API_RESPONSE", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("success") == 1) {
                                JSONObject userData = jsonObject.getJSONObject("userdata");
                                String name = userData.getString("name");
                                String mobile = userData.getString("mobileno");
                                String email = userData.getString("email");
                                String address = userData.getString("address");
                                String image1 = userData.getString("image");
                                String userrole = userData.getString("userrole");

                                Glide.with(getActivity())
                                        .load(url.address + "image/"+image1)
                                        .skipMemoryCache(true)
                                        .error(R.drawable.baseline_person_24)// Resize the image to 800x800 pixels
                                        .into(profileImage);
                                progressDialog.dismiss();

                                // Do something with the data
                                Log.d("USER_NAME", name);
                            } else {
                                Toast.makeText(getContext(),
                                        jsonObject.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY_ERROR", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}