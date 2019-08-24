package com.android.spotifyapp.di.modules;

import com.android.spotifyapp.ui.adapters.HomeHorizontal;
import com.android.spotifyapp.ui.adapters.MyPlaylistsAdapter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public class AdaptersModule {
    @Provides
    public HomeHorizontal homeHorizontal() {
        return new HomeHorizontal();
    }

    @Provides
    public MyPlaylistsAdapter myPlaylistsAdapter() {
        return new MyPlaylistsAdapter();
    }

}
