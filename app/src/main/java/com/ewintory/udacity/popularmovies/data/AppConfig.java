package com.ewintory.udacity.popularmovies.data;

import java.io.Serializable;
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

    public ArrayList<Promotion> getPromotions() {
        return promotions;
    }

    private ArrayList<Promotion> promotions;

    public Force getForce() {
        return force;
    }

    public String getOs_type() {
        return os_type;
    }

    public String getSubtile_server() {
        return subtile_server;
    }

    public boolean is_showTrailer() {
        return is_showTrailer;
    }

    public boolean is_show_ads() {
        return is_show_ads;
    }

    public boolean is_show_full_screen_ads() {
        return is_show_full_screen_ads;
    }

    public Admob getAdmob() {
        return admob;
    }


    public class Admob
    {
        private String banner;
        private String interstitial;
        private String rewardVideo;
        private String native_listMovie;
        private String native_detailMovie;

        public String getBanner() {
            return banner;
        }

        public String getInterstitial() {
            return interstitial;
        }

        public String getRewardVideo() {
            return rewardVideo;
        }

        public String getNative_listMovie() {
            return native_listMovie;
        }

        public String getNative_detailMovie() {
            return native_detailMovie;
        }
    }

    public class Force implements Serializable
    {
        private String package_name;
        private String external_link;
        private boolean keep_current_version;

        public String getPackage_name() {
            return package_name;
        }

        public String getExternal_link() {
            return external_link;
        }

        public boolean isKeep_current_version() {
            return keep_current_version;
        }
    }

    public class Promotion implements Serializable
    {
        private String imgUrl;
        private String package_name;

        public String getImgUrl() {
            return imgUrl;
        }

        public String getPackage_name() {
            return package_name;
        }
    }
}
