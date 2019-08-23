package com.android.spotifyapp.data.network.services;

import com.android.spotifyapp.data.network.model.MyPlaylist;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;


public interface MyPlaylistService {
    @GET("v1/me/playlists")
    @Streaming
    Observable<MyPlaylist> getMyPlaylist(@Header("Authorization") String access_token);
}
