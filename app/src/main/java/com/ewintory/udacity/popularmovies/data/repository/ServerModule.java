package com.ewintory.udacity.popularmovies.data.repository;

import android.content.ContentResolver;

import com.ewintory.udacity.popularmovies.data.GlideSetup;
import com.ewintory.udacity.popularmovies.data.api.ApiModule;
import com.ewintory.udacity.popularmovies.data.api.ServerConfigApi;
import com.ewintory.udacity.popularmovies.data.api.ServerConfigModule;
import com.ewintory.udacity.popularmovies.data.provider.ProviderModule;
import com.squareup.sqlbrite.BriteContentResolver;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Hung Hoang on 7/16/2017.
 */


@Module(
        includes = {
                ServerConfigModule.class
        },
        injects = {
                GlideSetup.class
        },
        complete = false,
        library = true
)
public final class ServerModule {
    @Singleton
    @Provides
    public ServerConfig providesServerConfig(ServerConfigApi serverConfigApi) {
        return new ServerConfigImpl(serverConfigApi);
    }
}
