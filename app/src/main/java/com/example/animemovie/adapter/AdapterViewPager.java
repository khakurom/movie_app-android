package com.example.animemovie.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.animemovie.fragment.fragment_intro.fragment_intro_1;
import com.example.animemovie.model.modelIntro;

import java.util.List;

public class AdapterViewPager extends FragmentStateAdapter {
    List <modelIntro> modelIntroList;

    public AdapterViewPager(@NonNull FragmentActivity fragmentActivity, List<modelIntro> modelIntroList) {
        super(fragmentActivity);
        this.modelIntroList = modelIntroList;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        modelIntro modelIntro = modelIntroList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key_photo_text", modelIntro);
        fragment_intro_1 photoFragment = new fragment_intro_1();
        photoFragment.setArguments(bundle);
        return photoFragment;
    }

    @Override
    public int getItemCount() {
        if(modelIntroList != null){
            return modelIntroList.size();
        }
        return 0;
    }
}
