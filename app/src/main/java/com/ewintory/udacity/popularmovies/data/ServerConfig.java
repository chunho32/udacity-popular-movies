package com.ewintory.udacity.popularmovies.data;

/**
 * Created by Hung Hoang on 7/17/2017.
 */

public class ServerConfig {

    private String server_url;
    private boolean in_review;
    private boolean is_active;

    public boolean is_active() {
        return is_active;
    }

    public String getServer_url() {
        return server_url;
    }

    public boolean isIn_review() {
        return in_review;
    }

    @Override
    public String toString()
    {
        return server_url + in_review + is_active;
    }
}
