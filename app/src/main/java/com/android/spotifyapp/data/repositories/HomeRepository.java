package com.android.spotifyapp.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.spotifyapp.data.network.model.RecentlyPlayed;
import com.android.spotifyapp.data.network.services.HomeService;
import com.android.spotifyapp.di.components.AppComponent;
import com.android.spotifyapp.di.components.DaggerAppComponent;
import com.android.spotifyapp.di.components.DaggerHomeComponent;
import com.android.spotifyapp.di.components.HomeComponent;
import com.android.spotifyapp.di.modules.HorizontalRecyclerView;
import com.android.spotifyapp.di.modules.ViewModelsModule;
import com.android.spotifyapp.di.qualifiers.RetrofitQualifier;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.android.spotifyapp.utils.SpotifyAuthContract.ACCESS_TOKEN;
import static com.android.spotifyapp.utils.TAGS.TAG;

public class HomeRepository {
    @Inject
            @RetrofitQualifier
    Retrofit retrofit;
    private static HomeRepository homeRepository_instance;
    private HomeRepository() {}
    public static HomeRepository getInstance() {
        if(homeRepository_instance == null) {
            homeRepository_instance = new HomeRepository();
        }
        return homeRepository_instance;
    }
    public MutableLiveData<RecentlyPlayed> getRecentlyPlayed() {
        final MutableLiveData<RecentlyPlayed> recentlyPlayedMutableLiveData = new MutableLiveData<>();
        HomeComponent homeComponent = DaggerHomeComponent.builder()
                .horizontalRecyclerView(new HorizontalRecyclerView(null))
                .viewModelsModule(new ViewModelsModule(null))
                .appComponent(new AppComponent() {
            @Override
            public Retrofit getRetrofit() {
                AppComponent appComponent = DaggerAppComponent.create();
                return appComponent.getRetrofit();
            }
        }).build();
        homeComponent.inject(this);
        HomeService homeService = retrofit.create(HomeService.class);
        Observable<RecentlyPlayed>observable = homeService.getRecentlyPlayed("Bearer " + ACCESS_TOKEN);
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<RecentlyPlayed>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RecentlyPlayed recentlyPlayed) {
                            recentlyPlayedMutableLiveData.setValue(recentlyPlayed);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return recentlyPlayedMutableLiveData;
    }
    public void refresh(final MutableLiveData<RecentlyPlayed> mutableLiveData) {
        HomeComponent homeComponent = DaggerHomeComponent.builder()
                .horizontalRecyclerView(new HorizontalRecyclerView(null))
                .appComponent(new AppComponent() {
                    @Override
                    public Retrofit getRetrofit() {
                        AppComponent appComponent = DaggerAppComponent.create();
                        return appComponent.getRetrofit();
                    }
                }).build();
        homeComponent.inject(this);
        HomeService homeService = retrofit.create(HomeService.class);
        Observable<RecentlyPlayed>observable = homeService.getRecentlyPlayed("Bearer " + ACCESS_TOKEN);
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<RecentlyPlayed>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(RecentlyPlayed recentlyPlayed) {
                        mutableLiveData.setValue(recentlyPlayed);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }



}
