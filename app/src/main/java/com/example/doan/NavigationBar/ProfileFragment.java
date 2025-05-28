package com.example.doan.NavigationBar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    TextView txtNameProfile;
    EditText edtEmailProfile, edtPhoneProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        innitView(view);
        loadData();
        return view;
    }

    private void innitView(View view) {
        txtNameProfile = view.findViewById(R.id.txtNameProfile);
        edtEmailProfile = view.findViewById(R.id.edtEmailProfile);
        edtPhoneProfile = view.findViewById(R.id.edtPhoneProfile);
    }

    private void loadData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String phone = documentSnapshot.getString("phone");
                        String email = documentSnapshot.getString("email");

                        // Hiển thị thông tin người dùng

                        txtNameProfile.setText(name);
                        edtEmailProfile.setText(email);
                        edtPhoneProfile.setText(phone);
                    }
                });
    }

}