package com.mountreachsolution.qcutadmin.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mountreachsolution.qcutadmin.ADAPTER.AdapterHistory;
import com.mountreachsolution.qcutadmin.POJO.POJOHis;
import com.mountreachsolution.qcutadmin.R;
import com.mountreachsolution.qcutadmin.comman.url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppoinmentFFragment extends Fragment {

    private RecyclerView recyclerViewFav;
    private TextView textViewNoFav;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appoinment_f, container, false);

        recyclerViewFav = view.findViewById(R.id.recyclerViewFav);
        textViewNoFav = view.findViewById(R.id.textViewNoFav);

        // Setup RecyclerView
        recyclerViewFav.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewFav.setHasFixedSize(true);

        // Setup ProgressDialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Appointments...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        fetchOrders();

        return view;
    }

    private void fetchOrders() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest request = new StringRequest(Request.Method.POST, url.getAppoinmentHis,
                response -> {
                    Log.d("HistoryResponse", response); // Logging full response
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

                        progressDialog.dismiss();

                        if (orderList.isEmpty()) {
                            recyclerViewFav.setVisibility(View.GONE);
                            textViewNoFav.setVisibility(View.VISIBLE);
                        } else {
                            recyclerViewFav.setVisibility(View.VISIBLE);
                            textViewNoFav.setVisibility(View.GONE);

                            AdapterHistory adapter = new AdapterHistory(orderList, getActivity());
                            recyclerViewFav.setAdapter(adapter);
                        }

                    } catch (Exception e) {
                        Log.e("HistoryParseError", e.toString());
                        progressDialog.dismiss();
                        recyclerViewFav.setVisibility(View.GONE);
                        textViewNoFav.setVisibility(View.VISIBLE);
                    }
                },
                error -> {
                    Log.e("HistoryVolleyError", error.toString());
                    progressDialog.dismiss();
                    recyclerViewFav.setVisibility(View.GONE);
                    textViewNoFav.setVisibility(View.VISIBLE);
                });

        queue.add(request);
    }
}
