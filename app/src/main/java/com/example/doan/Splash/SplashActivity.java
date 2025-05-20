package com.example.doan.Splash;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.doan.R;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = "zzzzzzzzzz";
    TextView[] letters;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_splash);

        // Xoay icon loading
        ImageView loadingIcon = findViewById(R.id.loading_icon);
        Animation rotateAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        loadingIcon.startAnimation(rotateAnimation);

        // Chuyển sang màn onboarding sau 5 giây
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, OnboardingActivity.class);
            startActivity(intent);
            finish();
        }, 5000);

        // Ánh xạ từng chữ cái
        letters = new TextView[]{
                findViewById(R.id.char1),
                findViewById(R.id.char2),
                findViewById(R.id.char3),
                findViewById(R.id.char4),
                findViewById(R.id.char5),
                findViewById(R.id.char6),
                findViewById(R.id.char7),
                findViewById(R.id.char8),
                findViewById(R.id.char9),
                findViewById(R.id.char10)
        };

        startSmoothBounceAnimation();
    }

    private void startSmoothBounceAnimation() {
        for (int i = 0; i < letters.length; i++) {
            final int index = i;
            letters[index].postDelayed(() -> {
                ObjectAnimator up = ObjectAnimator.ofFloat(letters[index], "translationY", 0f, -40f);
                ObjectAnimator down = ObjectAnimator.ofFloat(letters[index], "translationY", -40f, 0f);

                AnimatorSet bounceSet = new AnimatorSet();
                bounceSet.playSequentially(up, down);
                bounceSet.setInterpolator(new OvershootInterpolator());
                bounceSet.setDuration(300);
                bounceSet.start();
            }, i * 100);
        }

        // Lặp lại sau 2 giây
        letters[0].postDelayed(this::startSmoothBounceAnimation, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
