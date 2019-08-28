package com.android.spotifyapp.data.repositories;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.spotifyapp.data.network.model.MyPlaylist;
import com.android.spotifyapp.data.network.services.MyPlaylistService;
import com.android.spotifyapp.di.components.DaggerMyplaylistComponent;
import com.android.spotifyapp.di.components.MyplaylistComponent;
import com.android.spotifyapp.di.qualifiers.RetrofitQualifier;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MyPlaylistRepository {
    @Inject
            @RetrofitQualifier
    Retrofit retrofit;
    String TAG = "GETPLAYLIST";
    private static MyPlaylistRepository myPlaylistRepository_instance = null;
    private MyPlaylistRepository() {}
    public static MyPlaylistRepository getInstance() {
        if(myPlaylistRepository_instance == null) {
            myPlaylistRepository_instance = new MyPlaylistRepository();
        }
        return myPlaylistRepository_instance;
    }
    public LiveData<MyPlaylist> getMyPlaylist(String access_token) {
        final MutableLiveData<MyPlaylist> myPlaylistMutableLiveData = new MutableLiveData<>();
        MyplaylistComponent myplaylistComponent = DaggerMyplaylistComponent.create();
        myplaylistComponent.inject(this);
        MyPlaylistService myPlaylistService =  retrofit.create(MyPlaylistService.class);
        Observable<MyPlaylist> observable2 = myPlaylistService.getMyPlaylist(access_token);
        observable2.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<MyPlaylist>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("SUBGOT", "onSubscribe: playlist");
                    }

                    @Override
                    public void onNext(MyPlaylist myPlaylist) {
                        Log.d("GOTCHA", "onMyPlaylist: " + myPlaylist.getMitems().size());
                    myPlaylistMutableLiveData.setValue(myPlaylist);
                        Log.d(TAG, "onNext: " + myPlaylist.getTotal());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());

                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: " + "Completed");
                    }
                });
        return myPlaylistMutableLiveData;
    }
}
