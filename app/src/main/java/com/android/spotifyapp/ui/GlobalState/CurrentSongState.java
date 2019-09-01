package com.android.spotifyapp.ui.GlobalState;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;

public class CurrentSongState {
    private static CurrentSongState currentSongState_instance;
    private int song_duration;
    private PlayerConstants.PlayerState globalstate;

    public int getSong_duration() {
        return song_duration;
    }

    public void setSong_duration(int song_duration) {
        this.song_duration = song_duration;
    }

    public PlayerConstants.PlayerState getGlobalstate() {
        return globalstate;
    }

    public void setGlobalstate(PlayerConstants.PlayerState globalstate) {
        this.globalstate = globalstate;
    }

    private CurrentSongState() {song_duration = 0;}

    public static CurrentSongState getInstance() {
        if(currentSongState_instance == null) {
            currentSongState_instance = new CurrentSongState();
        }
        return currentSongState_instance;
    }
}
