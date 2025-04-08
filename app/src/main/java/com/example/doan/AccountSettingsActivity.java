package com.example.doan;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AccountSettingsActivity extends AppCompatActivity {

    Switch switchFaceID, switchPush, switchLocation, switchDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        switchFaceID = findViewById(R.id.switchFaceID);
        switchPush = findViewById(R.id.switchPush);
        switchLocation = findViewById(R.id.switchLocation);
        switchDarkMode = findViewById(R.id.switchDarkMode);

        // Example: Dark mode switch
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Toast.makeText(this, isChecked ? "Dark mode ON" : "Dark mode OFF", Toast.LENGTH_SHORT).show();
        });
    }
}
