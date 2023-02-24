package com.example.animemovie.fragment.fragment_main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.animemovie.R;
import com.example.animemovie.adapter.fragment_home.AllFilmAdapter;
import com.example.animemovie.adapter.fragment_home.HotSeriesAdapter;
import com.example.animemovie.adapter.fragment_home.ViewPagerAdapter_1;
import com.example.animemovie.model.model_fragment_home.AllFilm;
import com.example.animemovie.model.model_fragment_home.HotSeries;
import com.example.animemovie.model.model_fragment_home.Photo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class fragment_main_home extends Fragment {
    private TextView tvFeatures;
    private View view;
    private ViewPager2 introAboutHome;
    private AllFilmAdapter allFilmAdapter;
    private List<Photo> mListPhoto;
    private List<AllFilm> mListAllFilm;
    public String strNameAccount;
    private ImageView imgNotify;
    private List<HotSeries> mListHotSeries;
    private HotSeriesAdapter hotSeriesAdapter;
    private RecyclerView rcvHotSeries, rcvAllFilm;
    private Context context;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (introAboutHome.getCurrentItem() == mListPhoto.size() - 1) {
                introAboutHome.setCurrentItem(0);


            } else {
                introAboutHome.setCurrentItem(introAboutHome.getCurrentItem() + 1);
            }
        }
    };
    Handler mHandler = new Handler();

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_home, container, false);

        initUI();
        setOnCLick();
        getNameAccount();
        getDataAllFilm();
        getDataHotSeries();
        setDataViewPager();
        setViewPagerAdapter();
        setRcvHotSeries();
        setRcvAllFilm();
        return view;


    }

    private void initUI() {
        tvFeatures = view.findViewById(R.id.tv_features);
        introAboutHome = view.findViewById(R.id.viewpager_fragment_home);
        rcvHotSeries = view.findViewById(R.id.rcv_hot_series);
        rcvAllFilm = view.findViewById(R.id.rcv_all_film);
        mListAllFilm = new ArrayList<>();
        mListHotSeries = new ArrayList<>();
        imgNotify = view.findViewById(R.id.img_notify);


    }
    private void  setOnCLick() {
        imgNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), strNameAccount, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setViewPagerAdapter() {
        settingViewPager2();
        introAboutHome.setAdapter(new ViewPagerAdapter_1(mListPhoto, introAboutHome));
        introAboutHome.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mHandler.removeCallbacks(mRunnable);
                mHandler.postDelayed(mRunnable, 3000);
            }
        });


    }

    private void settingViewPager2() {
        introAboutHome.setOffscreenPageLimit(3);
        introAboutHome.setClipToPadding(false);
        introAboutHome.setClipChildren(false);
        introAboutHome.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(20));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        introAboutHome.setPageTransformer(compositePageTransformer);

    }

    private List<Photo> setDataViewPager() {
        mListPhoto = new ArrayList<>();

        mListPhoto.add(new Photo(R.drawable.img_home_1));
        mListPhoto.add(new Photo(R.drawable.img_home_2));
        mListPhoto.add(new Photo(R.drawable.img_home_3));
        mListPhoto.add(new Photo(R.drawable.img_home_4));
        mListPhoto.add(new Photo(R.drawable.img_home_5));
        return mListPhoto;
    }

    // set rcv hot series
    private void setRcvHotSeries() {
        hotSeriesAdapter = new HotSeriesAdapter(mListHotSeries, getActivity(), strNameAccount);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                RecyclerView.HORIZONTAL, false);
        rcvHotSeries.setLayoutManager(linearLayoutManager);
        rcvHotSeries.setFocusable(false);
        rcvHotSeries.setNestedScrollingEnabled(false);
        rcvHotSeries.setAdapter(hotSeriesAdapter);

    }

    // get data hot series from firebase
    private void getDataHotSeries() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("List_Film").child("Hot_Series");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HotSeries hotSeries = snapshot.getValue(HotSeries.class);
                if (hotSeries != null){
                    mListHotSeries.add(hotSeries);
                    hotSeriesAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                HotSeries hotSeries = snapshot.getValue(HotSeries.class);
                if (hotSeries == null || mListHotSeries == null || mListHotSeries.isEmpty() ||
                        hotSeriesAdapter == null) {
                    return;
                }
                for (HotSeries mHotSeries : mListHotSeries) {
                    if (hotSeries.getId() == mHotSeries.getId()) {
                        mHotSeries.setImage(hotSeries.getImage());
                        mHotSeries.setNameFilm(hotSeries.getNameFilm());
                        mHotSeries.setUrl(hotSeries.getUrl());
                        break;
                    }
                }
                hotSeriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                HotSeries hotSeries = snapshot.getValue(HotSeries.class);
                if (hotSeries == null || mListHotSeries == null || mListHotSeries.isEmpty() ||
                        hotSeriesAdapter == null) {
                    return;
                }
                for (HotSeries movieDelete : mListHotSeries) {
                    if (hotSeries.getId() == movieDelete.getId()) {
                        mListHotSeries.remove(movieDelete);
                        break;
                    }
                }
                hotSeriesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // set rcv all film on app
    private void setRcvAllFilm() {
        allFilmAdapter = new AllFilmAdapter(mListAllFilm, getActivity(), strNameAccount);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3,
                LinearLayoutManager.VERTICAL, false);
        rcvAllFilm.setLayoutManager(gridLayoutManager);
        rcvAllFilm.setFocusable(false);
        rcvAllFilm.setNestedScrollingEnabled(false);
        rcvAllFilm.setAdapter(allFilmAdapter);
    }


    // get data all film from firebase:
    private void getDataAllFilm() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("List_Film").child("All_Film");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                AllFilm mAllFilm = snapshot.getValue(AllFilm.class);
                if (mAllFilm != null){
                    mListAllFilm.add(mAllFilm);
                    allFilmAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                AllFilm allFilm = snapshot.getValue(AllFilm.class);
                if (allFilm == null || mListAllFilm == null || mListAllFilm.isEmpty() || allFilmAdapter == null) {
                    return;
                }
                for (AllFilm mAllFilm : mListAllFilm) {
                    if (allFilm.getId() == mAllFilm.getId()) {
                        mAllFilm.setImage(allFilm.getImage());
                        mAllFilm.setNameFilm(allFilm.getNameFilm());
                        mAllFilm.setUrl(allFilm.getUrl());
                        break;
                    }
                }
                allFilmAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                AllFilm allFilm = snapshot.getValue(AllFilm.class);
                if (allFilm == null || mListAllFilm == null || mListAllFilm.isEmpty() || allFilmAdapter == null) {
                    return;
                }
                for (AllFilm movieDelete : mListAllFilm) {
                    if (allFilm.getId() == movieDelete.getId()) {
                        mListAllFilm.remove(movieDelete);
                        break;
                    }
                }
                allFilmAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(mRunnable, 3000);
    }

    // get name account
    private void getNameAccount() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Bundle data = this.getArguments();
        if (data != null){
            strNameAccount = data.getString("key_name_account");
        }else {
            Toast.makeText(getActivity(), strNameAccount, Toast.LENGTH_SHORT).show();
        }
    }
}
