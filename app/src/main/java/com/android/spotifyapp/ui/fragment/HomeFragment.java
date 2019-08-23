package com.android.spotifyapp.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.android.spotifyapp.App;
import com.android.spotifyapp.R;
import com.android.spotifyapp.data.ViewModels.HomeViewModel;
import com.android.spotifyapp.data.network.model.RecentlyPlayed;
import com.android.spotifyapp.di.components.AppComponent;
import com.android.spotifyapp.di.components.DaggerAppComponent;
import com.android.spotifyapp.di.components.DaggerHomeComponent;
import com.android.spotifyapp.di.components.HomeComponent;
import com.android.spotifyapp.di.modules.AppModule;
import com.android.spotifyapp.di.modules.ContextModule;
import com.android.spotifyapp.di.modules.HorizontalRecyclerView;
import com.android.spotifyapp.di.qualifiers.HomeHorizontalAdapter;
import com.android.spotifyapp.ui.activities.BaseActivity;
import com.android.spotifyapp.ui.adapters.HomeHorizontal;

import java.util.Objects;

import javax.inject.Inject;

import retrofit2.Retrofit;

import static com.android.spotifyapp.utils.TAGS.TAG;

public class HomeFragment extends Fragment {
    @Inject
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    HomeViewModel homeViewModel;
    View view;
    Button refresh;
    Button refresh2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        refresh = view.findViewById(R.id.refresh);
        refresh2 = view.findViewById(R.id.refresh2);
        HomeComponent component = DaggerHomeComponent.builder()
                .horizontalRecyclerView(new HorizontalRecyclerView(view))
                .appComponent(App.get(Objects.requireNonNull(this.getActivity())).getAppComponent())
                .build();
        recyclerView = component.getList();
        adapter = component.getAdapter();
        final HomeHorizontal homeHorizontal = new HomeHorizontal();
        recyclerView.setAdapter(homeHorizontal);
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        homeViewModel.getRecentlyPlayedLiveData().observe(this, new Observer<RecentlyPlayed>() {
            @Override
            public void onChanged(RecentlyPlayed recentlyPlayed) {
                Log.d("REFRESH", "START: " );
                homeHorizontal.UpdateData(recentlyPlayed);


            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeViewModel.setData();
            }
        });
        refresh2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeViewModel.refresh();
            }
        });
        return view;
    }
}
