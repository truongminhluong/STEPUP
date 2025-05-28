package com.example.doan.doan.Screens;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan.R;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddressActivity extends AppCompatActivity {

    private Toolbar addressToolbar;
    private AutoCompleteTextView edtAddress;
    private Button btnConfirm;

    private PlacesClient placesClient;
    private ArrayAdapter<String> addressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_address);

        // Điều chỉnh layout để lấp vùng status bar nếu có
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo và cấu hình Google Places API
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBKSIF64u1FBEjpI0HxVSvc3BvYChbe0nE", Locale.getDefault());
        }
        placesClient = Places.createClient(this);

        // Khởi tạo các view và gắn listener
        initViews();
        setupToolbar();
        setListeners();
    }

    private void initViews() {
        addressToolbar = findViewById(R.id.addressToolbar);
        edtAddress = findViewById(R.id.edtAddress);
        btnConfirm = findViewById(R.id.btnConfirm);

        // Adapter để hiển thị gợi ý địa chỉ từ Google
        addressAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        edtAddress.setAdapter(addressAdapter);
        edtAddress.setThreshold(1); // Bắt đầu gợi ý sau 1 ký tự
    }

    private void setupToolbar() {
        setSupportActionBar(addressToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Nút back
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("Địa chỉ giao hàng");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void setListeners() {
        // Theo dõi sự thay đổi của người dùng trong AutoCompleteTextView
        edtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                        .setTypeFilter(TypeFilter.ADDRESS) // Chỉ gợi ý địa chỉ
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();

                // Gọi API Google Places để lấy gợi ý
                placesClient.findAutocompletePredictions(request)
                        .addOnSuccessListener(response -> {
                            List<String> suggestions = new ArrayList<>();
                            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                                suggestions.add(prediction.getFullText(null).toString());
                            }
                            addressAdapter.clear();
                            addressAdapter.addAll(suggestions);
                            addressAdapter.notifyDataSetChanged();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("PlacesAPI", "Lỗi khi lấy gợi ý địa chỉ", e);
                            Toast.makeText(AddressActivity.this, "Lỗi khi gợi ý địa chỉ: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Xử lý khi nhấn nút "Tiếp theo"
        btnConfirm.setOnClickListener(v -> {
            String inputAddress = edtAddress.getText().toString();

            Geocoder geocoder = new Geocoder(this);
            try {
                List<Address> addresses = geocoder.getFromLocationName(inputAddress, 1);
                if (!addresses.isEmpty()) {
                    Address addr = addresses.get(0);

                    // Gửi kết quả lại Activity trước đó
                    Intent result = new Intent();
                    result.putExtra("lat", addr.getLatitude());
                    result.putExtra("lng", addr.getLongitude());
                    result.putExtra("address", addr.getAddressLine(0));
                    setResult(RESULT_OK, result);
                    finish();
                } else {
                    Toast.makeText(this, "Không tìm thấy địa chỉ", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi khi tìm địa chỉ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
