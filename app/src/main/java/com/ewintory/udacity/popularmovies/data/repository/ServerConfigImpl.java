package com.ewintory.udacity.popularmovies.data.repository;

import com.ewintory.udacity.popularmovies.data.api.ServerConfigApi;
import com.ewintory.udacity.popularmovies.data.model.Genre;

import java.util.Map;

import rx.Observable;

/**
 * Created by Hung Hoang on 7/16/2017.
 */

final class ServerConfigImpl implements ServerConfig {

    private final ServerConfigApi serverApi;

    public ServerConfigImpl(ServerConfigApi moviesApi) {
        serverApi = moviesApi;
    }

    @Override
    public Observable<Map<Integer, Genre>> genres() {
        return null;
    }
}
