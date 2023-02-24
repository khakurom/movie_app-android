package com.example.animemovie.adapter.fragment_favorite;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animemovie.R;
import com.example.animemovie.activity.DetailFilm;
import com.example.animemovie.model.mode_fragment_favorite.FavoriteFilmObject;

import java.util.List;

public class FavoriteFilmAdapter extends RecyclerView.Adapter<FavoriteFilmAdapter.ViewHolder>{
    private List<FavoriteFilmObject> mFavoriteList;
    private Activity mActivity;
    private String nameAccount;

    public FavoriteFilmAdapter(String nameAccount,List<FavoriteFilmObject> mFavoriteList, Activity mActivity) {
        this.mFavoriteList = mFavoriteList;
        this.mActivity = mActivity;
        this.nameAccount = nameAccount;
    }

    @NonNull
    @Override
    public FavoriteFilmAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoriteFilmAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_all_film, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteFilmAdapter.ViewHolder holder, int position) {
        FavoriteFilmObject favoriteFilmObject = mFavoriteList.get(position);
        Glide.with(mActivity).load(favoriteFilmObject.getImage()).centerCrop().into(holder.imgFilm);
        holder.tvNameFilm.setText(favoriteFilmObject.getNameFilm());
        String strUrl = favoriteFilmObject.getUrl();
        String strNameFilm = favoriteFilmObject.getNameFilm();
        String strImage = favoriteFilmObject.getImage();
        holder.layoutItemFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFilmDetail(strNameFilm,strImage,strUrl);
            }
        });
    }
    private void goToFilmDetail(String nameFilm, String image,String url) {
        Intent intent = new Intent(mActivity, DetailFilm.class);
        intent.putExtra("key_name_film", nameFilm);
        intent.putExtra("key_image", image);
        intent.putExtra("key_url", url);
        intent.putExtra("key_name_account", nameAccount);
        mActivity.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (mFavoriteList != null){
            return mFavoriteList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgFilm;
        private TextView tvNameFilm;
        private CardView layoutItemFilm;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFilm = itemView.findViewById(R.id.img_all_film);
            tvNameFilm = itemView.findViewById(R.id.tv_name_all_film);
            layoutItemFilm = itemView.findViewById(R.id.layout_itemFilm);
        }
    }
}
