package com.example.animemovie.fragment.fragment_main;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.animemovie.R;
import com.example.animemovie.activity.ProfileActivity;
import com.example.animemovie.activity.login.SignInActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class fragment_main_account extends Fragment {
    ImageView imgEditProfile, imgLogout;
    TextView tvNameAccount, tvLogout;
    View view;

    String strNameAccount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_account, container, false);
        initUI();
        setInformationUser();
        setOnClickEditProfile();
        return view;

    }


    private void initUI() {
        imgEditProfile = view.findViewById(R.id.img_edit_profile);
        imgLogout = view.findViewById(R.id.img_logout);
        tvNameAccount = view.findViewById(R.id.tv_show_nameAccount);
        tvLogout = view.findViewById(R.id.tv_logout);
    }

    private void setInformationUser() {
        strNameAccount =  getArguments().getString("key_name_account");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("User_Account");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String strFullName = snapshot.child("Information_User").
                        child(strNameAccount).child("0").child("fullName").getValue(String.class);
                tvNameAccount.setText(strFullName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setOnClickEditProfile() {
        imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditProfileActivity();
            }
        });
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNotification(Gravity.CENTER);
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogNotification(Gravity.CENTER);
            }
        });
    }

    private void goToEditProfileActivity() {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra("key_name_account", strNameAccount);
        startActivity(intent);

    }


    private void openDialogNotification(int gravity) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_notification_logout);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        TextView tvCancel =  dialog.findViewById(R.id.tv_cancel);
        TextView tvOk = dialog.findViewById(R.id.tv_yes);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SignInActivity.class));
                getActivity().finish();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }


}
