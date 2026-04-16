package com.mountreachsolution.qcut;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mountreachsolution.qcut.ADAPTER.AdapterHistory;
import com.mountreachsolution.qcut.POJO.POJOHis;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Histroy extends AppCompatActivity {
    RecyclerView recyclerViewFav;
    TextView textViewNoFav;
    String userEmail;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histroy);

        recyclerViewFav = findViewById(R.id.recyclerViewFav);
        textViewNoFav = findViewById(R.id.textViewNoFav);
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        userEmail = prefs.getString("email", ""); // second parameter is the default value if not found


        // Setup RecyclerView
        recyclerViewFav.setLayoutManager(new LinearLayoutManager(Histroy.this ,LinearLayoutManager.VERTICAL, false));
        recyclerViewFav.setHasFixedSize(true);
        fetchOrders();
        progressDialog = new ProgressDialog(Histroy.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
       
    }

    private void fetchOrders() {

        RequestQueue queue = Volley.newRequestQueue(Histroy.this);

        StringRequest request = new StringRequest(Request.Method.POST, url.getUserHistory,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("getOrders");

                        List<POJOHis> orderList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            POJOHis order = new POJOHis(
                                    obj.getString("id"),
                                    obj.getString("shopname"),
                                    obj.getString("shopaddress"),
                                    obj.getString("shopemail"),
                                    obj.getString("finalname"),
                                    obj.getString("finalemail"),
                                    obj.getString("finaldate"),
                                    obj.getString("finaltime"),
                                    obj.getString("finalservice"),
                                    obj.getString("totalamount"),
                                    obj.getString("paymentMode"),
                                    obj.getString("paymentStatus"),
                                    obj.getString("Appoinmentstatus")

                            );
                            orderList.add(order);
                        }

                        if (orderList.isEmpty()) {
                            // No data -> show text, hide recycler
                            progressDialog.dismiss();
                            recyclerViewFav.setVisibility(View.GONE);
                            textViewNoFav.setVisibility(View.VISIBLE);
                        } else {
                            // Data exists -> show recycler, hide text
                            progressDialog.dismiss();
                            textViewNoFav.setVisibility(View.GONE);
                            recyclerViewFav.setVisibility(View.VISIBLE);

                            AdapterHistory adaptershopAppoinment = new AdapterHistory(orderList,Histroy.this);
                            recyclerViewFav.setAdapter(adaptershopAppoinment);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        recyclerViewFav.setVisibility(View.GONE);
                        textViewNoFav.setVisibility(View.VISIBLE);
                    }
                },
                error -> {
                    error.printStackTrace();
                    recyclerViewFav.setVisibility(View.GONE);
                    textViewNoFav.setVisibility(View.VISIBLE);
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("finalemail", userEmail);
                return params;
            }
        };

        queue.add(request);
    }
}