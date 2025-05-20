package com.example.doan.Splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doan.Adapter.OnboardingAdapter;
import com.example.doan.Auth.SignInActivity;
import com.example.doan.R;

public class OnboardingActivity extends AppCompatActivity {
    ViewPager2 viewPager;
    LinearLayout dotsLayout;
    Button btnNext;
    TextView btnGetStarted;
    int totalPages = 3;
    int currentPage = 0;

    int[] layouts = {R.layout.onboarding_item1, R.layout.onboarding_item2, R.layout.onboarding_item3};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_onboarding);

        viewPager = findViewById(R.id.viewPager);
        dotsLayout = findViewById(R.id.dotsLayout);
        btnNext = findViewById(R.id.btnNext);
        btnGetStarted = findViewById(R.id.btnGetStarted);

        viewPager.setAdapter(new OnboardingAdapter(layouts, this));
        addDots(0);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                addDots(position);
                btnGetStarted.setVisibility(position == totalPages - 1 ? View.VISIBLE : View.GONE);
                btnNext.setVisibility(position == totalPages - 1 ? View.GONE : View.VISIBLE);
            }
        });

        btnNext.setOnClickListener(v -> {
            if (currentPage < totalPages - 1) {
                viewPager.setCurrentItem(currentPage + 1);
            }
        });

        btnGetStarted.setOnClickListener(v -> {
            startActivity(new Intent(OnboardingActivity.this, SignInActivity.class));
            finish();
        });
    }

    private void addDots(int position) {
        dotsLayout.removeAllViews();
        for (int i = 0; i < totalPages; i++) {
            TextView dot = new TextView(this);
            dot.setText(Html.fromHtml("&#8226;"));
            dot.setTextSize(35);
            dot.setTextColor(getResources().getColor(i == position ? R.color.purple_500 : R.color.gray));
            dotsLayout.addView(dot);
        }
    }
}
