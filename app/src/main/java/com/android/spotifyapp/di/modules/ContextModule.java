package com.android.spotifyapp.di.modules;

import android.app.Activity;

import com.android.spotifyapp.di.qualifiers.ActivityContext;
import com.android.spotifyapp.di.scopes.HomeFragmentScope;

import dagger.Module;
import dagger.Provides;

@Module

public class ContextModule {
    private Activity context;

    public ContextModule(Activity context) {
        this.context = context;
    }

    @Provides
    @ActivityContext
    @HomeFragmentScope
    Activity getContext() {
        return this.context;
    }
}
