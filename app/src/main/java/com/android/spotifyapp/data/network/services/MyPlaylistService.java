package com.android.spotifyapp.data.network.services;

import com.android.spotifyapp.data.network.model.MyPlaylist;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Streaming;


public interface MyPlaylistService {
    @GET("v1/me/playlists")
    @Streaming
    Observable<MyPlaylist> getMyPlaylist(@Header("Authorization") String access_token);
    @DELETE("v1/playlists/{playlist_id}/followers")
    Call<Void> deletePlaylist(@Header("Authorization") String access_token, @Path("playlist_id") String playlist_id);
}
