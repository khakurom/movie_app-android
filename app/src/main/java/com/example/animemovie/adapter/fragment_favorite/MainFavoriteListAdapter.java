package com.example.animemovie.adapter.fragment_favorite;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animemovie.R;
import com.example.animemovie.activity.DetailFavoriteListActivity;
import com.example.animemovie.model.mode_fragment_favorite.FavoriteList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MainFavoriteListAdapter extends RecyclerView.Adapter<MainFavoriteListAdapter.ViewHolder>{
    private List<FavoriteList> mFavoriteList;
    private Activity activity;
    private String strNameAccount;

    public MainFavoriteListAdapter( String strNameAccount,Activity activity,List<FavoriteList> mFavoriteList) {
        this.mFavoriteList = mFavoriteList;
        this.activity =  activity;
        this.strNameAccount =strNameAccount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainFavoriteListAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_favorite_list2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteList favoriteList = mFavoriteList.get(position);
        holder.tvNameList.setText(favoriteList.getNameList());
        holder.layoutItemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetailListActivity(favoriteList);
            }
        });
        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogDeleteList(Gravity.CENTER,favoriteList);

            }
        });
    }


    private void goToDetailListActivity(FavoriteList favoriteList) {
        Intent intent = new Intent(activity, DetailFavoriteListActivity.class);
        intent.putExtra("key_name_list",favoriteList.getNameList());
        intent.putExtra("key_name_account", strNameAccount);
        activity.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (mFavoriteList != null){
            return mFavoriteList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvNameList;
        private RelativeLayout layoutItemList;
        private Button btDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameList = itemView.findViewById(R.id.tv_nameList);
            layoutItemList = itemView.findViewById(R.id.layout_itemList);
            btDelete = itemView.findViewById(R.id.bt_delete);
        }
    }
    // open dialog create list
    private void openDialogDeleteList(int gravity,FavoriteList favoriteList) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_film_in_list);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);
        TextView tvCancel = dialog.findViewById(R.id.tv_cancel);
        TextView tvDelete = dialog.findViewById(R.id.tv_yes);
        TextView tvNotify = dialog.findViewById(R.id.tv_notify);
        tvNotify.setText("Do you want to delete " + favoriteList.getNameList() );
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete1(favoriteList);
                Delete2(favoriteList);
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
    // delete list from firebase
    private void Delete1( FavoriteList favoriteList) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("User_Account").
                child("Information_User").child(strNameAccount).child("Favorite_List");
        databaseReference.child(favoriteList.getNameList()).removeValue();
    }
    private void Delete2( FavoriteList favoriteList) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("User_Account").
                child("Information_User").child(strNameAccount).child("Favorite_List_Detail");
        databaseReference.child(favoriteList.getNameList()).removeValue();
    }
}
