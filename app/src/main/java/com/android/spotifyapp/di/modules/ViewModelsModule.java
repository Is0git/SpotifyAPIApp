package com.android.spotifyapp.di.modules;

import androidx.lifecycle.ViewModelProviders;

import com.android.spotifyapp.data.ViewModels.HomeViewModel;
import com.android.spotifyapp.data.ViewModels.MyPlaylistViewModel;
import com.android.spotifyapp.data.ViewModels.YoutubePlayerViewmodel;
import com.android.spotifyapp.di.scopes.YoutubeScope;
import com.android.spotifyapp.ui.fragment.HomeFragment;
import com.android.spotifyapp.ui.fragment.YoutubeFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModelsModule {
    private HomeFragment homeFragment;
    private YoutubeFragment youtubeFragment;


    public ViewModelsModule(HomeFragment homeFragment, YoutubeFragment youtubeFragment) {
        this.homeFragment = homeFragment;
        this.youtubeFragment = youtubeFragment;
    }

    @Provides
    public HomeViewModel homeViewModel() {
        return ViewModelProviders.of(homeFragment).get(HomeViewModel.class);
    }

    @Provides
    public MyPlaylistViewModel myPlaylistViewModel() {
        return ViewModelProviders.of(homeFragment).get(MyPlaylistViewModel.class);

    }

    @Provides
    @YoutubeScope
    public YoutubePlayerViewmodel youtubePlayerViewmodel() {
        return ViewModelProviders.of(youtubeFragment).get(YoutubePlayerViewmodel.class);
    }

}
