package com.android.spotifyapp.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.android.spotifyapp.App;
import com.android.spotifyapp.R;
import com.android.spotifyapp.data.ViewModels.BaseViewModel;
import com.android.spotifyapp.data.network.model.RecentlyPlayed;
import com.android.spotifyapp.data.network.model.User;
import com.android.spotifyapp.di.components.BaseComponent;
import com.android.spotifyapp.di.components.DaggerBaseComponent;
import com.android.spotifyapp.di.modules.ActivityViewModelModule;
import com.android.spotifyapp.ui.fragment.HomeFragment;
import com.android.spotifyapp.ui.fragment.PlaylistFragment;
import com.android.spotifyapp.ui.fragment.YoutubeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.spotifyapp.utils.ActionBarSettings.SetActionBar;
import static com.android.spotifyapp.utils.Contracts.SpotifyAuthContract.USER_ID;

public class BaseActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener {
    @BindView(R.id.bottom_nav) BottomNavigationView bottomNavigationView;
    ImageView user_image;
    @Inject BaseViewModel viewModel;
    View actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_activity);
        ButterKnife.bind(this);


        //ActionBar
        actionbar  = LayoutInflater.from(this).inflate(R.layout.actionbar, null);
        SetActionBar(this, actionbar);
        user_image = actionbar.findViewById(R.id.action_user);


        //Dagger
        BaseComponent component = DaggerBaseComponent.builder().appComponent(App.get(Objects.requireNonNull(this))
                .getAppComponent())
                .activityViewModelModule(new ActivityViewModelModule(this))
                .build();
        component.injectActivity(this);




        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                Log.d("BASETAG", "onChangeds: " + user_image);
                Picasso.with(getApplicationContext())
                        .load(user.getMimages().get(0).getUrl())
                        .fit().into(user_image);

               USER_ID = user.getId();
            }
        });

        //Fragments
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            Fragment current = null;
            switch(menuItem.getItemId()) {
                case R.id.home:
                    current = new HomeFragment();
                    break;
                case R.id.my_playlists:
                    current = new PlaylistFragment();

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, current).commit();
            return true;
        });
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        youTubePlayer.loadVideo("XKxwV1ETTfc");
        youTubePlayer.pause();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    public void startPlayer() {
        getSupportFragmentManager().beginTransaction().replace(R.id.youtube_fragment, new YoutubeFragment()).commit();
    }


}
