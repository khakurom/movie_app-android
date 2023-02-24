package com.example.animemovie.adapter.fragment_home;

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
import com.example.animemovie.model.model_fragment_home.AllFilm;

import java.util.List;

public class AllFilmAdapter extends RecyclerView.Adapter<AllFilmAdapter.ViewHolder> {


    public List<AllFilm> mAllFilm;
    String nameAccount;
    Activity mActivity;


    public AllFilmAdapter(List<AllFilm> mAllFilm, Activity mActivity, String nameAccount) {
        this.mAllFilm = mAllFilm;
        this.mActivity = mActivity;
        this.nameAccount = nameAccount;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_film,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllFilm allFilm = mAllFilm.get(position);
        Glide.with(mActivity).load(allFilm.getImage()).centerCrop().into(holder.imgFilm);
        holder.tvNameFilm.setText(allFilm.getNameFilm());
        String strUrl = allFilm.getUrl();
        String strNameFilm = allFilm.getNameFilm();
        String strImage = allFilm.getImage();
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
        if (mAllFilm != null) {
            return mAllFilm.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView layoutItemFilm;
        ImageView imgFilm;
        TextView tvNameFilm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFilm = itemView.findViewById(R.id.img_all_film);
            tvNameFilm = itemView.findViewById(R.id.tv_name_all_film);
            layoutItemFilm = itemView.findViewById(R.id.layout_itemFilm);
        }
    }
}
