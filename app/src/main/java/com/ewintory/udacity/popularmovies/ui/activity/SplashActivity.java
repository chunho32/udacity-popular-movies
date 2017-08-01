package com.ewintory.udacity.popularmovies.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ewintory.udacity.popularmovies.R;
import com.ewintory.udacity.popularmovies.data.AppConfig;
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
                onGetAppPackageConfig();
            }
        });
    }

    private void onGetAppPackageConfig()
    {
        String magicToken = "kahdkjhh897a98d7asdaksdjh";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                //.url(ApiModule.serverConfig.getServer_url() + "/appconfig")
                //.addHeader("magicToken",magicToken)
                .url("https://www.dropbox.com/s/oam4jq565etjb71/app_config.json?dl=1")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e)
            {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                Gson gson = new GsonBuilder().create();
                AppConfig p = gson.fromJson(response.body().string(), AppConfig.class);
                ApiModule.appConfig = p;
                checkForceApp();
            }
        });
    }

    public void checkForceApp()
    {
        AppConfig.Force forceConfig = ApiModule.appConfig.getForce();
        if(forceConfig == null)
        {
            Intent intent = new Intent(mInstance, BrowseMoviesActivity.class);
            startActivity(intent);
            mInstance.finish();
            return;
        }

        String externalLink = forceConfig.getExternal_link();
        String packageName = forceConfig.getPackage_name();
        if(externalLink == "" && packageName == "")
        {
            Intent intent = new Intent(mInstance, BrowseMoviesActivity.class);
            startActivity(intent);
            mInstance.finish();
            return;
        }

        SplashActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                final Dialog dialog = new Dialog(SplashActivity.this,R.style.PauseDialog);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_dialog_force);

                TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
                text.setText("New version is available! Update now ?");

                Button dialogCancelButton = (Button) dialog.findViewById(R.id.btn_cancel_dialog);
                dialogCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(mInstance, BrowseMoviesActivity.class);
                        startActivity(intent);
                        mInstance.finish();
                    }
                });

                Button dialogOkButton = (Button) dialog.findViewById(R.id.btn_ok_dialog);
                dialogOkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(packageName != "") {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                            }
                        }
                        else if(externalLink != "") {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(externalLink));
                            startActivity(intent);
                        }
                        mInstance.finish();
                    }
                });

                dialog.show();
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