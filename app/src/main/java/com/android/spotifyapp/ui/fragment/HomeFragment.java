package com.android.spotifyapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import com.android.spotifyapp.App;
import com.android.spotifyapp.R;
import com.android.spotifyapp.data.ViewModels.HomeViewModel;
import com.android.spotifyapp.data.ViewModels.MyPlaylistViewModel;
import com.android.spotifyapp.data.network.model.MyPlaylist;
import com.android.spotifyapp.di.components.DaggerHomeComponent;
import com.android.spotifyapp.di.components.HomeComponent;
import com.android.spotifyapp.di.modules.HorizontalRecyclerView;
import com.android.spotifyapp.di.modules.ViewModelsModule;
import com.android.spotifyapp.di.qualifiers.MyPlaylistListQualifier;
import com.android.spotifyapp.di.qualifiers.RecentlyPlayedQualifier;
import com.android.spotifyapp.ui.adapters.HomeHorizontal;
import com.android.spotifyapp.ui.adapters.MyPlaylistsAdapter;
import java.util.Objects;
import javax.inject.Inject;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.android.spotifyapp.utils.SpotifyAuthContract.ACCESS_TOKEN;



public class HomeFragment extends Fragment {
    @Inject @RecentlyPlayedQualifier RecyclerView recyclerView;
    @Inject @MyPlaylistListQualifier RecyclerView MyPlaylistRecyclerView;

    @Inject HomeHorizontal homeHorizontal;
    @Inject MyPlaylistsAdapter myPlaylistsAdapter;

    @Inject HomeViewModel homeViewModel;
    @Inject MyPlaylistViewModel myPlaylistViewModel;

    View view;

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
        recyclerView.setAdapter(homeHorizontal);
        homeViewModel.getRecentlyPlayedLiveData().observe(this, recentlyPlayed -> homeHorizontal.UpdateData(recentlyPlayed));

        //MyPlaylist List
        MyPlaylistRecyclerView.setAdapter(myPlaylistsAdapter);
        myPlaylistViewModel.getMyPlaylistLiveData("Bearer " + ACCESS_TOKEN).observe(this, new Observer<MyPlaylist>() {
            @Override
            public void onChanged(MyPlaylist myPlaylist) {
                myPlaylistsAdapter.UpdateList(myPlaylist);
            }
        });
        return view;
    }

    @OnClick(R.id.refresh2) void click() {
        homeViewModel.refresh();
    }
    @OnClick(R.id.refresh) void click2() {
        homeViewModel.setData();
    }
}
