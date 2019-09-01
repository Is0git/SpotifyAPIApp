package com.android.spotifyapp.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.android.spotifyapp.R;
import com.android.spotifyapp.ui.GlobalState.CurrentSongState;
import com.android.spotifyapp.ui.listeners.OnSwipeListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import org.jetbrains.annotations.NotNull;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class YoutubeFragment extends Fragment  {
    View view;
    @BindView(R.id.play_button) CardView play_button;
    @BindView(R.id.youtube_player_view) YouTubePlayerView youTubePlayerView;
    private YouTubePlayer globalyouTubePlayer;
    @BindView(R.id.song_progress_bar) ProgressBar progressBar;
    @BindView(R.id.player_icon) ImageView player_icon;
    private FragmentManager fragmentManager;
    private CurrentSongState currentSongState;

    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.youtube_fragment, container, false);
        ButterKnife.bind(this, view);
        fragmentManager = getFragmentManager();
        currentSongState = CurrentSongState.getInstance();

        boolean b = youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NotNull YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                globalyouTubePlayer = youTubePlayer;
            }

            @Override
            public void onCurrentSecond(@NotNull YouTubePlayer youTubePlayer, float second) {
                super.onCurrentSecond(youTubePlayer, second);
                progressBar.setProgress((int) (100 * second / currentSongState.getSong_duration()));
            }

            @Override
            public void onVideoDuration(@NotNull YouTubePlayer youTubePlayer, float duration) {
                super.onVideoDuration(youTubePlayer, duration);
                currentSongState.setSong_duration((int) duration);
            }

            @Override
            public void onStateChange(@NotNull YouTubePlayer youTubePlayer, @NotNull PlayerConstants.PlayerState state) {
                super.onStateChange(youTubePlayer, state);
                currentSongState.setGlobalstate(state);
                switch(state) {
                    case PAUSED:
                    case ENDED:
                        player_icon.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                        break;
                    case PLAYING:
                        player_icon.setImageResource(R.drawable.ic_pause_black_24dp);
                        break;
                }
            }
        });

        view.setOnTouchListener((v, event) -> OnSwipeListener.onSwipe(v, event, fragmentManager, YoutubeFragment.this));
        return view;
    }

    @OnClick(R.id.play_button) void play() {
        if(globalyouTubePlayer != null) {
            if (currentSongState.getGlobalstate() == PlayerConstants.PlayerState.PLAYING)
                globalyouTubePlayer.pause();
            else {
                globalyouTubePlayer.play();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        youTubePlayerView.release();
    }
}
