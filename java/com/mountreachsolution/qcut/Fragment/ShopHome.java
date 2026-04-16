package com.mountreachsolution.qcut.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mountreachsolution.qcut.ADAPTER.AdaptershopAppoinment;
import com.mountreachsolution.qcut.AppointmentsActivity;
import com.mountreachsolution.qcut.POJO.Order;
import com.mountreachsolution.qcut.R;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShopHome extends Fragment {
    RecyclerView recyclerViewFav;
    TextView textViewNoFav;
    String userEmail;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_shop_home, container, false);
        recyclerViewFav = view.findViewById(R.id.recyclerViewFav);
        textViewNoFav = view.findViewById(R.id.textViewNoFav);
        SharedPreferences prefs = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
        userEmail = prefs.getString("email", ""); // second parameter is the default value if not found


        // Setup RecyclerView
        recyclerViewFav.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewFav.setHasFixedSize(true);
        fetchOrders();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        return view;
    }

    private void fetchOrders() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url.getAppoinmentShop,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("getOrders");

                        List<Order> orderList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);

                            Order order = new Order(
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
                                    obj.getString("setno"),
                                    obj.getString("statu")
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

                            AdaptershopAppoinment adaptershopAppoinment = new AdaptershopAppoinment(orderList,getActivity());
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