package com.example.doan.NavigationBar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doan.EditProfileActivity;
import com.example.doan.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
        private static final int REQUEST_CODE_GALLERY = 100;
        private static final int REQUEST_CODE_CAMERA = 101;

        private CircleImageView profileImageView;
        private ImageView editAvatarImageView;
        private TextView userNameTextView;
        private EditText fullNameEditText;
        private EditText emailEditText;
        private EditText passwordEditText;

        private Uri imageUri; // Lưu trữ URI của ảnh đã chọn

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_profile, container, false);
            setHasOptionsMenu(false);

            // Ẩn Navigation Bar (ActionBar)
            if (getActivity() != null) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
            }

            profileImageView = view.findViewById(R.id.profile_image);
            editAvatarImageView = view.findViewById(R.id.edit_avatar);
            userNameTextView = view.findViewById(R.id.user_name);
            fullNameEditText = view.findViewById(R.id.full_name_edittext);
            emailEditText = view.findViewById(R.id.email_edittext);
            passwordEditText = view.findViewById(R.id.password_edittext);


            // Hiển thị thông tin người dùng (mặc định)
            userNameTextView.setText("Lee Wang Anh");
            fullNameEditText.setText("Lee Wang Anh");
            emailEditText.setText("lqanh@gmail.com");
            passwordEditText.setText("********");

            // Hiển thị ảnh đại diện mặc định (có thể thay bằng URL hoặc từ camera)
            String imageUrl = "https://raw.githubusercontent.com/Lwanh/Blog/main/wanh.jpg";
            Picasso.get().load(imageUrl).into(profileImageView);


            // Sự kiện chỉnh sửa avatar
            editAvatarImageView.setOnClickListener(v -> showImagePickerOptions());

            // Nút quay lại
            ImageButton backButton = view.findViewById(R.id.back_button);
            backButton.setOnClickListener(v -> getActivity().onBackPressed());

            // Nút chỉnh sửa profile
            ImageButton editButton = view.findViewById(R.id.edit_button);
            editButton.setOnClickListener(v -> openEditProfile());

            return view;
        }

        private void showImagePickerOptions() {
            // Tạo Intent chọn ảnh từ gallery hoặc camera
            String[] options = {"Chọn từ Thư viện", "Chụp ảnh"};
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
            builder.setTitle("Thay đổi Avatar")
                    .setItems(options, (dialog, which) -> {
                        if (which == 0) {
                            // Chọn ảnh từ thư viện
                            openGallery();
                        } else {
                            // Chụp ảnh
                            openCamera();
                        }
                    })
                    .show();
        }

        private void openGallery() {
            // Mở gallery để chọn ảnh
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_CODE_GALLERY);
        }

        private void openCamera() {
            // Mở camera để chụp ảnh
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CODE_GALLERY && data != null) {
                    // Chọn ảnh từ gallery
                    imageUri = data.getData();
                    profileImageView.setImageURI(imageUri);
                } else if (requestCode == REQUEST_CODE_CAMERA && data != null) {
                    // Chụp ảnh từ camera
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    profileImageView.setImageBitmap(photo);
                }
            }
        }

        private void openEditProfile() {
            // Mở Activity chỉnh sửa thông tin người dùng
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            intent.putExtra("full_name", fullNameEditText.getText().toString());
            intent.putExtra("email", emailEditText.getText().toString());
            intent.putExtra("password", passwordEditText.getText().toString());
            startActivityForResult(intent, REQUEST_CODE_GALLERY);
        }
}