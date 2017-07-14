package com.ewintory.udacity.popularmovies.ui.fragment;

import android.support.annotation.IdRes;

import com.ewintory.udacity.popularmovies.ui.module.ConfigModule;
import com.ewintory.udacity.popularmovies.ui.module.MoviesModule;

import java.util.Collections;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;
import timber.log.Timber;

/**
 * Created by hunghm on 7/14/17.
 */

public class LoginFragment extends BaseFragment {

    private Subscription mFavoritesSubscription = Subscriptions.empty();

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected List<Object> getModules() {
        return Collections.<Object>singletonList(new ConfigModule());
    }
}

