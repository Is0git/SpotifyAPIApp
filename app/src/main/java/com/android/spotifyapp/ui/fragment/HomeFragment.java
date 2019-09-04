package com.android.spotifyapp.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.spotifyapp.App;
import com.android.spotifyapp.R;
import com.android.spotifyapp.data.ViewModels.HomeViewModel;
import com.android.spotifyapp.data.ViewModels.MyPlaylistViewModel;
import com.android.spotifyapp.data.network.model.MyPlaylist;
import com.android.spotifyapp.data.network.model.RecentlyPlayed;
import com.android.spotifyapp.di.components.DaggerHomeComponent;
import com.android.spotifyapp.di.components.HomeComponent;
import com.android.spotifyapp.di.modules.HorizontalRecyclerView;
import com.android.spotifyapp.di.modules.ViewModelsModule;
import com.android.spotifyapp.di.qualifiers.MyPlaylistListQualifier;
import com.android.spotifyapp.di.qualifiers.RecentlyPlayedQualifier;
import com.android.spotifyapp.di.qualifiers.RecommendedListQualifier;
import com.android.spotifyapp.ui.GlobalState.CurrentSongState;
import com.android.spotifyapp.ui.activities.BaseActivity;
import com.android.spotifyapp.ui.adapters.Home.HomeHorizontal;
import com.android.spotifyapp.ui.adapters.Home.MyPlaylistsAdapter;
import com.android.spotifyapp.ui.adapters.Home.RecommendedAdapter;
import com.android.spotifyapp.ui.adapters.Home.SliderAdapter;
import com.android.spotifyapp.utils.CheckProgressBar;
import com.smarteist.autoimageslider.SliderView;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.spotifyapp.utils.Contracts.SpotifyAuthContract.ACCESS_TOKEN;



public class HomeFragment extends Fragment implements HomeHorizontal.OnItemListener, MyPlaylistsAdapter.PlaylistListener {
    @Inject @RecentlyPlayedQualifier RecyclerView recyclerView;
    @Inject @MyPlaylistListQualifier RecyclerView MyPlaylistRecyclerView;
    @Inject @RecommendedListQualifier RecyclerView recommendedRecyclerView;

    @Inject HomeHorizontal homeHorizontal;
    @Inject MyPlaylistsAdapter myPlaylistsAdapter;
    @Inject RecommendedAdapter recommendedAdapter;

    @Inject HomeViewModel homeViewModel;
    @Inject MyPlaylistViewModel myPlaylistViewModel;

    @Inject SliderAdapter sliderAdapter;

    @BindView(R.id.progressBarRecommended) ProgressBar progressBar_recommended;
    @BindView(R.id.progressBarRecentlyPlayed) ProgressBar progressBar_recently;
    @BindView(R.id.progressBarPlaylists) ProgressBar progressBar_playlists;
    @BindView(R.id.progressBarHomeSlider) ProgressBar progressBar_slider;
    @BindView(R.id.recently_played_items) TextView recently_played_items;
    @BindView(R.id.myPlaylist_items) TextView my_playlist_items;
    @BindView(R.id.recommended_items) TextView recommended_items;
    @BindView(R.id.slider) SliderView sliderView;


    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, view);
        //Dagger
        HomeComponent component = DaggerHomeComponent.builder()
                .horizontalRecyclerView(new HorizontalRecyclerView(view))
                .appComponent(App.get(Objects.requireNonNull(this.getActivity())).getAppComponent())
                .viewModelsModule(new ViewModelsModule(this, null))
                .build();
        component.injectFragment(this);

        //Recently played list
        //getViewLifecycleOwner() is better than 'this' because then observer is removed after fragment view is destroyed
        recyclerView.setAdapter(homeHorizontal);
        homeHorizontal.setListener(this);
        homeViewModel.getRecentlyPlayedLiveData().observe(getViewLifecycleOwner(), recentlyPlayed -> {
            recently_played_items.setText(getString(R.string.items, recentlyPlayed.getMitems().size()));
            homeHorizontal.UpdateData(recentlyPlayed);
            CheckProgressBar.checkprogressBar(recyclerView, progressBar_recently);
        });

        //MyPlaylist List
        MyPlaylistRecyclerView.setAdapter(myPlaylistsAdapter);
        myPlaylistsAdapter.setPlaylistListener(this);
        myPlaylistViewModel.getMyPlaylistLiveData("Bearer " + ACCESS_TOKEN).observe(getViewLifecycleOwner(), myPlaylist -> {
            my_playlist_items.setText(getString(R.string.items, myPlaylist.getMitems().size()));
            myPlaylistsAdapter.UpdateList(myPlaylist);
            CheckProgressBar.checkprogressBar(MyPlaylistRecyclerView, progressBar_playlists);
        });

        //Recommended List
        recommendedRecyclerView.setAdapter(recommendedAdapter);
        homeViewModel.getRecommendations().observe(getViewLifecycleOwner(), recommendations -> {
            recommended_items.setText(getString(R.string.items, recommendations.getMtracks().size()));
            recommendedAdapter.UpdateData(recommendations);
            CheckProgressBar.checkprogressBar(recommendedRecyclerView, progressBar_recommended);
         });

        //Slider
        sliderView.setSliderAdapter(sliderAdapter);
        homeViewModel.getUserTopTracksMutableLiveData(5).observe(getViewLifecycleOwner(), userTopTracks -> {
            sliderAdapter.UpdateData(userTopTracks);
            CheckProgressBar.checkSliderProgress(sliderView, progressBar_slider);
        });
        return view;
    }

    @Override
    public void onClick(int position, String title) {
        CurrentSongState currentSongState = CurrentSongState.getInstance();
        currentSongState.setTitle(title);
        ((BaseActivity) Objects.requireNonNull(getActivity())).startPlayer();
    }

    @Override
    public void onPlaylistItemClick(int position, MyPlaylist myPlaylist, View itemView) {

//        myPlaylistViewModel.deletePlaylist(myPlaylist.getMitems().get(position).getId());
    }
}
