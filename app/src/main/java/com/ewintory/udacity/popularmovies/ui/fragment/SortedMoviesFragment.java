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

package com.ewintory.udacity.popularmovies.ui.fragment;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ewintory.udacity.popularmovies.R;
import com.ewintory.udacity.popularmovies.data.api.Sort;
import com.ewintory.udacity.popularmovies.data.model.Genre;
import com.ewintory.udacity.popularmovies.data.model.Movie;
import com.ewintory.udacity.popularmovies.data.provider.MoviesContract;
import com.ewintory.udacity.popularmovies.ui.activity.BrowseMoviesActivity;
import com.ewintory.udacity.popularmovies.ui.activity.MovieDetailsActivity;
import com.ewintory.udacity.popularmovies.ui.adapter.MoviesAdapter;
import com.ewintory.udacity.popularmovies.ui.listener.EndlessScrollListener;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

public final class SortedMoviesFragment extends MoviesFragment implements EndlessScrollListener.OnLoadMoreCallback {
    private static final String ARG_SORT = "state_sort";
    private static final String ARG_GENREID = "state_genreID";
    private static final String ARG_GENRE_NAME = "state_genreID_Name";

    private static final String STATE_CURRENT_PAGE = "state_current_page";
    private static final String STATE_IS_LOADING = "state_is_loading";

    private static final int VISIBLE_THRESHOLD = 10;

    @Inject ContentResolver mContentResolver;

    private int mCurrentAdLoadedID = 0;

