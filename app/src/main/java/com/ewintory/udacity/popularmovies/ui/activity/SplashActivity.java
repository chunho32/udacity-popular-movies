package com.ewintory.udacity.popularmovies.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.ewintory.udacity.popularmovies.R;
import com.ewintory.udacity.popularmovies.data.ServerConfig;
import com.ewintory.udacity.popularmovies.data.api.ApiModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Hung Hoang on 7/16/2017.
 */

public class SplashActivity extends BaseActivity {

    private SplashActivity mInstance = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mInstance = this;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.dropbox.com/s/icq5ezdahygcuil/ServerConfig.json?dl=1")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e)
            {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                Gson gson = new GsonBuilder().create();
                ServerConfig p = gson.fromJson(response.body().string(), ServerConfig.class);
                ApiModule.serverConfig = p;
                Intent intent = new Intent(mInstance, BrowseMoviesActivity.class);
                startActivity(intent);
                mInstance.finish();
            }
        });
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