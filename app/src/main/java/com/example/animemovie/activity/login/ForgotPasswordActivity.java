package com.example.animemovie.activity.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.animemovie.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordActivity extends AppCompatActivity {
    Button btCancel, btVerify, btSend;
    EditText edOTP, edEmailOrPhone;
    LinearLayout backgroundForgetPassword;
    String strEmailOrPhone, strVerificationId, otp;
    TextView tvErrorOrPhone, tvSentOtpAgain,tvShowNotification,tvShowSecond;
    FirebaseAuth mAuth;
    int k;

    PhoneAuthProvider.ForceResendingToken mForceResendingToken;

    private static final String KEY_VERIFICATION_ID = "key_verification_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initUI();
        setOnClick();
        if (strVerificationId == null && savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_VERIFICATION_ID, strVerificationId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        strVerificationId = savedInstanceState.getString(KEY_VERIFICATION_ID);
    }

    private void initUI() {
        btSend = findViewById(R.id.bt_send);
        backgroundForgetPassword = findViewById(R.id.background_forget_password);
        edEmailOrPhone = findViewById(R.id.ed_email_phoneNumber);
        tvErrorOrPhone = findViewById(R.id.tv_error_emailOrPhone);
    }

    // set on click listener
    private void setOnClick() {
        // send email/phone number
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                checkEmailOrPhone();
            }
        });
        // hide keyboard
        backgroundForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
    }

    // check user use phone or email
    private void checkEmailOrPhone() {
        strEmailOrPhone = edEmailOrPhone.getText().toString().trim();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("User_Account");
        if (strEmailOrPhone.isEmpty()) {
            tvErrorOrPhone.setText("Email/Phone number is empty");
        } else {
            if (isNumeric(strEmailOrPhone)) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("Phone_Number_Register").hasChild(strEmailOrPhone)) {
                            openDialogPhoneNumber(Gravity.CENTER);
                            onClickVerify("+84" + strEmailOrPhone);
                            tvErrorOrPhone.setText("");
                        } else {
                            tvErrorOrPhone.setText("Phone number is not existed");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else if (EmailValidation(strEmailOrPhone)){
                resetPassword(strEmailOrPhone);
                tvErrorOrPhone.setText("");
            }else {
                tvErrorOrPhone.setText("Email/phone number is invalid");
            }
        }

    }
    // change password through email
    private void resetPassword (String strEmail){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(strEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            String notification1 = "Check email now";
                            openDialogResetPassword(Gravity.CENTER,notification1);
                        }else {
                            String notification2 = "Something wrong,please send or check email again";
                            openDialogResetPassword(Gravity.CENTER, notification2);
                        }
                    }
                });
    }


    // verify phone number
    private void onClickVerify(String strPhoneNumber) {
        mAuth = FirebaseAuth.getInstance();
        if (strPhoneNumber == null) {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(strPhoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)
                        .setForceResendingToken(mForceResendingToken)// Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential
                                                                        phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(ForgotPasswordActivity.this,
                                        "Invalid phone number", Toast.LENGTH_SHORT).show();
                                countDownTimer();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationID, @NonNull
                                    PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(verificationID, forceResendingToken);
                                strVerificationId = verificationID;
                                mForceResendingToken = forceResendingToken;
                                countDownTimer();
                            }

                        })

                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
    // dialog reset password
    private void openDialogResetPassword(int gravity, String notification) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reset_password_dialog);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();

        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        tvShowNotification = dialog.findViewById(R.id.tv_show_notification);
        btCancel = dialog.findViewById(R.id.bt_cancel);
        tvShowNotification.setText(notification);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    // open dialog verify otp
    private void openDialogPhoneNumber(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verify_otp_dialog);
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();

        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        tvSentOtpAgain = dialog.findViewById(R.id.tv_send_otp_again);
        btCancel = dialog.findViewById(R.id.bt_cancel_otp);
        btVerify = dialog.findViewById(R.id.bt_verify_otp);
        tvShowSecond = dialog.findViewById(R.id.tv_show_second);
        edOTP = dialog.findViewById(R.id.ed_otp);
        otp = edOTP.getText().toString().trim();


        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edOTP = dialog.findViewById(R.id.ed_otp);
                otp = edOTP.getText().toString().trim();
                if (otp.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "OTP is empty", Toast.LENGTH_SHORT).show();
                } else {
                   onClickSendOTP(otp);
                }

            }
        });
        dialog.show();

    }


    private void onClickSendOTP(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(strVerificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if verify otp successfully

                            goToSetPasswordActivity();
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(ForgotPasswordActivity.this, "Invalid otp", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    // go to set password activity
    private void goToSetPasswordActivity() {
        Intent intent =  new Intent(ForgotPasswordActivity.this, SetPasswordAgainActivity.class);
        intent.putExtra("key_phone", strEmailOrPhone);
        startActivity(intent);
    }

    // hide keyboard when touch outside it
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // check string is number
    public boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    // check email validation

    public boolean EmailValidation(String strEmail) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern emailPat = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(strEmail);
        return matcher.find();
    }
    // count down timer
    private void countDownTimer(){
        CountDownTimer countDownTimer =  new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long l) {
                tvShowSecond.setText(String.valueOf((int)(l/1000))+"s");
            }

            @Override
            public void onFinish() {

            }
        }.start();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onClickVerify(strEmailOrPhone);
            }
        },60000);

    }
}