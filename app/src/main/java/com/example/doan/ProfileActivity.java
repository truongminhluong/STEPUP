package com.example.doan;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    EditText etFullName, etEmail, etPassword;
    TextView tvName;
    ImageButton btnBack, btnEdit;

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

        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Nút chỉnh sửa (demo Toast, bạn có thể mở EditText để cho chỉnh sửa ở đây)
        btnEdit.setOnClickListener(v -> {
            Toast.makeText(this, "Chỉnh sửa thông tin", Toast.LENGTH_SHORT).show();
            etFullName.setEnabled(true);
            etEmail.setEnabled(true);
            etPassword.setEnabled(true);
        });
    }
}
