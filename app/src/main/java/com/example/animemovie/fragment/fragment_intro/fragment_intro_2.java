package com.example.animemovie.fragment.fragment_intro;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.animemovie.R;
import com.example.animemovie.model.modelIntro;

public class fragment_intro_2 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_2,container,false);

        Bundle bundle = getArguments();
        modelIntro modelIntro = (modelIntro) bundle.get("key_photo_text");


        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView imgViewPager2 = view.findViewById(R.id.img_intro_2);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView tvViewPager2 = view.findViewById(R.id.tv_intro_2);
        tvViewPager2.setText(modelIntro.getText());
        imgViewPager2.setImageResource(modelIntro.getImg());

        return view;
    }
}
