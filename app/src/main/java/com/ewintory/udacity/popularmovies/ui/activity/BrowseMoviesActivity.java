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

package com.ewintory.udacity.popularmovies.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ewintory.udacity.popularmovies.R;
import com.ewintory.udacity.popularmovies.data.AppConfig;
import com.ewintory.udacity.popularmovies.data.api.ApiModule;
import com.ewintory.udacity.popularmovies.data.api.Sort;
import com.ewintory.udacity.popularmovies.data.model.Genre;
import com.ewintory.udacity.popularmovies.data.model.Movie;
import com.ewintory.udacity.popularmovies.ui.fragment.FavoredMoviesFragment;
import com.ewintory.udacity.popularmovies.ui.fragment.LeftMenuFragment;
import com.ewintory.udacity.popularmovies.ui.fragment.MovieFragment;
import com.ewintory.udacity.popularmovies.ui.fragment.MoviesFragment;
import com.ewintory.udacity.popularmovies.ui.fragment.SortedMoviesFragment;
import com.ewintory.udacity.popularmovies.utils.PrefUtils;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public final class BrowseMoviesActivity extends BaseActivity implements MoviesFragment.Listener , LeftMenuFragment.OnListFragmentInteractionListener {
    private static final String SORT_MODE = "state_mode";

    private static final String MOVIES_FRAGMENT_TAG = "fragment_movies";
    private static final String UPDATE_APP_FRAGMENT_TAG = "fragment_update";
    private static final String MOVIE_DETAILS_FRAGMENT_TAG = "fragment_movie_details";
    private static final String GENRES_FRAGMENT_TAG = "fragment_genres";
    public static final String MODE_FAVORITES = "favorites";

    private ModeSpinnerAdapter mSpinnerAdapter = new ModeSpinnerAdapter();
    private MoviesFragment mMoviesFragment;
    private String mSortMode;
    private boolean mTwoPane;


    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerList;
    private LeftMenuFragment mLeftViewFragment;
    private BottomBar bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_movies);

        mTwoPane = false;//findViewById(R.id.movie_details_container) != null;

        mSortMode = (savedInstanceState != null) ?
                savedInstanceState.getString(SORT_MODE, Sort.POPULARITY.toString())
                : PrefUtils.getBrowseMoviesMode(this);

        //initModeSpinner();
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    onModeSelected(Sort.POPULARITY.toString());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(Color.parseColor("#ff546e7a"));
                    }
                }
                else if (tabId == R.id.tab_Genres) {
                    onGenresTabSelected(28,"Action");
                    //mDrawerLayout.openDrawer(Gravity.LEFT);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(Color.parseColor("#ff4e342e"));
                    }
                }
                else if (tabId == R.id.tab_news) {
                    onModeSelected(Sort.REVENUE.toString());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(Color.parseColor("#ff006064"));
                    }
                }
                else if (tabId == R.id.tab_favorite) {
                    onFavoriteTabSelected();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        window.setStatusBarColor(Color.parseColor("#fffb8c00"));
                    }
                }
            }
        });

        if (mToolbar != null) {
            ViewCompat.setElevation(mToolbar, getResources().getDimension(R.dimen.toolbar_elevation));
            mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0.5f,getResources().getColor(R.color.theme_primary) ));
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            });

            mToolbar.setNavigationIcon(R.drawable.genre_list);
            ActionBar ab = getSupportActionBar();
            if (ab != null) {
                ab.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
                //ab.setDisplayHomeAsUpEnabled(true);
                ab.setDisplayShowHomeEnabled(true);
            }
        }
        //left menu
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        createGenresFragment(new LeftMenuFragment());

        checkPromotion();
    }

    public void checkPromotion()
    {
        ArrayList<AppConfig.Promotion> promotions = ApiModule.appConfig.getPromotions();
        if(promotions != null && promotions.size() > 0) {
            final Dialog dialog = new Dialog(BrowseMoviesActivity.this,R.style.PauseDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog_update);

//            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
//            text.setText("New version is available! Update now ?");

            Button dialogCancelButton = (Button) dialog.findViewById(R.id.update_cancel_dialog_btn);
            dialogCancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });

            Button dialogOkButton = (Button) dialog.findViewById(R.id.update_ok_dialog_btn);
            dialogOkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            dialog.show();
        }
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mMoviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentByTag(MOVIES_FRAGMENT_TAG);
        if (mMoviesFragment == null)
            replaceMoviesFragment(mSortMode.equals(MODE_FAVORITES)
                    ? new FavoredMoviesFragment()
                    : SortedMoviesFragment.newInstance(Sort.fromString(mSortMode)));
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface
                        .OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BrowseMoviesActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_browse_movies, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                mMoviesFragment.onRefresh();
                break;
            case R.id.menu_scroll_to_top:
                mMoviesFragment.scrollToTop(true);
                break;
            case R.id.menu_settings:
                startActivity(new Intent(this, PreferenceActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT_MODE, mSortMode);
    }

    @Override
    protected void onPause() {
        PrefUtils.setBrowseMoviesMode(this, mSortMode);
        super.onPause();
    }

    @Override
    public void onMovieSelected(@NonNull Movie movie, View view) {
        Timber.d(String.format("Movie '%s' selected", movie.getTitle()));

        if (mTwoPane) {
            MovieFragment fragment = MovieFragment.newInstance(movie);
            replaceMovieDetailsFragment(fragment);
        } else {
            Intent intent = new Intent(this, MovieDetailsActivity.class);
            intent.putExtra(MovieDetailsActivity.EXTRA_MOVIE, movie);
            startActivity(intent);
        }
    }

    private void replaceMovieDetailsFragment(MovieFragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movie_details_container, fragment, MOVIE_DETAILS_FRAGMENT_TAG)
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .commit();
    }

    private void replaceMoviesFragment(MoviesFragment fragment) {
        mMoviesFragment = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movies_container, fragment, MOVIES_FRAGMENT_TAG)
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .commit();
    }

    private void createGenresFragment(LeftMenuFragment fragment) {
        mLeftViewFragment = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.left_nativation_listview, fragment, GENRES_FRAGMENT_TAG)
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .commit();
    }
    private void initModeSpinner() {
        Toolbar toolbar = getToolbar();
        if (toolbar == null)
            return;

        mSpinnerAdapter.clear();
        mSpinnerAdapter.addItem(MODE_FAVORITES, getString(R.string.mode_favored), false);
        mSpinnerAdapter.addHeader(getString(R.string.mode_sort));
        mSpinnerAdapter.addItem(Sort.POPULARITY.toString(), getString(R.string.mode_sort_popularity), false);
        mSpinnerAdapter.addItem(Sort.VOTE_COUNT.toString(), getString(R.string.mode_sort_vote_count), false);
        mSpinnerAdapter.addItem(Sort.VOTE_AVERAGE.toString(), getString(R.string.mode_sort_vote_average), false);

        int itemToSelect = -1;

        if (mSortMode.equals(MODE_FAVORITES))
            itemToSelect = 0;
        else if (mSortMode.equals(Sort.POPULARITY.toString()))
            itemToSelect = 2;
        else if (mSortMode.equals(Sort.VOTE_COUNT.toString()))
            itemToSelect = 3;
        else if (mSortMode.equals(Sort.VOTE_AVERAGE.toString()))
            itemToSelect = 4;

        View spinnerContainer = LayoutInflater.from(this).inflate(R.layout.widget_toolbar_spinner, toolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        toolbar.addView(spinnerContainer, lp);

        Spinner spinner = (Spinner) spinnerContainer.findViewById(R.id.mode_spinner);
        spinner.setAdapter(mSpinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> spinner, View view, int position, long itemId) {
                onModeSelected(mSpinnerAdapter.getMode(position));
            }

            @Override public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        if (itemToSelect >= 0) {
            Timber.d("Restoring item selection to mode spinner: " + itemToSelect);
            spinner.setSelection(itemToSelect);
        }
    }

    private void onModeSelected(String mode) {
        mSortMode = mode;
        replaceMoviesFragment(SortedMoviesFragment.newInstance(Sort.fromString(mSortMode)));
    }

    private void onFavoriteTabSelected()
    {
        replaceMoviesFragment(new FavoredMoviesFragment());
    }
    private void onGenresTabSelected(int genre,String name)
    {
        replaceMoviesFragment(SortedMoviesFragment.newInstance(genre,name));
    }

    @Override
    public void onListFragmentInteraction(Genre item) {
        onGenresTabSelected(item.getId(),item.getName());
        mDrawerLayout.closeDrawers();
        bottomBar.setVisibility(View.VISIBLE);
    }

    private class ModeSpinnerItem {
        boolean isHeader;
        String mode, title;
        boolean indented;

        ModeSpinnerItem(boolean isHeader, String mode, String title, boolean indented) {
            this.isHeader = isHeader;
            this.mode = mode;
            this.title = title;
            this.indented = indented;
        }
    }

    private class ModeSpinnerAdapter extends BaseAdapter {

        private ModeSpinnerAdapter() { }

        private ArrayList<ModeSpinnerItem> mItems = new ArrayList<ModeSpinnerItem>();

        public void clear() {
            mItems.clear();
        }

        public void addItem(String tag, String title, boolean indented) {
            mItems.add(new ModeSpinnerItem(false, tag, title, indented));
        }

        public void addHeader(String title) {
            mItems.add(new ModeSpinnerItem(true, "", title, false));
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private boolean isHeader(int position) {
            return position >= 0 && position < mItems.size()
                    && mItems.get(position).isHeader;
        }

        @Override
        public View getDropDownView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("DROPDOWN")) {
                view = getLayoutInflater().inflate(R.layout.item_toolbar_spinner_dropdown,
                        parent, false);
                view.setTag("DROPDOWN");
            }

            TextView headerTextView = (TextView) view.findViewById(R.id.header_text);
            View dividerView = view.findViewById(R.id.divider_view);
            TextView normalTextView = (TextView) view.findViewById(android.R.id.text1);

            if (isHeader(position)) {
                headerTextView.setText(getTitle(position));
                headerTextView.setVisibility(View.VISIBLE);
                normalTextView.setVisibility(View.GONE);
                dividerView.setVisibility(View.VISIBLE);
            } else {
                headerTextView.setVisibility(View.GONE);
                normalTextView.setVisibility(View.VISIBLE);
                dividerView.setVisibility(View.GONE);

                setUpNormalDropdownView(position, normalTextView);
            }

            return view;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals("NON_DROPDOWN")) {
                view = getLayoutInflater().inflate(R.layout.item_toolbar_spinner, parent, false);
                view.setTag("NON_DROPDOWN");
            }
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getTitle(position));
            return view;
        }

        private String getTitle(int position) {
            return position >= 0 && position < mItems.size() ? mItems.get(position).title : "";
        }

        private String getMode(int position) {
            return position >= 0 && position < mItems.size() ? mItems.get(position).mode : "";
        }

        private void setUpNormalDropdownView(int position, TextView textView) {
            textView.setText(getTitle(position));
        }

        @Override
        public boolean isEnabled(int position) {
            return !isHeader(position);
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

}
