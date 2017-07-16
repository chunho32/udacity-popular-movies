package com.ewintory.udacity.popularmovies.data.repository;

import com.ewintory.udacity.popularmovies.data.model.Genre;

import java.util.Map;

import rx.Observable;

/**
 * Created by Hung Hoang on 7/16/2017.
 */

public interface ServerConfig {
    Observable<Map<Integer, Genre>> genres();
}
