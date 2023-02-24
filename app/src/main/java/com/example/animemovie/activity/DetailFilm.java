package com.example.animemovie.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.animemovie.IClick;
import com.example.animemovie.R;
import com.example.animemovie.model.mode_fragment_favorite.FavoriteFilmObject;
import com.example.animemovie.fragment.fragment_main.BottomSheetFragmentFavorite;
import com.example.animemovie.model.mode_fragment_favorite.FavoriteList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DetailFilm extends AppCompatActivity {
    private WebView mWebView;
    private String url, nameAccount, nameFilm, image;
    private ProgressBar progressBar;
    private RelativeLayout backgroundAddList;
    private List<FavoriteList> mListFavoriteList;
    private Context context;
    private RecyclerView rcvFavoriteList;
    private FavoriteList favoriteList;
    private Activity activity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);
        initUI();
        getFilmDetail();
        getFavoriteList();
        setWebView();
        setOnClick();
        activity = DetailFilm.this;


    }

    private void setOnClick() {
        backgroundAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTheList();
            }
        });
    }


    private void initUI() {
        mWebView = findViewById(R.id.id_webView);
        progressBar = findViewById(R.id.progressBar);
        backgroundAddList = findViewById(R.id.background_addList);
        mListFavoriteList = new ArrayList<>();
        rcvFavoriteList = findViewById(R.id.rcv_favoriteList);
    }

    // setting webview
    private void setWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setSaveFormData(true);
        webSettings.setDomStorageEnabled(true);
        //improve webView performance
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setEnableSmoothTransition(true);
        mWebView.setWebChromeClient(new ChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                setTitle("Loading ");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setTitle(view.getTitle());
                progressBar.setVisibility(View.GONE);
            }
        });
        mWebView.loadUrl(url);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mWebView.restoreState(savedInstanceState);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
    // set chrome client

    private class ChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        ChromeClient() {
        }

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    // get url, image, nameFilm, nameAccount of user
    private String getFilmDetail() {
        Bundle getData = getIntent().getExtras();
        if (getData != null) {
            url = getData.getString("key_url");
            nameAccount = getData.getString("key_name_account");
            nameFilm = getData.getString("key_name_film");
            image = getData.getString("key_image");
        }
        return url;
    }

    //     get favorite list of user from firebase
    private void getFavoriteList() {
        if (nameAccount == null){
            return;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("User_Account").
                child("Information_User").child(nameAccount).child("Favorite_List");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FavoriteList favoriteList = snapshot.getValue(FavoriteList.class);
                if (favoriteList != null){
                    mListFavoriteList.add(favoriteList);

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    // set rcv favorite list of user
    private void openTheList() {
        FavoriteFilmObject favoriteFilmObject = new FavoriteFilmObject(image,nameFilm,url);
        BottomSheetFragmentFavorite bottomSheetFragmentFavorite = new BottomSheetFragmentFavorite(
                activity,favoriteFilmObject, mListFavoriteList,favoriteList,nameAccount, new IClick() {
            @Override
            public void clickItemList(FavoriteList favoriteList) {
                Intent intent = new Intent(DetailFilm.this, DetailFavoriteListActivity.class);
                intent.putExtra("key_name_list",favoriteList.getNameList());
                intent.putExtra("key_name_account", nameAccount);
                activity.startActivity(intent);
            }
        });
        bottomSheetFragmentFavorite.show(getSupportFragmentManager(), bottomSheetFragmentFavorite.getTag());
    }



}