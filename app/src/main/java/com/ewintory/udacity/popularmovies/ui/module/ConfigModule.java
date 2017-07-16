package com.ewintory.udacity.popularmovies.ui.module;

import com.ewintory.udacity.popularmovies.ApplicationModule;
import com.ewintory.udacity.popularmovies.ui.fragment.SplashFragment;

import dagger.Module;

@Module(
        injects = {
                SplashFragment.class
        },
        addsTo = ApplicationModule.class
)
public final class ConfigModule {}