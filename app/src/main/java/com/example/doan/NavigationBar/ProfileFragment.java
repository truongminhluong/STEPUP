package com.example.doan.NavigationBar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doan.R;

public class ProfileFragment extends Fragment {

    EditText etFullName, etEmail, etPassword;
    TextView tvName;
    ImageButton btnBack, btnEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_profile, container, false);
        etFullName = view.findViewById(R.id.etFullName);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        tvName = view.findViewById(R.id.tvName);
        btnBack = view.findViewById(R.id.btnBack);
        btnEdit = view.findViewById(R.id.btnEdit);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnEdit.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Chỉnh sửa thông tin", Toast.LENGTH_SHORT).show();
            etFullName.setEnabled(true);
            etEmail.setEnabled(true);
            etPassword.setEnabled(true);
        });
    }
}