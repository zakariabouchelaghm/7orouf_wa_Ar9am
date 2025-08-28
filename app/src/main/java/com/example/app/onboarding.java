package com.example.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class onboarding extends AppCompatActivity {
    private static final int SPLASH_DURATION = 2000; // 3 seconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!NetworkUtils.isConnected(this)) {
            Toast.makeText(this, "لا يوجد اتصال بالإنترنيت", Toast.LENGTH_LONG).show();
            finish(); // Close app immediately
            return;
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_onboarding);
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        prefs.edit().putBoolean("isFirstTime", false).apply();

        // Delay for SPLASH_DURATION then open MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(onboarding.this, MainActivity.class));
                overridePendingTransition(0, 3);
                finish(); // so user can't go back to onboarding
            }
        }, SPLASH_DURATION);
    }
}