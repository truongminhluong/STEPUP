package com.example.doan;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AccountSettingsActivity extends AppCompatActivity {

    private Switch switchFaceID, switchPush, switchLocation, switchDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        // Back button
        ImageButton btnBack = findViewById(R.id.btnBack);


        switchFaceID = findViewById(R.id.switchFaceID);
        switchPush = findViewById(R.id.switchPush);
        switchLocation = findViewById(R.id.switchLocation);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());


        // Face ID toggle
        switchFaceID.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this, isChecked ? "Face ID enabled" : "Face ID disabled", Toast.LENGTH_SHORT).show();
        });

        // Push notifications toggle
        switchPush.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this, isChecked ? "Push notifications ON" : "Push notifications OFF", Toast.LENGTH_SHORT).show();
        });

        // Location services toggle
        switchLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this, isChecked ? "Location services ON" : "Location services OFF", Toast.LENGTH_SHORT).show();
        });

        // Dark mode toggle
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this, isChecked ? "Dark mode ON" : "Dark mode OFF", Toast.LENGTH_SHORT).show();
            // Optional: change theme dynamically
        });
    }
}
