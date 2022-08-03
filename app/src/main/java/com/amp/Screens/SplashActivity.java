package com.amp.Screens;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.amp.databinding.ActivitySplashScreenBinding;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashScreenBinding binding ;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("name", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Log.e("SplashActivity", "name: ----------------------->"+sharedPreferences.getString("name", ""));
        Log.e("SplashActivity", "data: ----------------------->"+sharedPreferences.contains("name"));
        Log.e("SplashActivity", "data: ----------------------->"+sharedPreferences.contains("data"));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPreferences.contains("name") && sharedPreferences.contains("data"))
                {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },3000);
    }
}