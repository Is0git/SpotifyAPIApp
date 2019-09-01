package com.android.spotifyapp.ui.fragment;

import android.os.Bundle;
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
import com.android.spotifyapp.di.components.DaggerHomeComponent;
import com.android.spotifyapp.di.components.HomeComponent;
import com.android.spotifyapp.di.modules.HorizontalRecyclerView;
import com.android.spotifyapp.di.modules.ViewModelsModule;
import com.android.spotifyapp.di.qualifiers.MyPlaylistListQualifier;
import com.android.spotifyapp.di.qualifiers.RecentlyPlayedQualifier;
import com.android.spotifyapp.di.qualifiers.RecommendedListQualifier;
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

import static com.android.spotifyapp.utils.SpotifyAuthContract.ACCESS_TOKEN;



public class HomeFragment extends Fragment {
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
                .viewModelsModule(new ViewModelsModule(this))
                .build();
        component.injectFragment(this);

        //Recently played list
        //getViewLifecycleOwner() is better than 'this' because then observer is removed after fragment view is destroyed
        recyclerView.setAdapter(homeHorizontal);
        homeViewModel.getRecentlyPlayedLiveData().observe(getViewLifecycleOwner(), recentlyPlayed -> {
            recently_played_items.setText(getString(R.string.items, recentlyPlayed.getMitems().size()));
            homeHorizontal.UpdateData(recentlyPlayed);
            CheckProgressBar.checkprogressBar(recyclerView, progressBar_recently);
        });

        //MyPlaylist List
        MyPlaylistRecyclerView.setAdapter(myPlaylistsAdapter);
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
}
