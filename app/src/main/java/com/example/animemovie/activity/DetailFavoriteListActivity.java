package com.example.animemovie.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.animemovie.R;
import com.example.animemovie.adapter.fragment_favorite.FavoriteFilmAdapter;
import com.example.animemovie.model.mode_fragment_favorite.FavoriteFilmObject;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DetailFavoriteListActivity extends AppCompatActivity {
    private RecyclerView rcvFavoriteFilmList;
    private FavoriteFilmAdapter favoriteFilmAdapter;
    private List<FavoriteFilmObject> favoriteFilmObjectList;
    private TextView tvNameList;
    private String strNameList, strNameAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_favorite_list);
        initUI();
        getNameListAndAccount();
        getFavoriteListUser();
        setRCVFavoriteFilmList();

    }




    private void initUI() {
        rcvFavoriteFilmList = findViewById(R.id.rcv_favoriteFilmList);
        tvNameList =  findViewById(R.id.tv_nameList);
        favoriteFilmObjectList = new ArrayList<>();

    }
    // get data favorite list from user
    private void getFavoriteListUser() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("User_Account")
                .child("Information_User").child(strNameAccount).child("Favorite_List_Detail")
                .child(strNameList);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FavoriteFilmObject favoriteFilmObject = snapshot.getValue(FavoriteFilmObject.class);
                if (favoriteFilmObject != null && !favoriteFilmObject.getNameFilm().isEmpty()){
                    favoriteFilmObjectList.add(favoriteFilmObject);
                    favoriteFilmAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    // set rcv film in favorite list of user
    private void setRCVFavoriteFilmList() {
        favoriteFilmAdapter = new FavoriteFilmAdapter(strNameAccount,favoriteFilmObjectList,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3,
                LinearLayoutManager.VERTICAL, false);
        rcvFavoriteFilmList.setLayoutManager(gridLayoutManager);
        rcvFavoriteFilmList.setFocusable(false);
        rcvFavoriteFilmList.setNestedScrollingEnabled(false);
        rcvFavoriteFilmList.setAdapter(favoriteFilmAdapter);
    }
    // get name list
    private void getNameListAndAccount() {
        Bundle getData = getIntent().getExtras();
        if (getData != null) {
            strNameList = getData.getString("key_name_list");
            strNameAccount = getData.getString("key_name_account");
        }
        tvNameList.setText(strNameList);

    }

}