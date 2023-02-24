package com.example.animemovie.activity.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.animemovie.R;
import com.example.animemovie.activity.MainActivity;

public class SplashMainActivity extends AppCompatActivity {
    String strNameAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_main);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                goToIntroActivity();
            }
        }, 3000);
    }

    private void goToIntroActivity() {
        Bundle getNameAccount = getIntent().getExtras();
        if (getNameAccount != null) {
            strNameAccount = getNameAccount.getString("key_name_account");
        }
//        openFragmentHome();
//        Bundle bundle = new Bundle();
//        bundle.putString("key_name_account", strNameAccount);
//        fragment_main_home fragment_main_home = new fragment_main_home();
//        fragment_main_home.setArguments(bundle);


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("key_name_account", strNameAccount);
        startActivity(intent);
        finish();
    }




}