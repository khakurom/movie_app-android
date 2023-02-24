package com.example.animemovie.adapter.fragment_favorite;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animemovie.IClick;
import com.example.animemovie.R;
import com.example.animemovie.model.mode_fragment_favorite.FavoriteFilmObject;
import com.example.animemovie.model.mode_fragment_favorite.FavoriteList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder> {
    private List<FavoriteList> mListFavorite;
    private IClick iClick;
    private FavoriteFilmObject favoriteFilmObject;
    private String nameAccount, nameList;
    private Activity mActivity;

    public FavoriteListAdapter(Activity mActivity, String nameList, String nameAccount, FavoriteFilmObject
            favoriteFilmObject, List<FavoriteList> mListFavorite, IClick iClick) {
        this.mListFavorite = mListFavorite;
        this.iClick = iClick;
        this.favoriteFilmObject = favoriteFilmObject;
        this.nameList = nameList;
        this.nameAccount = nameAccount;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteListAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_list,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteList mFavoriteListObject = mListFavorite.get(position);
        holder.tvNameList.setText(mFavoriteListObject.getNameList());
        holder.tvNameList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClick.clickItemList(mFavoriteListObject);
            }
        });
        holder.btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogCreateList(Gravity.CENTER, mFavoriteListObject);

            }

        });
    }

    @Override
    public int getItemCount() {
        if (mListFavorite != null) {
            return mListFavorite.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameList;
        Button btAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameList = itemView.findViewById(R.id.tv_nameList);
            btAdd = itemView.findViewById(R.id.bt_add);
        }
    }

    // open dialog create list
    private void openDialogCreateList(int gravity, FavoriteList mFavoriteListObject) {
        final Dialog dialog = new Dialog(mActivity);
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
        TextView tvAdd = dialog.findViewById(R.id.tv_yes);
        TextView tvNotify = dialog.findViewById(R.id.tv_notify);
        tvNotify.setText("Do you want to add " + favoriteFilmObject.getNameFilm() + " into the " +
                mFavoriteListObject.getNameList());
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference("User_Account/" +
                        "Information_User/" + nameAccount + "/Favorite_List_Detail/" +
                        mFavoriteListObject.getNameList());
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        databaseReference.child(favoriteFilmObject.getNameFilm()).
                                setValue(favoriteFilmObject);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
}
