package com.example.doan.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan.MainActivity;
import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtName, edtPhone, edtEmail, edtPassword;
    private Button btnSignUp;
    private ImageView imgTogglePassword;
    private Toolbar signupToolbar;
    private boolean isPasswordVisible = false;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupToolbar();
        setListeners();
    }

    private void initViews() {
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        signupToolbar = findViewById(R.id.signupToolbar);
        imgTogglePassword = findViewById(R.id.iconTogglePassword);

    }

    private void setupToolbar() {
        setSupportActionBar(signupToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút back
            getSupportActionBar().setHomeButtonEnabled(true); // Kích hoạt nút back
            actionBar.setTitle("Sign Up");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void setListeners() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    String email = edtEmail.getText().toString().trim();
                    String password = edtPassword.getText().toString().trim();
                    registerWithFirebase(email, password);
                }
            }
        });

        imgTogglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                togglePasswordVisibility();
            }
        });
    }

    private boolean validateInput() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (name.isEmpty()) {
            edtName.setError("Vui lòng nhập tên");
            edtName.requestFocus();
            return false;
        }

        if (phone.isEmpty()) {
            edtPhone.setError("Vui lòng nhập số điện thoại");
            edtPhone.requestFocus();
            return false;
        }

        // Kiểm tra định dạng số điện thoại Việt Nam
        if (!phone.matches("^(03|05|07|08|09)\\d{8}$")) {
            edtPhone.setError("Số điện thoại không hợp lệ");
            edtPhone.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            edtEmail.setError("Vui lòng nhập email");
            edtEmail.requestFocus();
            return false;
        }

        // Kiểm tra định dạng email hợp lệ
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            edtPassword.setError("Vui lòng nhập mật khẩu");
            edtPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            edtPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            edtPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imgTogglePassword.setImageResource(R.drawable.ic_eye_closed);
        } else {
            edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            imgTogglePassword.setImageResource(R.drawable.ic_eye_open);
        }
        edtPassword.setSelection(edtPassword.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

    private void registerWithFirebase(String email, String password) {
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Lấy UID của người dùng mới
                        String userId = auth.getCurrentUser().getUid();

                        // Lấy thông tin từ các ô input
                        String name = edtName.getText().toString().trim();
                        String phone = edtPhone.getText().toString().trim();

                        // Tạo dữ liệu để lưu vào Firestore
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("name", name);
                        userData.put("email", email);
                        userData.put("phone", phone);
                        userData.put("created_at", FieldValue.serverTimestamp());

                        // Ghi vào Firestore (ví dụ collection là "users")
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("users").document(userId)
                                .set(userData)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                    // Điều hướng sang com.example.doan.MainActivity
                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FIRESTORE", "Lỗi khi ghi dữ liệu", e);
                                    Toast.makeText(this, "Không thể lưu thông tin người dùng", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        String errorMessage;
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            errorMessage = "Tài khoản đã tồn tại.";
                        } else {
                            Log.e("FIREBASE_AUTH", "Error: ", task.getException());
                            errorMessage = "Đăng ký thất bại: " + task.getException().getMessage();
                        }

                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }


}