package com.android.spotifyapp.di.modules;

import androidx.lifecycle.ViewModelProviders;

import com.android.spotifyapp.data.ViewModels.BaseViewModel;
import com.android.spotifyapp.data.ViewModels.HomeViewModel;
import com.android.spotifyapp.data.ViewModels.MyPlaylistViewModel;
import com.android.spotifyapp.ui.fragment.HomeFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModelsModule {
    private HomeFragment homeFragment;

    public ViewModelsModule(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    @Provides
    public HomeViewModel homeViewModel() {
        return ViewModelProviders.of(homeFragment).get(HomeViewModel.class);
    }

    @Provides
    public MyPlaylistViewModel myPlaylistViewModel() {
        return ViewModelProviders.of(homeFragment).get(MyPlaylistViewModel.class);

    }

}
