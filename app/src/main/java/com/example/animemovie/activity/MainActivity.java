package com.example.animemovie.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.WindowManager;


import com.example.animemovie.R;
import com.example.animemovie.fragment.fragment_main.fragment_main_account;
import com.example.animemovie.fragment.fragment_main.fragment_main_favorite;
import com.example.animemovie.fragment.fragment_main.fragment_main_home;
import com.example.animemovie.fragment.fragment_main.fragment_main_search;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationBar;
    ConstraintLayout constraintLayout;
    String strNameAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        getNameAccount();
        setBottomNavigationBar();
    }


    private void initUI() {
        bottomNavigationBar = findViewById(R.id.id_bottom_navi_bar);
        constraintLayout = findViewById(R.id.constraint_layout);


    }

    private void setBottomNavigationBar() {

        fragment_main_home fragmentHome = new fragment_main_home();
        Bundle bundle = new Bundle();
        // send name account to fragment
        bundle.putString("key_name_account", strNameAccount);
        fragmentHome.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().
                beginTransaction();
        fragmentTransaction.replace(R.id.constraint_layout, fragmentHome).commit();


        bottomNavigationBar.setActivated(false);
        bottomNavigationBar.setSelected(false);
        bottomNavigationBar.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.id_home:
                        fragment_main_home fragmentMainHome = new fragment_main_home();
                        Bundle bundle1 = new Bundle();
                        // send name account to fragment
                        bundle1.putString("key_name_account", strNameAccount);
                        fragmentMainHome.setArguments(bundle1);
                        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().
                                beginTransaction();
                        fragmentTransaction1.replace(R.id.constraint_layout, fragmentMainHome).commit();
                        break;

                    case R.id.id_search:
                        fragment_main_search fragment_main_search  = new fragment_main_search();
                        Bundle bundle2 = new Bundle();
                        // send name account to fragment
                        bundle2.putString("key_name_account",strNameAccount);
                        fragment_main_search.setArguments(bundle2);
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.constraint_layout,fragment_main_search);
                        fragmentTransaction2.commit();
                        break;
                    case R.id.id_favorite:
                        fragment_main_favorite fragmentFavorite = new fragment_main_favorite();
                        Bundle bundle3 = new Bundle();
                        // send name account to fragment
                        bundle3.putString("key_name_account",strNameAccount);
                        fragmentFavorite.setArguments(bundle3);
                        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.constraint_layout,fragmentFavorite);
                        fragmentTransaction3.commit();
                        break;
                    case R.id.id_account:
                        fragment_main_account fragmentAccount = new fragment_main_account();
                        Bundle bundle4 = new Bundle();
                        // send name account to fragment
                        bundle4.putString("key_name_account", strNameAccount);
                        fragmentAccount.setArguments(bundle4);
                        FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().
                                beginTransaction();
                        fragmentTransaction4.replace(R.id.constraint_layout, fragmentAccount).commit();

                        break;

                }
                return true;
            }
        });
    }

    // get name account
    private String getNameAccount() {
        Bundle getNameAccount = getIntent().getExtras();
        if (getNameAccount != null) {
            strNameAccount = getNameAccount.getString("key_name_account");
        }
        return strNameAccount;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}