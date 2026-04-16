package com.mountreachsolution.qcut;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReportActivity extends AppCompatActivity {
    EditText etIssue;
    Button btnSend;
    String userEmail;
    ImageView bck;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report2);
        etIssue = findViewById(R.id.etIssue);
        btnSend = findViewById(R.id.btnSend);
        bck = findViewById(R.id.ivBack);
        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReportActivity.this,HomeActvity.class);
                startActivity(i);
                finish();
            }
        });

        // ✅ Get userId from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        userEmail = prefs.getString("email", ""); // second parameter is the default value if not found
        btnSend.setOnClickListener(v -> {
            String issueText = etIssue.getText().toString().trim();

            if (issueText.isEmpty()) {
                Toast.makeText(this, "Please enter your issue", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Create Email Intent
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"parthdahaput@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Report Issue");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "User ID: " + userEmail + "\n\nIssue: " + issueText);

            try {
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            } catch (Exception e) {
                Toast.makeText(this, "No email app found.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}