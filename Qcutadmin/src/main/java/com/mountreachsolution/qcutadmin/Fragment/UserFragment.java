package com.mountreachsolution.qcutadmin.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.mountreachsolution.qcutadmin.ADAPTER.UserAdapter;
import com.mountreachsolution.qcutadmin.LogInActvity;
import com.mountreachsolution.qcutadmin.R;
import com.mountreachsolution.qcutadmin.User;
import com.mountreachsolution.qcutadmin.comman.url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class UserFragment extends Fragment {
    MaterialToolbar toolbar;
    SearchView searchView;
    RecyclerView recyclerView;
    TextView textViewNoData;
    List<User>userList;

    UserAdapter userAdapter;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user, container, false);

        searchView = view.findViewById(R.id.serchView);
        recyclerView = view.findViewById(R.id.recyclerViewFav);
        textViewNoData = view.findViewById(R.id.textViewNoFav);

        toolbar = view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.topmenu); // your menu XML with logout

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.btLogout) {
                logoutUser();
                return true;
            }
            return false;
        });

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userList=new ArrayList<>();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading User");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        loadUsers();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (userAdapter != null) {
                    userAdapter.getFilter().filter(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (userAdapter != null) {
                    userAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });

        return view;

    }

    private void logoutUser() {
        // Clear login SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("AdminLogin", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear(); // remove all saved login info
        editor.apply();

        // Move to login page
        Intent intent = new Intent(getActivity(), LogInActvity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear back stack
        startActivity(intent);
        getActivity().finish();
    }

    private void loadUsers() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url.getAllUser, null,
                response -> {
                    try {
                        JSONArray array = response.getJSONArray("getUserData");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            userList.add(new User(
                                    obj.getString("id"),
                                    obj.getString("name"),
                                    obj.getString("mobile"),
                                    obj.getString("email"),
                                    obj.getString("adddress"),
                                    obj.getString("password"),
                                    obj.getString("image"),
                                    obj.getString("userrole")
                            ));
                        }

                        if (userList.isEmpty()) {
                            recyclerView.setVisibility(RecyclerView.GONE);
                            textViewNoData.setVisibility(TextView.VISIBLE);
                            progressDialog.dismiss();
                        } else {
                            recyclerView.setVisibility(RecyclerView.VISIBLE);
                            textViewNoData.setVisibility(TextView.GONE);
                            userAdapter=new UserAdapter(getActivity(),userList);
                            recyclerView.setAdapter(userAdapter);
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        Log.e("VolleyError", e.toString());
                    }
                },
                error -> Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        queue.add(request);
    }
}