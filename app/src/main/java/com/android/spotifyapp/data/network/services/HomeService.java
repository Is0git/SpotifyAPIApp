package com.android.spotifyapp.data.network.services;

import com.android.spotifyapp.data.network.model.RecentlyPlayed;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface HomeService {
    @GET("v1/me/player/recently-played")
    Observable<RecentlyPlayed>getRecentlyPlayed(@Header("Authorization") String access_token);
}
