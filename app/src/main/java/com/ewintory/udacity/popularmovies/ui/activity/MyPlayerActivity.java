package com.ewintory.udacity.popularmovies.ui.activity;

import android.net.Uri;
import android.os.Bundle;

import com.ewintory.udacity.popularmovies.R;
import com.google.android.exoplayer.text.Subtitle;
import com.halilibo.bettervideoplayer.BetterVideoCallback;
import com.halilibo.bettervideoplayer.BetterVideoPlayer;
import com.halilibo.bettervideoplayer.subtitle.CaptionsView;

/**
 * Created by hunghm on 6/30/17.
 */

public class MyPlayerActivity extends BaseActivity implements BetterVideoCallback{
    private static final String TEST_URL = "http://sv2.clgt.vn/videoplayback?hash=aHR0cHM6Ly9yMTEtLS1zbi1wNXFsc25sci5jLm1haWwuZ29vZ2xlLmNvbS92aWRlb3BsYXliYWNrP2lkPTU5ZTlhMGIxN2VlZjVkZDcmaXRhZz0yMiZzb3VyY2U9d2ViZHJpdmUmcmVxdWlyZXNzbD15ZXMmdHRsPXRyYW5zaWVudCZtbT0zMCZtbj1zbi1wNXFsc25sciZtcz1ueHUmbXY9bSZwbD0yMyZlaT1vWnhYV1pfYUtZU2pxUVdTbUxvbyZkcml2ZWlkPTBCN01COHJ2ZkoxakNRakl5T1hsaVVWOU9WVzgmbWltZT12aWRlby9tcDQmbG10PTE0OTQ4NTcwMzg3MDk3MjImbXQ9MTQ5ODkxMzg2NSZpcD0xOTguMjU1Ljc4LjEwOCZpcGJpdHM9MCZleHBpcmU9MTQ5ODkyODM1MyZjcD1RVkpPV2twZlYxTlRSRmhOT2tRM1VGWlBkRk16UWt4TiZzcGFyYW1zPWlwJTJDaXBiaXRzJTJDZXhwaXJlJTJDaWQlMkNpdGFnJTJDc291cmNlJTJDcmVxdWlyZXNzbCUyQ3R0bCUyQ21tJTJDbW4lMkNtcyUyQ212JTJDcGwlMkNlaSUyQ2RyaXZlaWQlMkNtaW1lJTJDbG10JTJDY3Amc2lnbmF0dXJlPTFEOUYxNERBMjQ0Qjk3Njk4MzcyQUE3M0U5OTQxOUM5OUU1MTY0NUMuMUNCQTMyRjhEMTlBODU4NDQ3QzlFOUNGRTg0NUVERTFCNUJDNThFQyZrZXk9Y2syJmFwcD1leHBsb3JlcnxEUklWRV9TVFJFQU09WjBSUlBzVDRCR0k7IFBhdGg9LzsgRG9tYWluPW1haWwuZ29vZ2xlLmNvbTsgSHR0cE9ubHk7IFNlY3VyZU5JRD0xMDY9RjgwV0tUNUUwNTVIenpHakwtXzVFcDRsWjNCdmRLaF9zRTd1UWhDaGZJQVVnWFJieE1vRlZwaFBjbjFxajE0RTdVODZzSHFjTHY5Qmk0cjdXMFc3R0hCZkFjZXZzcFZDellBUXQ2M2lHM0hiSkt4SEVITnVVS3lXLUM2bGtTVTc7IFBhdGg9LzsgRG9tYWluPWdvb2dsZS5jb207IEV4cGlyZXM9U3VuLCAzMSBEZWMgMjAxNyAxMjo1OToxMyBHTVQ7IEh0dHBPbmx5";

    private BetterVideoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_video_player);

        // Grabs a reference to the player view
        player = (BetterVideoPlayer) findViewById(R.id.my_video_player);

        // Sets the callback to this Activity, since it inherits EasyVideoCallback
        player.setCallback(this);

        // Sets the source to the HTTP URL held in the TEST_URL variable.
        // To play files, you can use Uri.fromFile(new File("..."))
        player.setSource(Uri.parse(TEST_URL));

        // From here, the player view will show a progress indicator until the player is prepared.
        // Once it's prepared, the progress indicator goes away and the controls become enabled for the user to begin playback.

        // Online SUBRIP subtitle
        player.setCaptions(R.raw.sub, CaptionsView.CMime.SUBRIP);

        //player.setCaptions(Uri.parse("https://www.example.com/subrip.srt"), CaptionsView.CMime.SUBRIP);


        player.setHideControlsOnPlay(true);

        player.enableSwipeGestures(getWindow());
    }

    @Override
    public void onPause() {
        super.onPause();
        // Make sure the player stops playing if the user presses the home button.
        player.pause();
    }

    // Methods for the implemented EasyVideoCallback

    @Override
    public void onStarted(BetterVideoPlayer player) {
        //Log.i(TAG, "Started");
    }

    @Override
    public void onPaused(BetterVideoPlayer player) {
        //Log.i(TAG, "Paused");
    }

    @Override
    public void onPreparing(BetterVideoPlayer player) {
        //Log.i(TAG, "Preparing");
    }

    @Override
    public void onPrepared(BetterVideoPlayer player) {
        //Log.i(TAG, "Prepared");
    }

    @Override
    public void onBuffering(int percent) {
        //Log.i(TAG, "Buffering " + percent);
    }

    @Override
    public void onError(BetterVideoPlayer player, Exception e) {
        //Log.i(TAG, "Error " +e.getMessage());
    }

    @Override
    public void onCompletion(BetterVideoPlayer player) {
        //Log.i(TAG, "Completed");
    }

    @Override
    public void onToggleControls(BetterVideoPlayer player, boolean isShowing) {
        //Log.i(TAG, "Controls toggled " + isShowing);
    }
}