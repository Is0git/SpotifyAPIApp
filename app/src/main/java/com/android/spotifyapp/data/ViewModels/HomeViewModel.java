package com.android.spotifyapp.data.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.spotifyapp.data.network.model.RecentlyPlayed;
import com.android.spotifyapp.data.repositories.HomeRepository;

public class HomeViewModel extends AndroidViewModel {
    private HomeRepository homeRepository;
    private MutableLiveData<RecentlyPlayed>recentlyPlayedLiveData;
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
}
