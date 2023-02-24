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
import com.example.animemovie.model.info_user.InfoUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    private EditText edFirstName, edLastName, edEmail, edNameAccount, edPassword, edConfirmPassword,edPhoneNumber;
    private Button btRegister;
    private TextView tvErrorName, tvErrorPhone, tvErrorEmail, tvErrorNameAccount, tvErrorPassword,
            tvErrorConfirmPassword, tvErrorRegister;
    private LinearLayout backgroundSignUp;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private List<InfoUser> infoUserList;
    private String strFirstName, strLastName, strEmail, strNameAccount, strPassword, strConfirmPassword,
            strFullName,strPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initUI();
        setOnClick();

    }

    private void initUI() {
        edFirstName = findViewById(R.id.ed_first_name);
        edLastName = findViewById(R.id.ed_last_name);
        edPhoneNumber = findViewById(R.id.ed_phoneNumber);
        edEmail = findViewById(R.id.ed_email);
        edNameAccount = findViewById(R.id.ed_name_account);
        edPassword = findViewById(R.id.ed_password);
        edConfirmPassword = findViewById(R.id.ed_password_confirm);
        btRegister = findViewById(R.id.bt_register);
        tvErrorName = findViewById(R.id.tv_show_error_name);
        tvErrorPhone = findViewById(R.id.tv_show_error_phone);
        tvErrorEmail = findViewById(R.id.tv_show_error_email);
        tvErrorNameAccount = findViewById(R.id.tv_show_error_name_account);
        tvErrorPassword = findViewById(R.id.tv_show_error_password);
        tvErrorConfirmPassword = findViewById(R.id.tv_show_error_confirm_password);
        tvErrorRegister = findViewById(R.id.tv_show_error_register);
        backgroundSignUp = findViewById(R.id.background_sign_up);
        progressDialog = new ProgressDialog(this);

    }

    private void setOnClick() {
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInformationRegister();
            }
        });
        backgroundSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });

    }

    private void checkInformationRegister() {
        strFirstName = edFirstName.getText().toString().trim();
        strLastName = edLastName.getText().toString().trim();
        strPhoneNumber = edPhoneNumber.getText().toString().trim();
        strEmail = edEmail.getText().toString().trim();
        strNameAccount = edNameAccount.getText().toString().trim();
        strPassword = edPassword.getText().toString().trim();
        strConfirmPassword = edConfirmPassword.getText().toString().trim();


        if (strFirstName.isEmpty() || strLastName.isEmpty()) {
            tvErrorName.setText("Name is invalid");
        } else if (strFirstName.length() < 2 || strLastName.length() < 2) {
            tvErrorName.setText("Invalid, at least 2 characters");
        } else if (isNumeric(strFirstName) || isNumeric(strFirstName)) {
            tvErrorName.setText("Invalid");
        } else {
            tvErrorName.setText("");
        }
        if (strPhoneNumber.isEmpty()){
            tvErrorPhone.setText("Phone number is invalid");
        }else if (strPhoneNumber.length() < 10){
            tvErrorPhone.setText("Phone number is invalid");
        }else {
            tvErrorPhone.setText("");
        }
        if (strEmail.isEmpty()) {
            tvErrorEmail.setText("Email is empty");
        } else {
            boolean checkEmailValid = EmailValidation(strEmail);
            if (checkEmailValid == true) {
                tvErrorEmail.setText("");
            } else {
                tvErrorEmail.setText("Email is invalid");
            }
        }
        if (strNameAccount.isEmpty()) {
            tvErrorNameAccount.setText("Name account is empty");
        } else if (strNameAccount.length() < 6) {
            tvErrorNameAccount.setText("Invalid name account, at least 6 characters");
        } else {
            tvErrorNameAccount.setText("");
        }
        if (strPassword.isEmpty()) {
            tvErrorPassword.setText("Password is empty");
        } else if (strPassword.length() < 6) {
            tvErrorPassword.setText("Invalid password, at least 6 characters");
        } else {
            tvErrorPassword.setText("");
        }
        if (strConfirmPassword.isEmpty()) {
            tvErrorConfirmPassword.setText("Confirm Password is empty");
        } else if (!strConfirmPassword.equals(strPassword)) {
            tvErrorConfirmPassword.setText("Confirm password doesn't match");
        } else {
            tvErrorConfirmPassword.setText("");
        }
        if (tvErrorName.getText().toString().trim().isEmpty()
                && tvErrorPhone.getText().toString().trim().isEmpty()
                && tvErrorEmail.getText().toString().trim().isEmpty()
                && tvErrorNameAccount.getText().toString().trim().isEmpty()
                && tvErrorPassword.getText().toString().trim().isEmpty()
                && tvErrorConfirmPassword.getText().toString().trim().isEmpty()) {
            setInfoUserToFirebase();
        }
    }

    private void setInfoUserToFirebase() {
        // real time database
        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("User_Account");

        // Authentication email/password
        progressDialog.show();
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // save data to firebase (real-time database)
                            databaseReference.addListenerForSingleValueEvent
                                    (new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            // check if name account was register before
                                            if (snapshot.child("Information_User").hasChild(strNameAccount)) {
                                                progressDialog.dismiss();
                                                tvErrorRegister.setText("Name account was existed");
                                                String getPhoneNumber = snapshot.child(strNameAccount).child("0").child("password").
                                                        getValue(String.class);
                                            }else if (snapshot.child("Phone_Number_Register").hasChild(strPhoneNumber)){
                                                progressDialog.dismiss();
                                                tvErrorRegister.setText("Phone number was existed");
                                            } else {
                                                setDataAccountUser();
                                                databaseReference.child("Phone_Number_Register").child(strPhoneNumber).setValue(strNameAccount);
                                                databaseReference.child("Information_User").child(strNameAccount).setValue(infoUserList);
                                                startActivity(new Intent(SignUpActivity
                                                        .this, SignInActivity.class));
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                        } else {
                            tvErrorRegister.setText("Register unsuccessfully ! System failed or " +
                                    "email is existed");
                            progressDialog.dismiss();

                        }
                    }
                });


    }
    // check email validation

    public boolean EmailValidation(String strEmail) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(strEmail);
        return matcher.find();
    }

    // hide keyboard when touch outside it
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // check string is number?
    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // set list user account
    public List<InfoUser> setDataAccountUser() {
        strFullName = strFirstName + " " + strLastName;
        infoUserList = new ArrayList<>();
        infoUserList.add(new InfoUser(strFullName,strPhoneNumber, strEmail, strNameAccount, strPassword));
        return infoUserList;
    }
}