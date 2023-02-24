package com.example.animemovie.fragment.fragment_main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.animemovie.R;
import com.example.animemovie.adapter.fragment_search.SearchAdapter;
import com.example.animemovie.model.model_fragment_home.AllFilm;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class fragment_main_search extends Fragment {
    View view;
    RecyclerView recyclerView;
    SearchAdapter searchFilmAdapter;
    List<AllFilm> mListAllFilm;
    Context context;
    SearchView searchView;
    TextView tvNotFound;
    NestedScrollView layoutSearch;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_search, container, false);
        initUI();
        getDataAllFilm();
        setOnClick();
        setRcvSearch();

        return view;
    }



    private void initUI() {
        recyclerView = view.findViewById(R.id.rcv_search);
        mListAllFilm = new ArrayList<>();
        searchView = view.findViewById(R.id.search);
        tvNotFound = view.findViewById(R.id.tv_notFound);
        layoutSearch = view.findViewById(R.id.layout_search);
    }
    private void setOnClick() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFilmAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFilmAdapter.getFilter().filter(newText);
                return false;
            }
        });
        // hide keyboard
        layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });

    }

    // hide keyboard when touch outside it
    public void hideKeyboard() {
        View view = this.getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity(). getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // set rcv search film
    private void setRcvSearch() {

        searchFilmAdapter = new SearchAdapter(mListAllFilm,getActivity());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(searchFilmAdapter);


    }

    // get data from firebase
    private void getDataAllFilm() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("List_Film");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.child("All_Film").getChildren()) {
                    AllFilm mAllFilm = dataSnapshot.getValue(AllFilm.class);

                    if (mAllFilm != null) {
                        mListAllFilm.add(mAllFilm);
                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }

                }
                searchFilmAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.search_film, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }




}

