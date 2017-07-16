/*
 * Copyright 2015.  Emin Yahyayev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ewintory.udacity.popularmovies.data.api;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module(complete = false, library = true)
public final class ServerConfigModule {
    public static final String SERVER_CONFIG_URL = "abc.com";//http://13.76.179.224:3000/api/v3";//"http://api.themoviedb.org/3";

    @Provides @Singleton @Named("SERVER_CONFIG") Endpoint provideEndpoint() {
        return Endpoints.newFixedEndpoint(SERVER_CONFIG_URL);
    }

    @Provides @Singleton @Named("SERVER_CONFIG") OkHttpClient provideApiClient(OkHttpClient client) {
        return client.clone();
    }

    @Provides @Singleton @Named("SERVER_CONFIG") RestAdapter provideRestAdapter(@Named("SERVER_CONFIG") Endpoint endpoint, @Named("SERVER_CONFIG") OkHttpClient client, Gson gson) {
        return new RestAdapter.Builder()
                .setClient(new OkClient(client))
                .setEndpoint(endpoint)
                .setLogLevel(RestAdapter.LogLevel.NONE)
                //.setRequestInterceptor(request -> request.addQueryParam("api_key", BuildConfig.MOVIE_DB_API_KEY))
                .setConverter(new GsonConverter(gson))
                .build();
    }
    @Provides @Singleton
    ServerConfigApi provideServerConfigApi(@Named("SERVER_CONFIG") RestAdapter restAdapter)
    {
        return restAdapter.create(ServerConfigApi.class);
    }
}
