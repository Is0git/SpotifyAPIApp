package com.android.spotifyapp.di.components;

import androidx.recyclerview.widget.RecyclerView;

import com.android.spotifyapp.data.repositories.HomeRepository;
import com.android.spotifyapp.di.modules.HomeModule;
import com.android.spotifyapp.di.modules.HorizontalRecyclerView;
import com.android.spotifyapp.di.qualifiers.HomeHorizontalAdapter;
import com.android.spotifyapp.di.scopes.HomeFragmentScope;
import com.android.spotifyapp.ui.fragment.HomeFragment;
import com.google.gson.annotations.SerializedName;

import javax.inject.Singleton;

import dagger.Component;
@HomeFragmentScope
@Component(modules = {HorizontalRecyclerView.class}, dependencies = {AppComponent.class})

public interface HomeComponent {

    RecyclerView getList();
    @HomeHorizontalAdapter
    RecyclerView.Adapter getAdapter();
    void inject(HomeRepository homeRepository);

}
