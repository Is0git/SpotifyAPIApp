package com.android.spotifyapp.di.modules;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.spotifyapp.R;
import com.android.spotifyapp.di.qualifiers.ActivityContext;
import com.android.spotifyapp.di.qualifiers.ArtistsAlbumsRecyclerViewQualifier;
import com.android.spotifyapp.di.qualifiers.GridLayoutQualifier;
import com.android.spotifyapp.di.qualifiers.MyPlaylistListQualifier;
import com.android.spotifyapp.di.qualifiers.RecentlyPlayedQualifier;
import com.android.spotifyapp.di.qualifiers.RecommendedListQualifier;
import com.android.spotifyapp.di.qualifiers.RelatedArtistsRecyclerViewQualifier;
import com.android.spotifyapp.di.qualifiers.TopSongRecyclerViewQualifier;
import com.android.spotifyapp.di.qualifiers.VerticalLayoutQualifier;

import dagger.Module;
import dagger.Provides;

@Module(includes = {ContextModule.class})
public class RecyclerViewModule {
    private View view;

    public RecyclerViewModule(View view) {
        this.view = view;
    }

    @Provides
    RecyclerView.LayoutManager layoutManager() {
        return new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false);
    }

    @Provides
    @VerticalLayoutQualifier
    RecyclerView.LayoutManager verticalLayoutManager() {
        return new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
    }

    @Provides
    @GridLayoutQualifier
    RecyclerView.LayoutManager gridLayout(@ActivityContext Context context) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
        gridLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        gridLayoutManager.setReverseLayout(false);
        return gridLayoutManager;
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

    @Provides
    @ArtistsAlbumsRecyclerViewQualifier
    RecyclerView ArtistAlbumRecyclerView(@GridLayoutQualifier RecyclerView.LayoutManager layoutManager) {
        RecyclerView recyclerView;
        recyclerView = view.findViewById(R.id.albums_recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        return recyclerView;
    }

    @Provides
    @TopSongRecyclerViewQualifier
    RecyclerView TopSongRecyclerView(@VerticalLayoutQualifier RecyclerView.LayoutManager layoutManager) {
        RecyclerView recyclerView;
        recyclerView = view.findViewById(R.id.top_tracks_recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        return recyclerView;
    }

    @Provides
    @RelatedArtistsRecyclerViewQualifier
    RecyclerView RelatedArtistRecyclerView(RecyclerView.LayoutManager layoutManager) {
        RecyclerView recyclerView;
        recyclerView = view.findViewById(R.id.related_artists_recylerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        return recyclerView;
    }

}
