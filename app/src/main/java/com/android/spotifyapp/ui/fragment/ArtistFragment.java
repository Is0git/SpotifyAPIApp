package com.android.spotifyapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import com.android.spotifyapp.App;
import com.android.spotifyapp.R;
import com.android.spotifyapp.data.ViewModels.ArtistViewModel;
import com.android.spotifyapp.data.network.model.Recommendations;
import com.android.spotifyapp.data.network.model.byId.ArtistTopTracks;
import com.android.spotifyapp.data.network.model.byId.RelatedArtists;
import com.android.spotifyapp.di.components.ArtistComponent;
import com.android.spotifyapp.di.components.DaggerArtistComponent;
import com.android.spotifyapp.di.modules.AdaptersModule;
import com.android.spotifyapp.di.modules.ContextModule;
import com.android.spotifyapp.di.modules.RecyclerViewModule;
import com.android.spotifyapp.di.modules.ViewModelsModule;
import com.android.spotifyapp.di.qualifiers.ArtistsAlbumsRecyclerViewQualifier;
import com.android.spotifyapp.di.qualifiers.RelatedArtistsRecyclerViewQualifier;
import com.android.spotifyapp.di.qualifiers.TopSongRecyclerViewQualifier;
import com.android.spotifyapp.ui.activities.BaseActivity;
import com.android.spotifyapp.ui.adapters.Artist.AlbumAdapter;
import com.android.spotifyapp.ui.adapters.Artist.RelatedArtistsAdapter;
import com.android.spotifyapp.ui.adapters.Artist.TopSongsAdapter;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistFragment extends Fragment {
    @BindView(R.id.artist_photo) ImageView artist_image;
    @BindView(R.id.artist_name_artist) TextView artist_name;
    @BindView(R.id.followers_artist) TextView followers;
    @BindView(R.id.album_items) TextView album_items;
    @BindView(R.id.artist_small_image) ImageView artist_small;
    @BindView(R.id.top_tracks_items) TextView top_track_items;
    @BindView(R.id.related_artists_items) TextView related_artists_item;


    @Inject ArtistViewModel viewModel;

    @Inject @ArtistsAlbumsRecyclerViewQualifier RecyclerView albums_recyclerView;
    @Inject @TopSongRecyclerViewQualifier RecyclerView songs_recyclerView;
    @Inject @RelatedArtistsRecyclerViewQualifier RecyclerView related_artists_recyclerView;

    @Inject AlbumAdapter albumAdapter;
    @Inject TopSongsAdapter topSongsAdapter;
    @Inject RelatedArtistsAdapter relatedArtistsAdapter;

    private Recommendations recommendations;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.artist_layout, container, false);
        ButterKnife.bind(this, view);

        //Dagger
        ArtistComponent component = DaggerArtistComponent.builder()
                .appComponent(((App) Objects.requireNonNull(getActivity()).getApplicationContext()).getAppComponent())
                .viewModelsModule(new ViewModelsModule(this))
                .adaptersModule(new AdaptersModule())
                .contextModule(new ContextModule(this.getActivity()))
                .recyclerViewModule(new RecyclerViewModule(view))
                .build();
        component.injectFragment(this);

        //data from past fragment
        assert getArguments() != null;
        int position = getArguments().getInt("position");
        recommendations = (Recommendations) getArguments().getSerializable("recommendations");
        assert recommendations != null;
        artist_name.setText(recommendations.getMtracks().get(position).getMartists().get(0).getName());
        followers.setText(getString(R.string.followers, recommendations.getMtracks().get(position).getArtist().getFollowers().getTotal()));
        Picasso.with(getContext()).load(recommendations.getMtracks().get(position).getArtist().getImages().get(0).getUrl()).fit().into(artist_image);

        //Artist's UI
        albums_recyclerView.setAdapter(albumAdapter);
        viewModel.getArtistsAlbum(recommendations.getMtracks().get(position).getMartists().get(0).getId()).observe(getViewLifecycleOwner(), artistsAlbum -> {
            album_items.setText(getString(R.string.items, artistsAlbum.getItems().size()));
            albumAdapter.setArtistsAlbum(artistsAlbum);
            Picasso.with(getContext()).load(recommendations.getMtracks().get(position).getArtist().getImages().get(2).getUrl()).fit().into(artist_small);
        });

        //Artist's top songs data
        songs_recyclerView.setAdapter(topSongsAdapter);
        viewModel.getTopTracks(recommendations.getMtracks().get(position).getMartists().get(0).getId(), ((BaseActivity) getActivity()).getUser().getCountry()).observe(getViewLifecycleOwner(), artistTopTracks -> {
            top_track_items.setText(getString(R.string.items, artistTopTracks.getTracks().size()));
            topSongsAdapter.updateData(artistTopTracks);
        });

        //Related artists
        related_artists_recyclerView.setAdapter(relatedArtistsAdapter);
        viewModel.getRelatedArtists(recommendations.getMtracks().get(position).getMartists().get(0).getId()).observe(getViewLifecycleOwner(), new Observer<RelatedArtists>() {
            @Override
            public void onChanged(RelatedArtists relatedArtists) {
                related_artists_item.setText(getString(R.string.items, relatedArtists.getArtists().size()));
                relatedArtistsAdapter.setRelatedArtists(relatedArtists);
            }
        });
        return view;
    }
}
