package com.example.animemovie.fragment.fragment_intro;

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

public class fragment_intro_1 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_1,container,false);

        Bundle bundle = getArguments();
        modelIntro modelIntro = (modelIntro) bundle.get("key_photo_text");


        ImageView imgViewPager1 = view.findViewById(R.id.img_intro_1);
        TextView  tvViewPager1 = view.findViewById(R.id.tv_intro_1);
        tvViewPager1.setText(modelIntro.getText());
        imgViewPager1.setImageResource(modelIntro.getImg());

        return view;
    }
}
