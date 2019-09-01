package com.android.spotifyapp.data.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.spotifyapp.data.network.model.User;
import com.android.spotifyapp.data.repositories.BaseRepository;

public class BaseViewModel extends AndroidViewModel {
    private MutableLiveData<User> mutableLiveData;
    private BaseRepository baseRepository;
    public BaseViewModel(@NonNull Application application) {
        super(application);
        baseRepository = BaseRepository.getInstance();

    }
    public LiveData<User> getUser() {
        mutableLiveData = baseRepository.getUser();
        return mutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        baseRepository.getDisposables().clear();
    }
}
