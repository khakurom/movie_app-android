package com.example.animemovie.fragment.fragment_main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animemovie.R;
import com.example.animemovie.adapter.fragment_favorite.MainFavoriteListAdapter;
import com.example.animemovie.model.mode_fragment_favorite.FavoriteList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class fragment_main_favorite extends Fragment {
    private RecyclerView rcvFavoriteList;
    private View view;
    private String strNameAccount;
    private List<FavoriteList> mFavoriteList;
    private Context context;
    private Activity activity;
    private MainFavoriteListAdapter mainFavoriteListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main_favorite, container, false);
        initUI();
        getNameAccountUser();
        getDataFavoriteList();
        setRCVFavoriteList();
        return view;
    }




    private void initUI() {
        rcvFavoriteList = view.findViewById(R.id.rcv_favoriteList2);
        mFavoriteList = new ArrayList<>();
        activity = getActivity();
    }

    // get name account of user
    private void getNameAccountUser() {
        strNameAccount = getArguments().getString("key_name_account");
    }


    // get data favorite list of user from firebase
    private void getDataFavoriteList() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("User_Account").
                child("Information_User").child(strNameAccount).child("Favorite_List");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FavoriteList favoriteList = snapshot.getValue(FavoriteList.class);
                if (favoriteList != null){
                    mFavoriteList.add(favoriteList);
                    mainFavoriteListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                FavoriteList favoriteList = snapshot.getValue(FavoriteList.class);
                if (favoriteList == null || mFavoriteList == null || mFavoriteList.isEmpty() ||
                        mainFavoriteListAdapter == null) {
                    return;
                }
                for (FavoriteList listDelete : mFavoriteList) {
                    if (favoriteList.getNameList().equals( listDelete.getNameList())) {
                        mFavoriteList.remove(listDelete);
                        break;
                    }
                }
                mainFavoriteListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void setRCVFavoriteList() {
        mainFavoriteListAdapter = new MainFavoriteListAdapter(strNameAccount,activity,mFavoriteList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                RecyclerView.VERTICAL, false);
        rcvFavoriteList.setLayoutManager(linearLayoutManager);
        rcvFavoriteList.setFocusable(false);
        rcvFavoriteList.setNestedScrollingEnabled(false);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL);
        rcvFavoriteList.addItemDecoration(itemDecoration);
        rcvFavoriteList.setAdapter(mainFavoriteListAdapter);
    }

}
