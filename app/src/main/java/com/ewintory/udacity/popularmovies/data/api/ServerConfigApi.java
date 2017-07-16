package com.ewintory.udacity.popularmovies.data.api;

import com.ewintory.udacity.popularmovies.data.model.ServerConfig;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by hunghm on 7/14/17.
 */

public interface ServerConfigApi {
    @GET("/")
    Observable<ServerConfig.Response> genres();
}
