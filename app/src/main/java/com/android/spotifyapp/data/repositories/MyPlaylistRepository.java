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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.android.spotifyapp.utils.Contracts.SpotifyAuthContract.ACCESS_TOKEN;

public class MyPlaylistRepository {
    @Inject
            @RetrofitQualifier
    Retrofit retrofit;
    String TAG = "GETPLAYLIST";
    private MutableLiveData<MyPlaylist> myPlaylistMutableLiveData;
    private CompositeDisposable compositeDisposable;
    private static MyPlaylistRepository myPlaylistRepository_instance = null;
    private MyPlaylistRepository() {
        myPlaylistMutableLiveData = new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();}
    public static MyPlaylistRepository getInstance() {
        if(myPlaylistRepository_instance == null) {
            myPlaylistRepository_instance = new MyPlaylistRepository();
        }
        return myPlaylistRepository_instance;
    }
    public LiveData<MyPlaylist> getMyPlaylist(String access_token) {

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
    public void delete_playlist(String playlist_id) {
            MyPlaylistService myPlaylistService = retrofit.create(MyPlaylistService.class);
            Call<Void> call =  myPlaylistService.deletePlaylist("Bearer " + ACCESS_TOKEN, playlist_id);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    refreshPlaylist();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
    }

    public void resetPlaylist() {
        myPlaylistMutableLiveData.setValue(new MyPlaylist());
    }

    public void refreshPlaylist() {
        getMyPlaylist("Bearer " + ACCESS_TOKEN);
    }
    public CompositeDisposable getDisposables() {
        return compositeDisposable;
    }
}
