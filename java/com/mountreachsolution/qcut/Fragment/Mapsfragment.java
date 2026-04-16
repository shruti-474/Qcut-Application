package com.mountreachsolution.qcut.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.mountreachsolution.qcut.R;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONArray;
import org.json.JSONObject;

public class Mapsfragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LatLng userLatLng;
    private Polyline currentPolyline;
    private Marker userMarker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_mapsfragment, container, false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        ImageView btnChangeMapType = view.findViewById(R.id.btnChangeMapType);
        btnChangeMapType.setOnClickListener(v -> showMapTypeDialog());

        return view;
    }

    private void showMapTypeDialog() {
        if (mMap == null) return;
        final String[] mapTypes = {"Normal", "Satellite", "Terrain", "Hybrid"};
        new AlertDialog.Builder(requireContext())
                .setTitle("Select Map Type")
                .setItems(mapTypes, (dialog, which) -> {
                    switch (which) {
                        case 0: mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); break;
                        case 1: mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); break;
                        case 2: mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN); break;
                        case 3: mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); break;
                    }
                }).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        checkLocationPermission();
        fetchShopData();
    }

    private void fetchShopData() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url.getShop, null,
                response -> {
                    try {
                        JSONArray array = response.getJSONArray("getShop");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject shop = array.getJSONObject(i);
                            String name = shop.getString("name");

                            // ⚠️ check backend key names
                            double lat = Double.parseDouble(shop.getString("lattitude"));
                            double lng = Double.parseDouble(shop.getString("lobgitude"));

                            LatLng shopLocation = new LatLng(lat, lng);

                            mMap.addMarker(new MarkerOptions()
                                    .position(shopLocation)
                                    .title(name)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                        }

                        mMap.setOnMarkerClickListener(marker -> {
                            marker.showInfoWindow();
                            if (userLatLng != null) {
                                drawRoute(userLatLng, marker.getPosition());
                            } else {
                                Toast.makeText(requireContext(), "User location not available", Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error parsing shop data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(requireContext(), "Failed to fetch shops", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(requireContext()).add(request);
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            checkLocationEnabled();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void checkLocationEnabled() {
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(android.content.Context.LOCATION_SERVICE);

        boolean gpsEnabled = locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkEnabled = locationManager != null && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gpsEnabled && !networkEnabled) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Enable Location")
                    .setMessage("Location is required to show your current position. Please enable GPS.")
                    .setPositiveButton("Settings", (dialog, which) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            getUserLocation();
        }
    }

    private void getUserLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            if (location != null && mMap != null) {
                showUserMarker(location);
            } else {
                requestNewLocation();
            }
        });
    }

    private void requestNewLocation() {
        com.google.android.gms.location.LocationRequest locationRequest =
                com.google.android.gms.location.LocationRequest.create()
                        .setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setInterval(5000)
                        .setFastestInterval(2000)
                        .setNumUpdates(1);

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, new com.google.android.gms.location.LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                fusedLocationClient.removeLocationUpdates(this);
                if (!locationResult.getLocations().isEmpty()) {
                    Location location = locationResult.getLastLocation();
                    if (location != null && mMap != null) {
                        showUserMarker(location);
                    }
                } else {
                    Toast.makeText(requireContext(), "Still unable to fetch location", Toast.LENGTH_SHORT).show();
                }
            }
        }, requireActivity().getMainLooper());
    }

    private void showUserMarker(Location location) {
        userLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (userMarker != null) userMarker.remove();

        Bitmap bitmap = getBitmapFromVector(R.drawable.mapuser);

        MarkerOptions markerOptions = new MarkerOptions().position(userLatLng).title("You are here");
        if (bitmap != null) markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
        userMarker = mMap.addMarker(markerOptions);

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14f));
    }

    private Bitmap getBitmapFromVector(int drawableId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(requireContext(), drawableId);
        if (vectorDrawable == null) return null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    private void drawRoute(LatLng origin, LatLng destination) {
        if (mMap == null) return;
        if (currentPolyline != null) currentPolyline.remove();

        String url = getDirectionsUrl(origin, destination);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray routes = response.getJSONArray("routes");
                        if (routes.length() == 0) {
                            Toast.makeText(requireContext(), "No route found", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONObject route = routes.getJSONObject(0);
                        JSONArray legs = route.getJSONArray("legs");

                        PolylineOptions polylineOptions = new PolylineOptions()
                                .width(12f)
                                .color(ContextCompat.getColor(requireContext(), R.color.blue))
                                .geodesic(true);

                        // ✅ Loop through legs and steps for accurate road path
                        for (int i = 0; i < legs.length(); i++) {
                            JSONArray steps = legs.getJSONObject(i).getJSONArray("steps");
                            for (int j = 0; j < steps.length(); j++) {
                                String polyline = steps.getJSONObject(j)
                                        .getJSONObject("polyline")
                                        .getString("points");
                                polylineOptions.addAll(PolyUtil.decode(polyline));
                            }
                        }

                        currentPolyline = mMap.addPolyline(polylineOptions);

                        // Zoom camera to fit the full route
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (LatLng point : polylineOptions.getPoints()) {
                            builder.include(point);
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error parsing route", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(requireContext(), "Failed to get route", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                });

        Volley.newRequestQueue(requireContext()).add(request);
    }


    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=driving";
        String key = "key=YOUR_REAL_API_KEY"; // ✅ FIXED
        return "https://maps.googleapis.com/maps/api/directions/json?"
                + str_origin + "&" + str_dest + "&" + mode + "&" + key;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationEnabled();
            } else {
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
