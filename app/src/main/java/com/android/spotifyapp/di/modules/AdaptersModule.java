package com.android.spotifyapp.di.modules;

import com.android.spotifyapp.di.scopes.HomeFragmentScope;
import com.android.spotifyapp.ui.adapters.Home.HomeHorizontal;
import com.android.spotifyapp.ui.adapters.Home.MyPlaylistsAdapter;
import com.android.spotifyapp.ui.adapters.Home.RecommendedAdapter;
import com.android.spotifyapp.ui.adapters.Home.SliderAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class AdaptersModule {
    @Provides
    @HomeFragmentScope
    public HomeHorizontal homeHorizontal() {
        return new HomeHorizontal();
    }

    @Provides
    @HomeFragmentScope
    public MyPlaylistsAdapter myPlaylistsAdapter() {
        return new MyPlaylistsAdapter();
    }

    @Provides
    @HomeFragmentScope
    public RecommendedAdapter recommendedAdapter() {
        return new RecommendedAdapter();
    }

    @Provides
    @HomeFragmentScope
    public SliderAdapter sliderAdapter() {return new SliderAdapter(); }

}
