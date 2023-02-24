package com.example.animemovie.adapter.fragment_home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.animemovie.R;
import com.example.animemovie.model.model_fragment_home.Photo;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class ViewPagerAdapter_1 extends RecyclerView.Adapter<ViewPagerAdapter_1.ViewHolder>{
    private final List<Photo> mList;
    ViewPager2 viewPager2;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mList.addAll(mList);
            notifyDataSetChanged();
        }
    };


    public ViewPagerAdapter_1(List<Photo> mList, ViewPager2 viewPager2) {
        this.mList = mList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.photo_viewpager_fragment_home,parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setImgPhoto(mList.get(position));
        if(position == mList.size()-2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        if (mList != null){
            return mList.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imgPhoto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_photo_fragment_home_1);

        }
        void setImgPhoto (Photo mListPhoto ){
            imgPhoto.setImageResource(mListPhoto.getImage());
        }
    }
}
