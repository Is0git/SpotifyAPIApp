package com.android.spotifyapp.data.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.spotifyapp.data.network.model.RecentlyPlayed;
import com.android.spotifyapp.data.network.model.Recommendations;
import com.android.spotifyapp.data.network.model.UserTopTracks;
import com.android.spotifyapp.data.repositories.HomeRepository;

public class HomeViewModel extends AndroidViewModel {
    private HomeRepository homeRepository;
    private MutableLiveData<RecentlyPlayed>recentlyPlayedLiveData;
    private MutableLiveData<Recommendations>recommendationsMutableLiveData;
    private MutableLiveData<UserTopTracks>userTopTracksMutableLiveData;
    public HomeViewModel(@NonNull Application application) {
        super(application);
        homeRepository = HomeRepository.getInstance();
    }

    public LiveData<RecentlyPlayed> getRecentlyPlayedLiveData() {
        recentlyPlayedLiveData = homeRepository.getRecentlyPlayed();
        return recentlyPlayedLiveData;
    }
    public void setData() {
        recentlyPlayedLiveData.setValue(new RecentlyPlayed());
    }
    public void refresh() {
        homeRepository.refresh(recentlyPlayedLiveData);
    }

    public LiveData<Recommendations>getRecommendations() {
        recommendationsMutableLiveData = homeRepository.getRecommendations();
        return recommendationsMutableLiveData;
    }

    public LiveData<UserTopTracks>getUserTopTracksMutableLiveData(int limit) {
        userTopTracksMutableLiveData = homeRepository.getUserTopTracks(limit);
        return userTopTracksMutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        homeRepository.getDisposables().clear();
    }
}
