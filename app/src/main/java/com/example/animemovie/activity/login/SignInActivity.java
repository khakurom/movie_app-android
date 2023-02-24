package com.example.animemovie.activity.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.animemovie.R;
import com.example.animemovie.activity.splash.SplashMainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {
    Button btLogin;
    EditText edNameAccount, edPassword;
    TextView tvShowError,tvForgotPassword;
    LinearLayout backgroundSignIn;
    ProgressDialog progressDialog;

//    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(
//            "https://movie-app-d199f-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initUI();
        setOnClick();

    }

    private void initUI() {
        btLogin = findViewById(R.id.bt_login);
        edNameAccount = findViewById(R.id.ed_name_account_login);
        edPassword = findViewById(R.id.ed_password_login);
        tvShowError = findViewById(R.id.tv_show_error);
        backgroundSignIn = findViewById(R.id.background_sign_in);
        progressDialog = new ProgressDialog(this);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);

    }

    private void setOnClick() {
        // login
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInformationAccount();
            }
        });
        // hide keyboard
        backgroundSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
        // click forgot password
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickForgotPassword();
            }
        });
    }

    private void checkInformationAccount() {
        String strNameAccount = edNameAccount.getText().toString().trim();
        String strPassword = edPassword.getText().toString().trim();
        progressDialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("User_Account");

        if (strNameAccount.isEmpty() || strPassword.isEmpty()) {
            progressDialog.dismiss();
            tvShowError.setText("Name account or password are empty");
        } else {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.child("Information_User").hasChild(strNameAccount)) {
                        String getPassword = snapshot.child("Information_User").child(strNameAccount)
                                .child("0").child("password").getValue(String.class);
                        if (getPassword.equals(strPassword)) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(SignInActivity.this, SplashMainActivity.class);
                            intent.putExtra("key_name_account",strNameAccount);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            progressDialog.dismiss();
                            tvShowError.setText("Wrong password");
                        }


                    } else {
                        progressDialog.dismiss();
                        tvShowError.setText("Account isn't existed");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    // forget password
    private void clickForgotPassword() {
        startActivity(new Intent(SignInActivity.this,ForgotPasswordActivity.class));

    }
    // hide keyboard when touch outside it
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



}