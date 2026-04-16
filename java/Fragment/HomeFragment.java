package com.mountreachsolution.qcut.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.mountreachsolution.qcut.ADAPTER.AdapterHomePageTopProduct;
import com.mountreachsolution.qcut.ADAPTER.ShopAdapter;
import com.mountreachsolution.qcut.AppointmentsActivity;
import com.mountreachsolution.qcut.FavoriteActivity;
import com.mountreachsolution.qcut.Histroy;
import com.mountreachsolution.qcut.ImageSliderAdapter;
import com.mountreachsolution.qcut.MainActivity;
import com.mountreachsolution.qcut.MainActivity2;
import com.mountreachsolution.qcut.POJO.PojoHomeCategory;
import com.mountreachsolution.qcut.POJO.Shop;
import com.mountreachsolution.qcut.R;
import com.mountreachsolution.qcut.ReportActivity;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ViewPager2 viewPagerSlider;
    private List<String> sliderImages;
    private ImageSliderAdapter sliderAdapter;

    private Handler sliderHandler = new Handler();
    private Runnable sliderRunnable;

    RecyclerView rvcategory;
    List<PojoHomeCategory>pojoHomeCategories;
    AdapterHomePageTopProduct adapterHomePageTopProduct;

    RecyclerView rvshop;

    public HomeFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rvcategory=view.findViewById(R.id.rvHomeFrgmentCategory);
        rvcategory.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        pojoHomeCategories=new ArrayList<>();
        getAllCtegoryofProduct();


         rvshop = view.findViewById(R.id.rvShop);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvshop.setLayoutManager(layoutManager);
        fetchShopData();
        // Enable menu for this fragment
        setHasOptionsMenu(true);

        // Toolbar setup
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);

            if (toolbar.getOverflowIcon() != null) {
                toolbar.getOverflowIcon().setTint(getResources().getColor(android.R.color.white));
                toolbar.setOverflowIcon(toolbar.getOverflowIcon());
                toolbar.setPadding(
                        toolbar.getPaddingLeft(),
                        toolbar.getPaddingTop() + 16,
                        toolbar.getPaddingRight(),
                        toolbar.getPaddingBottom()
                );
            }
        }

        // ViewPager2 setup
        viewPagerSlider = view.findViewById(R.id.viewPagerSlider);
        sliderImages = new ArrayList<>();
        sliderAdapter = new ImageSliderAdapter(requireContext(), sliderImages);
        viewPagerSlider.setAdapter(sliderAdapter);

        fetchSliderImages(); // Fetch images from API

        setupAutoSlider(); // Enable auto-scroll

        return view;
    }

    /** Fetch images from API */
    private void fetchSliderImages() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url.getSlider, null,
                response -> {
                    try {
                        JSONArray imagesArray = response.getJSONArray("getImage");
                        sliderImages.clear();

                        for (int i = 0; i < imagesArray.length(); i++) {
                            JSONObject obj = imagesArray.getJSONObject(i);
                            String imageName = obj.getString("image");
                            sliderImages.add(url.imageaddress + imageName);
                        }

                        sliderAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Parsing error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "API error", Toast.LENGTH_SHORT).show()
        );

        queue.add(jsonRequest);
    }

    /** Setup auto-scroll for ViewPager2 */
    private void setupAutoSlider() {
        sliderRunnable = new Runnable() {
            @Override
            public void run() {
                if (viewPagerSlider.getAdapter() != null) {
                    int currentItem = viewPagerSlider.getCurrentItem();
                    int total = viewPagerSlider.getAdapter().getItemCount();
                    int nextItem = (currentItem + 1) % total;
                    viewPagerSlider.setCurrentItem(nextItem, true);
                    sliderHandler.postDelayed(this, 3000); // 3-second interval
                }
            }
        };
        sliderHandler.postDelayed(sliderRunnable, 3000);

        // Optional: reset timer when user manually scrolls
        viewPagerSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        sliderHandler.removeCallbacks(sliderRunnable); // Prevent memory leaks
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.topmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.tAppoinment) {
            Toast.makeText(getContext(), "Appointments clicked", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(), AppointmentsActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.tFavourit) {
            Toast.makeText(getContext(), "Favourite clicked", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(), FavoriteActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.tHistory) {
            Toast.makeText(getContext(), "History clicked", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(), Histroy.class);
            startActivity(i);
            return true;
        }else if (id == R.id.tTry) {
            Toast.makeText(getContext(), "Try Hair Cut clicked", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(), MainActivity.class);
            startActivity(i);
            return true;
        }else if (id == R.id.tTry1) {
            Toast.makeText(getContext(), "Try Hair Cut clicked", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(), MainActivity2.class);
            startActivity(i);
            return true;
        } else if (id == R.id.tReport) {
            Toast.makeText(getContext(), "Report Issue clicked", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(), ReportActivity.class);

            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void getAllCtegoryofProduct() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url.getCategory,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("HomeCategoryResponse", "Response: " + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("getHomecategory");
                            pojoHomeCategories.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String strid = jsonObject1.getString("id");
                                String strcategoryimage = jsonObject1.getString("categoryImage");
                                String strcategoryname = jsonObject1.getString("categoryName");

                                Log.d("HomeCategoryItem", "ID: " + strid + ", Name: " + strcategoryname + ", Image: " + strcategoryimage);
                                pojoHomeCategories.add(new PojoHomeCategory(strid, strcategoryname, strcategoryimage));
                            }


                            adapterHomePageTopProduct = new AdapterHomePageTopProduct(pojoHomeCategories, getActivity());
                            rvcategory.setAdapter(adapterHomePageTopProduct);

                        } catch (JSONException e) {
                            Log.e("HomeCategoryParseError", "JSON error: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("HomeCategoryError", "Volley error: " + error.toString());
                error.printStackTrace();
            }
        });

        requestQueue.add(stringRequest);
    }

    public void fetchShopData() {


        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url.getShop, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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

                            ShopAdapter adapter = new ShopAdapter(shopList, getContext());
                            rvshop.setAdapter(adapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);
    }

}
