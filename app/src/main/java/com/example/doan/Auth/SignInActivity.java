package com.example.doan.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.MainActivity;
import com.example.doan.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001; // Mã yêu cầu cho sign-in Google
    private GoogleSignInClient mGoogleSignInClient;  // Đối tượng GoogleSignInClient
    private FirebaseAuth mAuth;  // FirebaseAuth instance
    private Button btnGoogleSignIn;  // Nút đăng nhập với Google

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Cấu hình Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))  // Lấy ID Client từ Firebase
                .requestEmail()  // Yêu cầu email
                .build();

        // Tạo GoogleSignInClient
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Khởi tạo nút đăng nhập bằng Google
        btnGoogleSignIn = findViewById(R.id.btnSignInGG);
        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();  // Gọi phương thức đăng nhập với Google
            }
        });
    }

    // Bắt đầu quy trình đăng nhập với Google
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Xử lý kết quả trả về từ Google Sign-In
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Nếu kết quả đến từ đăng nhập Google
        if (requestCode == RC_SIGN_IN) {
            // Lấy tài khoản từ Intent và thực hiện đăng nhập Firebase
            GoogleSignIn.getSignedInAccountFromIntent(data)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Nếu đăng nhập thành công, lấy tài khoản Google
                            GoogleSignInAccount account = task.getResult();
                            firebaseAuthWithGoogle(account);  // Đăng nhập vào Firebase
                        } else {
                            // Nếu thất bại, log lỗi
                            Log.w("SignInActivity", "Google sign in failed", task.getException());
                            Toast.makeText(SignInActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // Đăng nhập vào Firebase bằng Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        // Lấy thông tin tài khoản Google
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập thành công, chuyển hướng đến MainActivity
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // Nếu đăng nhập thất bại
                        Log.w("SignInActivity", "Authentication failed", task.getException());
                        Toast.makeText(SignInActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Cập nhật UI sau khi đăng nhập thành công
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Điều hướng người dùng đến MainActivity
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    // Đăng xuất khỏi Firebase và Google
    private void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> updateUI(null));
    }
}
