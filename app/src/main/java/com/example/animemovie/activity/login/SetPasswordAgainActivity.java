package com.example.animemovie.activity.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.animemovie.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SetPasswordAgainActivity extends AppCompatActivity {
    EditText edNewPassword, edConfirmPassword;
    Button btChangePassword;
    TextView showError;
    String phoneNumber,getNameAccount;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password_again);
        initUI();
        getPhone();
        setOnClick();

    }
    private void initUI (){
        edNewPassword = findViewById(R.id.ed_new_password);
        edConfirmPassword =  findViewById(R.id.ed_confirm_password);
        btChangePassword = findViewById(R.id.bt_change_password);
        showError = findViewById(R.id.tv_show_error_password);
        progressDialog = new ProgressDialog(this);
    }

    private void setOnClick (){
        btChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPassword();
            }
        });
    }

    private void checkPassword() {
        String strNewPassword = edNewPassword.getText().toString().trim();
        String strConfirmPassword = edConfirmPassword.getText().toString().trim();

        if (strNewPassword.isEmpty() || strConfirmPassword.isEmpty()){
            showError.setText("Invalid");
        }else if (strNewPassword.length() < 6 ){
            showError.setText("Too short, at least 6 characters");
        }else if (!strConfirmPassword.equals(strNewPassword)){
            showError.setText("Confirm password doesn't match");
        }else {
            progressDialog.show();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference("User_Account");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    getNameAccount = snapshot.child("Phone_Number_Register").child(phoneNumber)
                            .getValue(String.class);
                    databaseReference.child("Information_User").child(getNameAccount).child("0")
                            .child("password").setValue(strConfirmPassword);
                    goToSignInActivity();
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            showError.setText("");
        }
    }
    private String getPhone (){
        Bundle getPhone = getIntent().getExtras();
        if (getPhone != null){
            phoneNumber = getPhone.getString("key_phone");
        }
        return phoneNumber;
    }
    private void goToSignInActivity(){
        startActivity(new Intent(this,SignInActivity.class));
        finish();
    }

}