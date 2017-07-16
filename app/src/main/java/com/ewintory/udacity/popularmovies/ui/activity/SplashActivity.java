package com.ewintory.udacity.popularmovies.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.ewintory.udacity.popularmovies.R;
import com.ewintory.udacity.popularmovies.data.api.ApiModule;

/**
 * Created by Hung Hoang on 7/16/2017.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ApiModule.MOVIE_DB_API_URL = "http://13.76.179.224:3000/api/v3";

        Intent intent = new Intent(this, BrowseMoviesActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }


    /** Called when leaving the activity */
    @Override
    public void onPause() {
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();

    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}