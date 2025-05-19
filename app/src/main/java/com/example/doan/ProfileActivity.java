package com.example.doan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    EditText etFullName, etEmail, etPassword;
    TextView tvName;
    ImageButton btnBack, btnEdit, btnSave;

    private static final String PREFS_NAME = "UserProfilePrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvName = findViewById(R.id.tvName);
        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        btnSave = findViewById(R.id.btnSave);

        disableEditMode();
        loadUserData();

        btnBack.setOnClickListener(v -> {
            if (btnSave.getVisibility() == View.VISIBLE) {
                saveUserData(); // Lưu nếu đang trong chế độ chỉnh sửa
            }
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        btnEdit.setOnClickListener(v -> {
            enableEditMode();
            Toast.makeText(this, "Bạn có thể chỉnh sửa thông tin", Toast.LENGTH_SHORT).show();
        });

        btnSave.setOnClickListener(v -> {
            if (validateInput()) {
                saveUserData();
                disableEditMode();
                Toast.makeText(this, "Thông tin đã được lưu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void enableEditMode() {
        etFullName.setEnabled(true);
        etEmail.setEnabled(true);
        etPassword.setEnabled(true);
        btnSave.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.GONE);
    }

    private void disableEditMode() {
        etFullName.setEnabled(false);
        etEmail.setEnabled(false);
        etPassword.setEnabled(false);
        btnSave.setVisibility(View.GONE);
        btnEdit.setVisibility(View.VISIBLE);
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(etFullName.getText())) {
            etFullName.setError("Vui lòng nhập họ tên");
            return false;
        }
        if (TextUtils.isEmpty(etEmail.getText())) {
            etEmail.setError("Vui lòng nhập email");
            return false;
        }
        if (TextUtils.isEmpty(etPassword.getText())) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            return false;
        }
        return true;
    }

    private void saveUserData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fullName", etFullName.getText().toString().trim());
        editor.putString("email", etEmail.getText().toString().trim());
        editor.putString("password", etPassword.getText().toString().trim());
        editor.apply();

        tvName.setText(etFullName.getText().toString().trim());
    }

    private void loadUserData() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String fullName = prefs.getString("fullName", "Alisson Becker");
        String email = prefs.getString("email", "alissonbecker@gmail.com");
        String password = prefs.getString("password", "password");

        etFullName.setText(fullName);
        etEmail.setText(email);
        etPassword.setText(password);
        tvName.setText(fullName);
    }
}
