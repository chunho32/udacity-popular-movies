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
    private static final String TEST_URL = "https://r8---sn-8pxuuxa-nbole.googlevideo.com/videoplayback?id=c5174c05cabf8e82&itag=22&source=webdrive&requiressl=yes&pl=24&ttl=transient&ei=j1hwWd3QCtS-qQWrkqrIBg&susci=o-AKuqjbp9WjEBl7e_4qkkG8J6aaooIQidyezVlLbyMkKl9HoT&mime=video%2Fmp4&lmt=1485076568300340&ip=2600%3A3c01%3A%3Af03c%3A91ff%3Afe60%3A4f78&ipbits=0&expire=1500549327&sparams=api,ei,expire,id,ip,ipbits,ipbypass,itag,lmt,mime,mip,mm,mn,ms,mv,pl,requiressl,source,susci,ttl&signature=24FE7FDFF611996069069F245838883A08F817A8.3F0D9A21C08030BD0F7F1010C6EC7E5D7437E81D&api=68990F9068BF3D8E4D51BC3C9D6DE&key=cms1&app=storage&sck=U2FsdGVkX1%2F2CfnEOvF5hKM1EQKsBaFeZav8jVGKdhM%3D&redirect_counter=1&req_id=d7a366857cc436e2&cms_redirect=yes&ipbypass=yes&mip=27.74.241.248&mm=31&mn=sn-8pxuuxa-nbole&ms=au&mt=1500539743&mv=m";

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