package com.android.spotifyapp.di.modules;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.spotifyapp.R;
import com.android.spotifyapp.di.qualifiers.HomeHorizontalAdapter;
import com.android.spotifyapp.di.qualifiers.MyPlaylistListQualifier;
import com.android.spotifyapp.di.qualifiers.RecentlyPlayedQualifier;
import com.android.spotifyapp.di.qualifiers.RecommendedListQualifier;
import com.android.spotifyapp.ui.adapters.Home.HomeHorizontal;
import dagger.Module;
import dagger.Provides;

@Module(includes = {ContextModule.class})
public class HorizontalRecyclerView {
    private View view;

    public HorizontalRecyclerView(View view) {
        this.view = view;
    }

    @Provides
    RecyclerView.LayoutManager layoutManager() {
        return new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false);
    }


    @Provides
    @RecentlyPlayedQualifier
    RecyclerView recyclerView(RecyclerView.LayoutManager layoutManager) {
        RecyclerView recyclerView;
        recyclerView = view.findViewById(R.id.recently_played_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        return recyclerView;
    }
    @Provides
    @MyPlaylistListQualifier
    RecyclerView MyPlaylistRecyclerView(RecyclerView.LayoutManager layoutManager) {
        RecyclerView recyclerView;
        recyclerView = view.findViewById(R.id.MyPlaylists_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        return recyclerView;
    }

    @Provides
    @RecommendedListQualifier
    RecyclerView RecommendedRecyclerView(RecyclerView.LayoutManager layoutManager) {
        RecyclerView recyclerView;
        recyclerView = view.findViewById(R.id.recommended_list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        return recyclerView;
    }

}
