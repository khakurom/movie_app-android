package com.example.animemovie.adapter.fragment_search;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.animemovie.R;
import com.example.animemovie.activity.DetailFilm;
import com.example.animemovie.model.model_fragment_home.AllFilm;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {
    public List<AllFilm> mListAllFilm;
    public Activity mActivity;
    public List <AllFilm> mListAllFilm1;


    public  SearchAdapter (List <AllFilm> mList,Activity activity){
        this.mListAllFilm = mList;
        this.mListAllFilm1 = mList;
        this.mActivity = activity;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_film,
                parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllFilm allFilm = mListAllFilm.get(position);
        Glide.with(mActivity).load(allFilm.getImage()).centerCrop().into(holder.imgFilm);
        holder.tvNameFilm.setText(allFilm.getNameFilm());
        String strUrl = allFilm.getUrl();
        holder.layoutItemFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFilmDetail(strUrl);
            }
        });
    }
    private void goToFilmDetail(String url) {
        Intent intent = new Intent(mActivity, DetailFilm.class);
        intent.putExtra("key_url", url);
        mActivity.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (mListAllFilm != null){
            return mListAllFilm.size();
        }
        return 0;
    }
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch =  constraint.toString();
                if (strSearch.isEmpty()){
                    mListAllFilm = mListAllFilm1;
                }else {
                    List<AllFilm>list = new ArrayList<>();
                    for (AllFilm mAllFilm : mListAllFilm1){
                        if (mAllFilm.getNameFilm().toLowerCase().contains(strSearch.toLowerCase())){
                            list.add(mAllFilm);
                        }
                    }
                    mListAllFilm = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mListAllFilm;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mListAllFilm = (List<AllFilm>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgFilm;
        TextView tvNameFilm;
        CardView layoutItemFilm;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFilm = itemView.findViewById(R.id.img_all_film);
            tvNameFilm = itemView.findViewById(R.id.tv_name_all_film);
            layoutItemFilm = itemView.findViewById(R.id.layout_itemFilm);
        }
    }

}
