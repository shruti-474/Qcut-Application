package com.mountreachsolution.qcut;

import static java.security.AccessController.getContext;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mountreachsolution.qcut.ADAPTER.FavAdapter;
import com.mountreachsolution.qcut.ADAPTER.ShopAdapter;
import com.mountreachsolution.qcut.POJO.POJOFav;
import com.mountreachsolution.qcut.POJO.Shop;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteActivity extends AppCompatActivity {
    RecyclerView recyclerViewFav;
    TextView textViewNoFav;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        recyclerViewFav = findViewById(R.id.recyclerViewFav);
        textViewNoFav = findViewById(R.id.textViewNoFav);
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        userEmail = prefs.getString("email", ""); // second parameter is the default value if not found


        LinearLayoutManager layoutManager = new LinearLayoutManager(FavoriteActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewFav.setLayoutManager(layoutManager);
        fetchShopData(userEmail);

    }

    public void fetchShopData(String userEmail) {

        RequestQueue queue = Volley.newRequestQueue(FavoriteActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url.getFav,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("getShop");

                            List<POJOFav> pojoFavs = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject obj = array.getJSONObject(i);

                                pojoFavs.add(new POJOFav(
                                        obj.getString("id"),
                                        obj.getString("name"),
                                        obj.getString("address"),
                                        obj.getString("time"),
                                        obj.getString("day"),
                                        obj.getString("service"),
                                        obj.getString("image"),
                                        obj.getString("type"),
                                        obj.getString("lattitude"),
                                        obj.getString("lobgitude"),  // note spelling!
                                        obj.getString("shopemail"),
                                        obj.getString("shopId"),
                                        obj.getString("useerEmail")
                                ));
                            }

                            // Handle empty list vs filled list
                            if (pojoFavs.isEmpty()) {
                                recyclerViewFav.setVisibility(View.GONE);
                                textViewNoFav.setVisibility(View.VISIBLE);
                            } else {
                                recyclerViewFav.setVisibility(View.VISIBLE);
                                textViewNoFav.setVisibility(View.GONE);

                                FavAdapter adapter = new FavAdapter(pojoFavs, FavoriteActivity.this);
                                recyclerViewFav.setAdapter(adapter);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(FavoriteActivity.this, "Parsing Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(FavoriteActivity.this, "Volley Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userEmail", userEmail);
                return params;
            }
        };

        queue.add(request);
    }


}