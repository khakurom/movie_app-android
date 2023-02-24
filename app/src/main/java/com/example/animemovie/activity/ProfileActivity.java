package com.example.animemovie.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.animemovie.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    ImageView imgEditFullName, imgEditPassword;
    TextView tvNameAccount, tvFullName, tvEmail, tvPhoneNumber, tvPassword,tvOk;
    String strNameAccount, getStrFullName, getStrEmail, getStrPhoneNumber, getStrPassword;
    EditText edNewPass,edConfirmPass;
    Button btEditOrUpdate;
    LinearLayout backgroundProfileA,backgroundProfileC;
    RelativeLayout backgroundProfileB;
    int flag = 0, flag2 = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("User_Account");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initUI();
        getNameAccount();
        getDataFirebase();
        setOnClick();
    }

    private void setOnClick() {
        imgEditFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imgEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (flag2){
                    case 0:
                        imgEditPassword.setImageResource(R.drawable.ic_clear);
                        edNewPass.setVisibility(View.VISIBLE);
                        edConfirmPass.setVisibility(View.VISIBLE);
                        flag2 = 1;
                        break;
                    case 1:
                    case 2:
                        imgEditPassword.setImageResource(R.drawable.icon_edit_pen);
                        edNewPass.setVisibility(View.GONE);
                        edNewPass.setText("");
                        edConfirmPass.setText("");
                        edConfirmPass.setVisibility(View.GONE);
                        flag2 = 0;
                        break;

                }
            }
        });



        btEditOrUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (flag) {
                    case 0:
                        btEditOrUpdate.setText("Update");
                        imgEditFullName.setVisibility(View.VISIBLE);
                        imgEditPassword.setVisibility(View.VISIBLE);
                        flag = 1;
                        break;
                    case 1:
                        if (flag2 == 1 || flag2 == 2){
                            changePassword();
                            if (flag2 == 2){
                                edNewPass.setBackground(getDrawable(R.drawable.custom_wrong_input));
                                edConfirmPass.setBackground(getDrawable(R.drawable.custom_wrong_input));
                            }
                        }
                        if (flag2 == 0){
                            btEditOrUpdate.setText("Edit profile");
                            imgEditFullName.setVisibility(View.GONE);
                            imgEditPassword.setVisibility(View.GONE);
                            edNewPass.setVisibility(View.GONE);
                            edConfirmPass.setVisibility(View.GONE);
                            imgEditPassword.setImageResource(R.drawable.icon_edit_pen);
                            flag = 0;
                            break;
                        }

                }

            }
        });
        backgroundProfileA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
        backgroundProfileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
        backgroundProfileC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
    }
    private void changePassword (){
        String strNewPassword = edNewPass.getText().toString().trim();
        String strConfirmPassword = edConfirmPass.getText().toString().trim();
        if (flag2 ==1 || flag2 == 2){
            if (strNewPassword.isEmpty() || strConfirmPassword.isEmpty()) {
                Toast.makeText(this, "Password is empty", Toast.LENGTH_SHORT).show();
                flag2 = 2;

            } else if (strNewPassword.length() < 6 ) {
                Toast.makeText(this, "Invalid password, at least 6 characters", Toast.LENGTH_SHORT).show();
                flag2 = 2;
            } else if(!strConfirmPassword.equals(strNewPassword)){
                Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                flag2 = 2;
            }else {
                flag2 = 0;
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child("Information_User").child(strNameAccount).child("0").child("password")
                                .setValue(strNewPassword);
                        edNewPass.setText("");
                        edConfirmPass.setText("");
                        openDialogNotification(Gravity.CENTER);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }else {
            return;
        }

    }


    private void initUI() {
        backgroundProfileA = findViewById(R.id.background_profile_A);
        backgroundProfileB = findViewById(R.id.background_profile_B);
        backgroundProfileC = findViewById(R.id.background_profile_C);
        imgEditFullName = findViewById(R.id.img_edit_fullName);
        imgEditPassword = findViewById(R.id.img_edit_password);
        tvNameAccount = findViewById(R.id.tv_nameAccount);
        tvFullName = findViewById(R.id.tv_fullName);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvEmail = findViewById(R.id.tv_email);
        tvPassword = findViewById(R.id.tv_password);
        edNewPass = findViewById(R.id.ed_newPassword);
        edConfirmPass = findViewById(R.id.ed_password_confirm);
        btEditOrUpdate = findViewById(R.id.btEditOrUpdate);

    }

    private void setProfileUser() {
        tvFullName.setText(getStrFullName);
        tvEmail.setText(getStrEmail);
        tvPhoneNumber.setText(getStrPhoneNumber);
        tvNameAccount.setText(strNameAccount);
        tvPassword.setText(getStrPassword);
    }
    // open dialog notification update
    private void openDialogNotification(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_notification);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        tvOk = dialog.findViewById(R.id.tv_ok);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    // get name account
    private String getNameAccount() {
        Bundle getNameAccount = getIntent().getExtras();
        if (getNameAccount != null) {
            strNameAccount = getNameAccount.getString("key_name_account");
        }
        return strNameAccount;
    }

    // get info from firebase
    private void getDataFirebase() {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                getStrFullName = snapshot.child("Information_User").
                        child(strNameAccount).child("0").child("fullName").getValue(String.class);
                getStrEmail = snapshot.child("Information_User").
                        child(strNameAccount).child("0").child("email").getValue(String.class);
                getStrPhoneNumber = snapshot.child("Information_User").
                        child(strNameAccount).child("0").child("phoneNumber").getValue(String.class);
                getStrPassword = snapshot.child("Information_User").
                        child(strNameAccount).child("0").child("password").getValue(String.class);
                setProfileUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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