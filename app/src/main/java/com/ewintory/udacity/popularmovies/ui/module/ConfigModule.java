package com.ewintory.udacity.popularmovies.ui.module;

import com.ewintory.udacity.popularmovies.ApplicationModule;
import com.ewintory.udacity.popularmovies.ui.fragment.LoginFragment;
import dagger.Module;

@Module(
        injects = {
                LoginFragment.class
        },
        addsTo = ApplicationModule.class
)
public final class ConfigModule {}