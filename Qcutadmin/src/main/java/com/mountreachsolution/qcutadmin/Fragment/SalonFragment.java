package com.mountreachsolution.qcutadmin.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mountreachsolution.qcutadmin.ADAPTER.ShopAdapter;
import com.mountreachsolution.qcutadmin.POJO.Shop;
import com.mountreachsolution.qcutadmin.R;
import com.mountreachsolution.qcutadmin.comman.url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SalonFragment extends Fragment {
    private RecyclerView recyclerViewFav;
    private TextView textViewNoFav;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_salon, container, false);

        recyclerViewFav = view.findViewById(R.id.recyclerViewFav);
        textViewNoFav = view.findViewById(R.id.textViewNoFav);

        recyclerViewFav.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFav.setHasFixedSize(true);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Salons...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        fetchShopData();
        return view;
    }

    private void fetchShopData() {
        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url.getShop, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray array = response.getJSONArray("getShop");
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

                            if (shopList.isEmpty()) {
                                recyclerViewFav.setVisibility(View.GONE);
                                textViewNoFav.setVisibility(View.VISIBLE);
                            } else {
                                recyclerViewFav.setVisibility(View.VISIBLE);
                                textViewNoFav.setVisibility(View.GONE);

                                ShopAdapter adapter = new ShopAdapter(shopList, getContext());
                                recyclerViewFav.setAdapter(adapter);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            recyclerViewFav.setVisibility(View.GONE);
                            textViewNoFav.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
                recyclerViewFav.setVisibility(View.GONE);
                textViewNoFav.setVisibility(View.VISIBLE);
            }
        });

        queue.add(request);
    }
}
