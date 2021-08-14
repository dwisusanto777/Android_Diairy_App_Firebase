package com.example.course2dairyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.course2dairyapp.dashboard.Dashboard;
import com.example.course2dairyapp.databinding.ActivitySplashBinding;

public class Splash extends AppCompatActivity {

    ActivitySplashBinding binding;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        preferences = getSharedPreferences(DataAuth.SHARED_PREFERENCE, MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(preferences.getBoolean(DataAuth.AUTO_LOGIN, false)){
                    startActivity(new Intent(Splash.this, Dashboard.class));
                }else{
                    startActivity(new Intent(Splash.this, Signin.class));
                }
                finish();
            }
        }, 5000);
    }
}
