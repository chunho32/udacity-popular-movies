package com.ewintory.udacity.popularmovies.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewintory.udacity.popularmovies.R;
import com.ewintory.udacity.popularmovies.data.repository.ServerConfig;
import com.ewintory.udacity.popularmovies.ui.module.ConfigModule;
import com.ewintory.udacity.popularmovies.ui.module.MoviesModule;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * Created by hunghm on 7/14/17.
 */

public class SplashFragment extends BaseFragment {

    private Subscription mFavoritesSubscription = Subscriptions.empty();

    @Inject
    ServerConfig mServerConfig;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected List<Object> getModules() {
        return Collections.<Object>singletonList(new ConfigModule());
    }
}

