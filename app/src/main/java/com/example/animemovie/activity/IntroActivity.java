package com.example.animemovie.activity;

import static com.example.animemovie.R.drawable.app_store_google_play;
import static com.example.animemovie.R.drawable.free;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.animemovie.R;
import com.example.animemovie.activity.login.SignInActivity;
import com.example.animemovie.activity.login.SignUpActivity;
import com.example.animemovie.adapter.AdapterViewPager;
import com.example.animemovie.model.modelIntro;
import com.example.animemovie.transformer_viewpager2.ZoomOutPageTransformer;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class IntroActivity extends AppCompatActivity {
    private ViewPager2 mViewPager2;
    private CircleIndicator3 circleIndicator;
    private List<modelIntro> modelIntroList;
    private TextView tvPrivacy,tvSignUp;
    private Button btSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        initUI();
        setAdapter();
        setLicense();
        setSignUp();
        setSignIn();

    }
    private void initUI() {
        mViewPager2 = findViewById(R.id.id_viewPager);
        circleIndicator = findViewById(R.id.id_CircleIndicator);
        tvPrivacy = findViewById(R.id.tv_privacy);
        tvSignUp = findViewById(R.id.tv_sign_up);
        btSignIn = findViewById(R.id.bt_sign_in);
    }
    private void setAdapter() {
        AdapterViewPager adapter = new AdapterViewPager(IntroActivity.this, setDataModelIntro());
        mViewPager2.setAdapter(adapter);
        mViewPager2.setPageTransformer(new ZoomOutPageTransformer());
        circleIndicator.setViewPager(mViewPager2);
    }

    private void setLicense() {
        tvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPrivacy();
            }
        });
    }


    private void openPrivacy() {
        View viewDialog = getLayoutInflater().inflate(R.layout.bottom_sheet_help_intro, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.show();
        bottomSheetDialog.setCancelable(true); // set false neu muon user phai click cancel de thoat
        // hien thi toan bo bottom sheet:
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) viewDialog.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

    }
    private void setSignUp (){
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUp();
            }
        });
    }

    private void openSignUp() {
        Intent intent = new Intent(IntroActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
    private void setSignIn (){
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignIn();
            }
        });
    }
    private void openSignIn() {
        Intent intent = new Intent(IntroActivity.this, SignInActivity.class);
        startActivity(intent);
    }


    private List<modelIntro> setDataModelIntro() {
        modelIntroList = new ArrayList<>();
        modelIntroList.add(new modelIntro("Available on google play and appstore ", app_store_google_play));
        modelIntroList.add(new modelIntro("All free", free));
        modelIntroList.add(new modelIntro("Constantly update ", R.drawable.update));
        return modelIntroList;
    }


}