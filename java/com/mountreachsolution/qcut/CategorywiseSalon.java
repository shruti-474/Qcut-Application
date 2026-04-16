package com.mountreachsolution.qcut;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mountreachsolution.qcut.ADAPTER.CateAdapter;
import com.mountreachsolution.qcut.ADAPTER.ShopAdapter;
import com.mountreachsolution.qcut.POJO.Shop;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategorywiseSalon extends AppCompatActivity {

    TextView tvTitle;
    RecyclerView recyclerViewCategories;
    String category;
    SearchView searchView;
    CateAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Draw edge-to-edge
        setContentView(R.layout.activity_categorywise_salon);
        category=getIntent().getStringExtra("category");
        tvTitle = findViewById(R.id.tvTitle);
        searchView=findViewById(R.id.searchView);
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CategorywiseSalon.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCategories.setLayoutManager(layoutManager);
        fetchShopData(category);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.pink));
        }

// White icons on pink
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(0);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });





        // Make TextView appear below status bar
        ViewCompat.setOnApplyWindowInsetsListener(tvTitle, (view, insets) -> {
            int statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top;
            view.setPadding(view.getPaddingLeft(), statusBarHeight + 16, view.getPaddingRight(), view.getPaddingBottom());
            return insets;
        });

        tvTitle.setText("Your Look, Your Choice");

        // RecyclerView setup (LayoutManager + Adapter)
    }
    public void fetchShopData(String type) {
        RequestQueue queue = Volley.newRequestQueue(CategorywiseSalon.this);

        StringRequest request = new StringRequest(Request.Method.POST, url.getCategoryShop,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray("getShop");
                        List<Shop> shopList = new ArrayList<>();

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            shopList.add(new Shop(
                                    obj.getString("id"),
                                    obj.getString("name"),
                                    obj.getString("address"),
                                    obj.getString("time"),
                                    obj.getString("day"),
                                    obj.getString("service"),
                                    obj.getString("image"),
                                    obj.getString("type"),
                                    obj.getString("lattitude"),
                                    obj.getString("lobgitude"),
                                    obj.getString("shopemail")
                            ));
                        }

                         adapter = new CateAdapter(shopList, CategorywiseSalon.this);
                        recyclerViewCategories.setAdapter(adapter);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> error.printStackTrace()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("type", type);  // ✅ sending form-data
                return params;
            }
        };

        queue.add(request);
    }


}
