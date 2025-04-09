package com.example.doan;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditProfileActivity extends AppCompatActivity {

    private ImageButton backButton, saveButton;
    private EditText fullNameEditText, emailEditText, passwordEditText;
    private TextView userNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Khởi tạo các view
        backButton = findViewById(R.id.back_button);
        saveButton = findViewById(R.id.save_button); // Nút lưu thông tin
        fullNameEditText = findViewById(R.id.full_name_edittext);
        emailEditText = findViewById(R.id.email_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        userNameTextView = findViewById(R.id.user_name);

        // Thiết lập hành động cho nút quay lại
        backButton.setOnClickListener(v -> {
            finish(); // Quay lại Activity trước đó
        });

        // Thiết lập hành động cho nút lưu thông tin (Dấu tích)
        saveButton.setOnClickListener(v -> {
            // Lấy dữ liệu từ các EditText
            String fullName = fullNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Kiểm tra dữ liệu hợp lệ (có thể thêm kiểm tra chuỗi rỗng hoặc sai định dạng)
            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Thực hiện lưu thông tin (hoặc gửi dữ liệu qua API, lưu vào cơ sở dữ liệu)
            // Ví dụ: Hiển thị thông báo khi lưu thành công
            Toast.makeText(this, "Thông tin đã được lưu!", Toast.LENGTH_SHORT).show();

            // Nếu muốn lưu vào SharedPreferences, hoặc gửi qua API, bạn có thể làm ở đây
            // Ví dụ với SharedPreferences:
            // SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
            // SharedPreferences.Editor editor = sharedPreferences.edit();
            // editor.putString("full_name", fullName);
            // editor.putString("email", email);
            // editor.putString("password", password);
            // editor.apply();
        });
    }
}