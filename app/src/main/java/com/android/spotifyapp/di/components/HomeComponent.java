package com.android.spotifyapp.di.components;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.android.spotifyapp.data.repositories.HomeRepository;
import com.android.spotifyapp.di.modules.AdaptersModule;
import com.android.spotifyapp.di.modules.HorizontalRecyclerView;
import com.android.spotifyapp.di.modules.ViewModelsModule;
import com.android.spotifyapp.di.qualifiers.HomeHorizontalAdapter;
import com.android.spotifyapp.di.qualifiers.MyPlaylistListQualifier;
import com.android.spotifyapp.di.qualifiers.RecentlyPlayedQualifier;
import com.android.spotifyapp.di.scopes.HomeFragmentScope;
import com.android.spotifyapp.ui.fragment.HomeFragment;


import dagger.Component;
@HomeFragmentScope
@Component(modules = {HorizontalRecyclerView.class, AdaptersModule.class, ViewModelsModule.class}, dependencies = {AppComponent.class})

public interface HomeComponent {
    @RecentlyPlayedQualifier
    RecyclerView getList();
    @MyPlaylistListQualifier
    RecyclerView getPlaylistList();
    @HomeHorizontalAdapter
    RecyclerView.Adapter getAdapter();
    void inject(HomeRepository homeRepository);
    void injectFragment(HomeFragment homeFragment);


}
