package com.example.animemovie.fragment.fragment_main;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animemovie.IClick;
import com.example.animemovie.R;
import com.example.animemovie.model.mode_fragment_favorite.FavoriteFilmObject;
import com.example.animemovie.adapter.fragment_favorite.FavoriteListAdapter;
import com.example.animemovie.model.mode_fragment_favorite.FavoriteList;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class BottomSheetFragmentFavorite extends BottomSheetDialogFragment {
    private FavoriteList favoriteList;
    private List<FavoriteList> mFavoriteList;
    private IClick iClick;
    private String strNameAccount, strNameList;
    private int index=0;
    private FavoriteFilmObject favoriteFilmObject;
    private Activity mActivity;


    public BottomSheetFragmentFavorite(Activity mActivity,FavoriteFilmObject favoriteFilmObject, List<FavoriteList>
            mFavoriteList, FavoriteList favoriteList, String strNameAccount, IClick iClick) {
        this.mFavoriteList = mFavoriteList;
        this.favoriteList = favoriteList;
        this.strNameAccount = strNameAccount;
        this.iClick = iClick;
        this.favoriteFilmObject =favoriteFilmObject;
        this.mActivity =mActivity;

    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_fav_list,null);
        bottomSheetDialog.setContentView(view);
        ImageView imgCreateList = view.findViewById(R.id.img_create_list);
        imgCreateList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogCreateList(Gravity.CENTER);
            }
        });
        RecyclerView rcvFavoriteList = view.findViewById(R.id.rcv_favoriteList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rcvFavoriteList.setLayoutManager(linearLayoutManager);

        FavoriteListAdapter favoriteListAdapter = new FavoriteListAdapter(mActivity,strNameList,strNameAccount
                ,favoriteFilmObject, mFavoriteList, new IClick() {
            @Override
            public void clickItemList(FavoriteList favoriteList) {
                iClick.clickItemList(favoriteList);
            }
        });
        rcvFavoriteList.setAdapter(favoriteListAdapter);
        RecyclerView.ItemDecoration itemDecoration =  new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        rcvFavoriteList.addItemDecoration(itemDecoration);

        return bottomSheetDialog;
    }
    // open dialog create list
    private void openDialogCreateList(int gravity) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_list);
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
        TextView tvCreate = dialog.findViewById(R.id.tv_create);

        tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edNewList = dialog.findViewById(R.id.ed_nameList);
                strNameList = edNewList.getText().toString().trim();
                setNameList ();
                createList();
                createList2();
                dialog.dismiss();

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
    // create list to firebase
    private void createList (){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("User_Account")
                .child("Information_User").child(strNameAccount).child("Favorite_List");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                databaseReference.child(String.valueOf(strNameList)).setValue(favoriteList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void createList2 (){
        FavoriteFilmObject favoriteFilmObjectEmpty = new FavoriteFilmObject("","","");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("User_Account/Information_User/" +
                        strNameAccount+ "/Favorite_List_Detail");
        databaseReference.child(strNameList).child("0").setValue(favoriteFilmObjectEmpty);
    }
    // create list object
    private void setNameList (){
        favoriteList = new FavoriteList(strNameList);
    }
}
