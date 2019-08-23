package com.android.spotifyapp.di.modules;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.spotifyapp.R;
import com.android.spotifyapp.di.qualifiers.HomeHorizontalAdapter;
import com.android.spotifyapp.di.scopes.HomeFragmentScope;
import com.android.spotifyapp.ui.adapters.HomeHorizontal;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ContextModule.class})
public class HorizontalRecyclerView {
    private View view;

    public HorizontalRecyclerView(View view) {
        this.view = view;
    }

    @Provides
    @HomeFragmentScope
    RecyclerView.LayoutManager layoutManager() {
        return new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false);
    }
    @Provides
    @HomeFragmentScope
    @HomeHorizontalAdapter
        RecyclerView.Adapter adapter() {
        return new HomeHorizontal();
    }
    @Provides
    @HomeFragmentScope
    RecyclerView recyclerView(RecyclerView.LayoutManager layoutManager, @HomeHorizontalAdapter RecyclerView.Adapter adapter) {
        RecyclerView recyclerView;
        recyclerView = view.findViewById(R.id.recently_played_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }


}
