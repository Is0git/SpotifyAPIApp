package com.android.spotifyapp.di.modules;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.android.spotifyapp.di.qualifiers.ActivityContext;
import com.android.spotifyapp.di.scopes.HomeFragmentScope;

import javax.inject.Singleton;

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
