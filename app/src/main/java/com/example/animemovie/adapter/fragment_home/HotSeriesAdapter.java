package com.example.animemovie.adapter.fragment_home;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animemovie.R;
import com.example.animemovie.activity.DetailFilm;
import com.example.animemovie.model.model_fragment_home.HotSeries;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class HotSeriesAdapter extends RecyclerView.Adapter<HotSeriesAdapter.ViewHolder>{


    private List<HotSeries> mHotSeriesList;
    private Activity mActivity;
    private String strNameAccount;



    public HotSeriesAdapter(List<HotSeries> mHotSeriesList, Activity mActivity, String strNameAccount) {
        this.mHotSeriesList = mHotSeriesList;
        this.mActivity = mActivity;
        this.strNameAccount = strNameAccount;
        notifyDataSetChanged();
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hot_series,
                parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HotSeries hotSeries = mHotSeriesList.get(position);
        Glide.with(mActivity).load(hotSeries.getImage()).centerCrop().into(holder.imgFilm);
        holder.tvNameFilm.setText(hotSeries.getNameFilm());
        Glide.with(mActivity).load(hotSeries.getUrlImageNumber()).centerCrop().into(holder.imgNumber);
        String strUrl = hotSeries.getUrl();
        String strNameFilm = hotSeries.getNameFilm();
        String strImage = hotSeries.getImage();
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
        intent.putExtra("key_name_account", strNameAccount);
        mActivity.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (mHotSeriesList != null){
            return mHotSeriesList.size();
        }
        return 0;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imgFilm;
        TextView tvNameFilm;
        RoundedImageView imgNumber;
        LinearLayout layoutItemFilm;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFilm = itemView.findViewById(R.id.img_film);
            tvNameFilm = itemView.findViewById(R.id.tv_name_film);
            imgNumber = itemView.findViewById(R.id.img_number);
            layoutItemFilm = itemView.findViewById(R.id.layout_itemFilm);
        }
    }
}
