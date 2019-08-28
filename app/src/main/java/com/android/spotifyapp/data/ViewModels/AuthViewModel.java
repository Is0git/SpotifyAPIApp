package com.android.spotifyapp.data.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.spotifyapp.data.network.model.AccessToken;
import com.android.spotifyapp.data.repositories.AuthRepository;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    private LiveData<AccessToken> data;
    public AuthViewModel(@NonNull Application application) {
        super(application);
        authRepository = AuthRepository.getInstance();

    }
    public LiveData<AccessToken>getTokenData(String code) {
        this.data = authRepository.getAccess(code);
        return data;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        authRepository.getDisposables().clear();
    }
}
