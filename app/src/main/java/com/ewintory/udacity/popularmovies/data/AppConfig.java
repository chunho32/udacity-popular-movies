package com.ewintory.udacity.popularmovies.data;

import java.util.ArrayList;

/**
 * Created by hoanghung on 7/28/17.
 */

public class AppConfig {
    private String os_type = "android";
    private String subtile_server;
    private boolean is_showTrailer;

    private boolean is_show_ads;
    private boolean is_show_full_screen_ads;
    private Admob admob;

    private Force force;
    private ArrayList<String> promote_package_names;

    private class Admob
    {
        private String banner;
        private String interstitial;
        private String rewardVideo;
        private String native_listMovie;
        private String native_detailMovie;
    }

    private class Force
    {
        private String package_name;
        private String external_link;
    }
}