    private EndlessScrollListener mEndlessScrollListener;
    private BehaviorSubject<Observable<List<Movie>>> mItemsObservableSubject = BehaviorSubject.create();
    private Sort mSort;
    private int mGenre = -1;
    private String mGenreName = "noname";
    private int mCurrentPage = 0;
    private boolean mIsLoading = false;
    private int mLastAdInitIndex = 0;
    private boolean first_movie_loaded = false;
    private boolean isLoadingAds = false;
    private int mMaxPage = 0;
    private boolean m_firstItem = false;
    public static SortedMoviesFragment newInstance(@NonNull Sort sort) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SORT, sort);
        args.putInt(ARG_GENREID, -1);
        SortedMoviesFragment fragment = new SortedMoviesFragment();
        fragment.setArguments(args);


        return fragment;
    }

    public static SortedMoviesFragment newInstance(@NonNull int genreID,@NonNull String name) {
        Bundle args = new Bundle();
        args.putInt(ARG_GENREID, genreID);
        args.putString(ARG_GENRE_NAME, name);
        SortedMoviesFragment fragment = new SortedMoviesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSort = (Sort) getArguments().getSerializable(ARG_SORT);
        mGenre = (int)getArguments().getInt(ARG_GENREID);
        mGenreName = (String)getArguments().getString(ARG_GENRE_NAME);

        trySetupToolbar();
        if(mGenre >= 0)
        {
            if (mToolbar != null) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().show();
                mToolbar.setTitle(mGenreName);
            }
        }
        else
        {
            if (mToolbar != null) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
            }
        }
        if (savedInstanceState != null) {
            mCurrentPage = savedInstanceState.getInt(STATE_CURRENT_PAGE, 0);
            mIsLoading = savedInstanceState.getBoolean(STATE_IS_LOADING, true);
            Timber.d(String.format("Restoring state: pages 1-%d, was loading - %s", mCurrentPage, mIsLoading));
        }

        mMoviesAdapter.setLoadMore(true);
        mViewAnimator.setDisplayedChildId((mCurrentPage == 0) ? ANIMATOR_VIEW_LOADING : ANIMATOR_VIEW_CONTENT);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // subscribe to global favored changes in order to synchronise movies from different views
        mSubscriptions.add(mHelper.getFavoredObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    int count = mMoviesAdapter.getItemCount();
                    for (int position = 0; position < count; position++) {
                        if (mMoviesAdapter.getItemId(position) == event.movieId) {
                            mMoviesAdapter.notifyItemChanged(position);
                        }
                    }
                }));
        subscribeToGenre();
        if (savedInstanceState == null)
            reloadContent();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_CURRENT_PAGE, mCurrentPage);
        outState.putBoolean(STATE_IS_LOADING, mIsLoading);
        outState.putSerializable(ARG_SORT, mSort);
        outState.putSerializable(ARG_GENREID, mGenre);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        if (mSort != null || mGenre >= 0) reloadContent();
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        if (mMoviesAdapter.isLoadMore())
            pullPage(page);
    }

    protected final void reloadContent() {
        if (!mSwipeRefreshLayout.isRefreshing())
            mViewAnimator.setDisplayedChildId(ANIMATOR_VIEW_LOADING);

        mSelectedPosition = -1;
        isLoadingAds = false;
        mCurrentAdLoadedID = 0;
        reAddOnScrollListener(mGridLayoutManager, mCurrentPage = 0);
        pullPage(1);
    }

    private void subscribeToGenre() {
        Timber.d("Subscribing to genre items");
        mSubscriptions.add(mGenresRepository.discoveryGenres()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(genres -> {
                    for(int i = 0 ; i < genres.size() ; i++) {
                        AsyncQueryHandler handler = new AsyncQueryHandler(mContentResolver) {
                        };
                        handler.startInsert(-1, null, MoviesContract.Genres.CONTENT_URI, new Genre.Builder()
                                .genre(genres.get(i))
                                .build());
                    }
                    subscribeToMovies();
                }, throwable -> {
                    Timber.e(throwable, "Genre loading failed.");
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (mViewAnimator.getDisplayedChildId() == ANIMATOR_VIEW_CONTENT) {
                        mMoviesAdapter.setLoadMore(false);
                        Toast.makeText(getActivity(), R.string.view_error_message, Toast.LENGTH_SHORT).show();
                    } else
                        mViewAnimator.setDisplayedChildId(ANIMATOR_VIEW_ERROR);
                }));
    }

    private void subscribeToMovies() {
        Timber.d("Subscribing to items");
        mSubscriptions.add(Observable.concat(mItemsObservableSubject)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mCurrentPage++;
                    if(mMaxPage > mCurrentPage)
                        mMaxPage = mCurrentPage;

                    Timber.d(String.format("Page %d is loaded, %d new items", mCurrentPage, movies.size()));
                    if (mCurrentPage == 1) mMoviesAdapter.clear();

                    mMoviesAdapter.setLoadMore(!movies.isEmpty());
                    for(int i = 0 ; i < movies.size(); i++)
                    {
                        if(mMoviesAdapter.getItems().size() >= MoviesAdapter.START_AD_INDEX && ((mMoviesAdapter.getItems().size() - MoviesAdapter.START_AD_INDEX ) % MoviesAdapter.ITEMS_PER_AD == 0))
                        {
                            final NativeExpressAdView adView = new NativeExpressAdView(getActivity());
                            mMoviesAdapter.addAd(mMoviesAdapter.getItems().size(),adView);

                            Movie adMarkMovie = new Movie();
                            adMarkMovie.setId(-1000);
                            mMoviesAdapter.add(adMarkMovie);
                            m_firstItem = true;
                        }
                        mMoviesAdapter.add(movies.get(i));
                    }

                    mViewAnimator.setDisplayedChildId(ANIMATOR_VIEW_CONTENT);

                    if(!movies.isEmpty() && mCurrentPage >= mMaxPage && !isLoadingAds && mCurrentAdLoadedID < mMoviesAdapter.mAdItems.size())
                    {
                        setUpAndLoadNativeExpressAds(mCurrentAdLoadedID);
                    }

                }, throwable -> {
                    Timber.e(throwable, "Movies loading failed.");
                    mSwipeRefreshLayout.setRefreshing(false);
                    if (mViewAnimator.getDisplayedChildId() == ANIMATOR_VIEW_CONTENT) {
                        mMoviesAdapter.setLoadMore(false);
                        Toast.makeText(getActivity(), R.string.view_error_message, Toast.LENGTH_SHORT).show();
                    } else
                        mViewAnimator.setDisplayedChildId(ANIMATOR_VIEW_ERROR);
                }));
    }


    private void setUpAndLoadNativeExpressAds(int currentAdLoadedID) {
        // Use a Runnable to ensure that the RecyclerView has been laid out before setting the
        // ad size for the Native Express ad. This allows us to set the Native Express ad's
        // width to match the full width of the RecyclerView.

        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                final float scale = getActivity().getResources().getDisplayMetrics().density;
                // Set the ad size and ad unit ID for each Native Express ad in the items list.
                isLoadingAds = true;
                Object[] keys = mMoviesAdapter.mAdItems.keySet().toArray();

                mLastAdInitIndex = keys.length;

                for(int i = currentAdLoadedID ; i < keys.length ; i ++)
                {
                    final NativeExpressAdView adView =
                            (NativeExpressAdView)mMoviesAdapter.mAdItems.get(keys[i]);
                    final CardView cardView = (CardView) getActivity().findViewById(R.id.ad_movie_item);
                    if(cardView != null) {
                        final int adWidth = cardView.getWidth() - cardView.getPaddingLeft()
                                - cardView.getPaddingRight();
                        AdSize adSize = new AdSize((int) (adWidth / scale), 150);
                        adView.setAdSize(adSize);
                        adView.setAdUnitId(getActivity().getString(R.string.ad_unit_id_native_express_movie_item));
                    }
                    else
                    {
                        Log.d("HUNG_TAG","INIT_AD_VIEW _ FAILED " + keys[i].toString());
                        mLastAdInitIndex = keys.length - 1;
                        return;
                    }
                    Log.d("HUNG_TAG","INIT_AD_VIEW " + keys[i].toString());
                }
                // Load the first Native Express ad in the items list.
                loadNativeExpressAd(mCurrentAdLoadedID);
            }
        });
    }

    private void loadNativeExpressAd(final int index) {
        if(index >= mLastAdInitIndex) {
            isLoadingAds = false;
            return;
        }

        Object[] keys = mMoviesAdapter.mAdItems.keySet().toArray();
        isLoadingAds = true;
        int key = (Integer)keys[index];

        Log.d("HUNG_TAG","LOAD_AD_VIEW " + keys[index].toString());

        Object item = mMoviesAdapter.mAdItems.get(key);

        if (!(item instanceof NativeExpressAdView)) {
            throw new ClassCastException("Expected item at index " + key + " to be a Native"
                    + " Express ad.");
        }

        final NativeExpressAdView adView = (NativeExpressAdView) item;
        if(adView.getAdSize().getWidth() > 0) {
            // Set an AdListener on the NativeExpressAdView to wait for the previous Native Express ad
            // to finish loading before loading the next ad in the items list.
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    // The previous Native Express ad loaded successfully, call this method again to
                    // load the next ad in the items list.
                    mCurrentAdLoadedID++;
                    loadNativeExpressAd(mCurrentAdLoadedID);

                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // The previous Native Express ad failed to load. Call this method again to load
                    // the next ad in the items list.
                    Log.e("MainActivity", "The previous Native Express ad failed to load. Attempting to"
                            + " load the next Native Express ad in the items list.");
                    mCurrentAdLoadedID++;
                    loadNativeExpressAd(mCurrentAdLoadedID);

                }
            });

            // Load the Native Express ad.
            adView.loadAd(new AdRequest.Builder().build());
        }
        else {
            Log.d("HUNG_TAG","why not set size");
        }

    }


    private void pullPage(int page) {
        Timber.d(String.format("Page %d is loading.", page));
        if(mGenre == -1)
            mItemsObservableSubject.onNext(mMoviesRepository.discoverMovies(mSort, page));
        else
            mItemsObservableSubject.onNext(mMoviesRepository.discoverMoviesByGenre(mGenre,page));
    }

    @Override
    protected void initRecyclerView() {
        super.initRecyclerView();
        reAddOnScrollListener(mGridLayoutManager, mCurrentPage);
    }

    private void reAddOnScrollListener(GridLayoutManager layoutManager, int startPage) {
        if (mEndlessScrollListener != null)
            mRecyclerView.removeOnScrollListener(mEndlessScrollListener);

        mEndlessScrollListener = EndlessScrollListener.fromGridLayoutManager(layoutManager, VISIBLE_THRESHOLD, startPage).setCallback(this);
        mRecyclerView.addOnScrollListener(mEndlessScrollListener);
    }

}

