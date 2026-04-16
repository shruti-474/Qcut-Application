package com.mountreachsolution.qcut;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mountreachsolution.qcut.common.url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class BookAppoinment extends AppCompatActivity {

    TextInputEditText etFullName, etEmail, etDate, etTime;
    ChipGroup servicesChipGroup;
    AppCompatButton btnBook;

    String selectedDate = "", selectedTime = "";

    // shop
    String shopName, shopAddress, shopTime, shopDay, shopService, shopImage, shopType, shopemail, shopStatus, shopSets;

    // user
    String userId, userName, userMobileNo, userEmail, userAddress, userImage, userRole;

    CardView[] cards = new CardView[10];
    TextView[] tvCards = new TextView[10];

    int selectedSetIndex = -1;
    String SelectedSet = "";   // ✅ Store selected set as string

    List<String> selectedServicesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appoinment);

        // --- Shop data ---
        Intent intent = getIntent();
        if (intent != null) {
            shopName = intent.getStringExtra("shopname");
            shopAddress = intent.getStringExtra("address");
            shopTime = intent.getStringExtra("time");
            shopDay = intent.getStringExtra("day");
            shopService = intent.getStringExtra("service");
            shopImage = intent.getStringExtra("image");
            shopType = intent.getStringExtra("type");
            shopemail = intent.getStringExtra("email");
            shopStatus = intent.getStringExtra("status");
            shopSets = intent.getStringExtra("sets");
        }

        // --- User data ---
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        userId = prefs.getString("id", "");
        userName = prefs.getString("name", "");
        userMobileNo = prefs.getString("mobileno", "");
        userEmail = prefs.getString("email", "");
        userAddress = prefs.getString("address", "");
        userImage = prefs.getString("image", "");
        userRole = prefs.getString("userrole", "");

        // --- Views ---
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        servicesChipGroup = findViewById(R.id.servicesChipGroup);
        btnBook = findViewById(R.id.btnBook);

        // Initialize CardViews
        cards[0] = findViewById(R.id.card1);
        cards[1] = findViewById(R.id.card2);
        cards[2] = findViewById(R.id.card3);
        cards[3] = findViewById(R.id.card4);
        cards[4] = findViewById(R.id.card5);
        cards[5] = findViewById(R.id.card6);
        cards[6] = findViewById(R.id.card7);
        cards[7] = findViewById(R.id.card8);
        cards[8] = findViewById(R.id.card9);
        cards[9] = findViewById(R.id.card10);

        // Initialize TextViews
        tvCards[0] = findViewById(R.id.tvCard1);
        tvCards[1] = findViewById(R.id.tvCard2);
        tvCards[2] = findViewById(R.id.tvCard3);
        tvCards[3] = findViewById(R.id.tvCard4);
        tvCards[4] = findViewById(R.id.tvCard5);
        tvCards[5] = findViewById(R.id.tvCard6);
        tvCards[6] = findViewById(R.id.tvCard7);
        tvCards[7] = findViewById(R.id.tvCard8);
        tvCards[8] = findViewById(R.id.tvCard9);
        tvCards[9] = findViewById(R.id.tvCard10);

        // Example: shop has N sets
        int totalSets = Integer.parseInt(shopSets);

        for (int i = 0; i < cards.length; i++) {
            if (i < totalSets) {
                cards[i].setCardBackgroundColor(Color.parseColor("#4CAF50"));
                tvCards[i].setText("Set " + (i + 1));
                tvCards[i].setTextColor(Color.WHITE);
            } else {
                cards[i].setCardBackgroundColor(Color.parseColor("#F44336"));
                tvCards[i].setText("Not Available");
                tvCards[i].setTextColor(Color.WHITE);
            }
        }

        fetchSets(shopName);

        // --- Card click logic ---
        for (int i = 0; i < cards.length; i++) {
            final int index = i;
            cards[i].setOnClickListener(v -> {
                int bgColor = cards[index].getCardBackgroundColor().getDefaultColor();

                if (bgColor == Color.parseColor("#4CAF50")) {
                    if (selectedSetIndex != -1) {
                        cards[selectedSetIndex].setCardBackgroundColor(Color.parseColor("#4CAF50"));
                        tvCards[selectedSetIndex].setText("Set " + (selectedSetIndex + 1));
                    }

                    cards[index].setCardBackgroundColor(Color.parseColor("#E91E63"));
                    tvCards[index].setText("Selected Set " + (index + 1));
                    selectedSetIndex = index;

                    // ✅ Save selected set string
                    SelectedSet = "Set " + (index + 1);

                } else if (bgColor == Color.parseColor("#F44336")) {
                    Toast.makeText(BookAppoinment.this, "Set " + (index + 1) + " is not available", Toast.LENGTH_SHORT).show();

                } else if (bgColor == Color.parseColor("#E91E63")) {
                    cards[index].setCardBackgroundColor(Color.parseColor("#4CAF50"));
                    tvCards[index].setText("Set " + (index + 1));
                    selectedSetIndex = -1;

                    // ✅ Reset string
                    SelectedSet = "";
                }
            });
        }

        etFullName.setText(userName);
        etEmail.setText(userEmail);

        // Date picker
        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(
                    BookAppoinment.this,
                    (view, year, month, dayOfMonth) -> {
                        selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        etDate.setText(selectedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePicker.show();
        });

        // Time picker
        etTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(
                    BookAppoinment.this,
                    (view, hourOfDay, minute1) -> {
                        int hourSelected = hourOfDay % 12;
                        if (hourSelected == 0) hourSelected = 12;
                        String amPm = (hourOfDay < 12) ? "AM" : "PM";

                        selectedTime = String.format("%02d:%02d %s", hourSelected, minute1, amPm);
                        etTime.setText(selectedTime);
                    },
                    hour, minute, false
            );
            timePicker.show();
        });

        // Services chips
        if (shopService != null) {
            String[] serviceArray = shopService.split(",");
            for (String s : serviceArray) {
                Chip chip = new Chip(this);
                chip.setText(s);
                chip.setCheckable(true);
                chip.setChipStrokeWidth(2f);
                chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
                chip.setTextColor(getResources().getColor(R.color.pink));
                chip.setChipBackgroundColor(ColorStateList.valueOf(Color.WHITE));

                chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.pink)));
                        chip.setTextColor(Color.WHITE);
                        selectedServicesList.add(chip.getText().toString());
                    } else {
                        chip.setChipBackgroundColor(ColorStateList.valueOf(Color.WHITE));
                        chip.setTextColor(getResources().getColor(R.color.pink));
                        selectedServicesList.remove(chip.getText().toString());
                    }
                });

                servicesChipGroup.addView(chip);
            }
        }

        // Book button
        btnBook.setOnClickListener(v -> {
            String finalName = etFullName.getText().toString().trim();
            String finalEmail = etEmail.getText().toString().trim();
            String finalDate = etDate.getText().toString().trim();
            String finalTime = etTime.getText().toString().trim();
            String finalServices = String.join(", ", selectedServicesList);

            if (finalName.isEmpty() || finalEmail.isEmpty() || finalDate.isEmpty() ||
                    finalTime.isEmpty() || finalServices.isEmpty()) {
                Toast.makeText(BookAppoinment.this, "Please fill all fields and select services", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int selectedMinutes = convertTimeToMinutes(finalTime);
                if (selectedMinutes >= 22 * 60 || selectedMinutes <= 8 * 60) {
                    Toast.makeText(BookAppoinment.this, "Shop is closed at this time", Toast.LENGTH_SHORT).show();
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(BookAppoinment.this, "Invalid time format", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedSetIndex == -1 || SelectedSet.isEmpty()) {
                Toast.makeText(BookAppoinment.this, "Please select a set before booking", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Pass selected set string
            Intent billIntent = new Intent(BookAppoinment.this, BillActivity.class);
            billIntent.putExtra("finalName", finalName);
            billIntent.putExtra("finalEmail", finalEmail);
            billIntent.putExtra("userMobile", userMobileNo);
            billIntent.putExtra("userAddress", userAddress);
            billIntent.putExtra("userImage", userImage);

            billIntent.putExtra("shopName", shopName);
            billIntent.putExtra("shopAddress", shopAddress);
            billIntent.putExtra("shopTime", shopTime);
            billIntent.putExtra("shopDay", shopDay);
            billIntent.putExtra("shopService", shopService);
            billIntent.putExtra("shopImage", shopImage);
            billIntent.putExtra("shopType", shopType);
            billIntent.putExtra("shopEmail", shopemail);

            billIntent.putExtra("finalDate", finalDate);
            billIntent.putExtra("finalTime", finalTime);
            billIntent.putExtra("finalServices", finalServices);
            billIntent.putExtra("chosenSet", SelectedSet); // ✅ now string

            startActivity(billIntent);
        });
    }

    private void fetchSets(String shopName) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("shopname", shopName);

        client.post(url.getbooking, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = new String(responseBody);
                    Log.d("API_RESPONSE", response);

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("success")) {
                        JSONArray setsArray = jsonObject.getJSONArray("sets");

                        int totalSets = 10;
                        for (int i = 0; i < totalSets; i++) {
                            cards[i].setCardBackgroundColor(Color.parseColor("#4CAF50"));
                            tvCards[i].setText("Set " + (i + 1));
                            tvCards[i].setTextColor(Color.WHITE);
                        }

                        for (int j = 0; j < setsArray.length(); j++) {
                            int bookedSet = Integer.parseInt(setsArray.getString(j));
                            int index = bookedSet - 1;
                            if (index >= 0 && index < totalSets) {
                                cards[index].setCardBackgroundColor(Color.parseColor("#F44336"));
                                tvCards[index].setText("Not Available");
                                tvCards[index].setTextColor(Color.WHITE);
                            }
                        }
                    } else {
                        Log.e("API_ERROR", "Status failed");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("API_ERROR", "Request failed", error);
            }
        });
    }

    private int convertTimeToMinutes(String time) {
        time = time.trim();
        String[] parts = time.split(" ");
        String[] hm = parts[0].split(":");
        int hour = Integer.parseInt(hm[0]);
        int minute = Integer.parseInt(hm[1]);
        String amPm = parts[1];

        if (amPm.equalsIgnoreCase("PM") && hour != 12) hour += 12;
        if (amPm.equalsIgnoreCase("AM") && hour == 12) hour = 0;

        return hour * 60 + minute;
    }
}
