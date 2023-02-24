package com.example.animemovie.activity.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.animemovie.R;
import com.example.animemovie.activity.IntroActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToIntroActivity();
            }
        },3000);
    }

    private void goToIntroActivity() {
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
        finish();
    }
}