package com.android.spotifyapp.data.repositories;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.spotifyapp.data.network.model.RecentlyPlayed;
import com.android.spotifyapp.data.network.model.Recommendations;
import com.android.spotifyapp.data.network.model.UserTopTracks;
import com.android.spotifyapp.data.network.services.HomeService;
import com.android.spotifyapp.di.components.AppComponent;
import com.android.spotifyapp.di.components.DaggerAppComponent;
import com.android.spotifyapp.di.components.DaggerHomeComponent;
import com.android.spotifyapp.di.components.HomeComponent;
import com.android.spotifyapp.di.modules.ContextModule;
import com.android.spotifyapp.di.modules.DialogModule;
import com.android.spotifyapp.di.modules.HorizontalRecyclerView;
import com.android.spotifyapp.di.modules.ViewModelsModule;
import com.android.spotifyapp.di.qualifiers.RetrofitQualifier;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.android.spotifyapp.utils.Contracts.SpotifyAuthContract.ACCESS_TOKEN;
import static com.android.spotifyapp.utils.TAGS.TAG;
import static com.android.spotifyapp.utils.TAGS.TAG4;

public class HomeRepository {
    @Inject
    @RetrofitQualifier
    Retrofit retrofit;
    private HomeService homeService;
    private MutableLiveData<UserTopTracks> userTopTracksMutableLiveData;
    private MutableLiveData<RecentlyPlayed> recentlyPlayedMutableLiveData;
    private MediatorLiveData mediatorLiveData;
    private Recommendations recommendationsHelper;
    private CompositeDisposable compositeDisposable;
    private static HomeRepository homeRepository_instance;
    private HomeRepository() {
        userTopTracksMutableLiveData = new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();
        recentlyPlayedMutableLiveData = new MutableLiveData<>();
        mediatorLiveData = new MediatorLiveData();

        HomeComponent homeComponent = DaggerHomeComponent.builder()
                .horizontalRecyclerView(new HorizontalRecyclerView(null))
                .viewModelsModule(new ViewModelsModule(null, null))
                .appComponent(() -> {
                    AppComponent appComponent = DaggerAppComponent.create();
                    return appComponent.getRetrofit();
                })
                .contextModule(new ContextModule(null))
                .dialogModule(new DialogModule(null))
                .build();

        homeComponent.inject(this);
    }
    public static HomeRepository getInstance() {
        if(homeRepository_instance == null) {
            homeRepository_instance = new HomeRepository();

        }
        return homeRepository_instance;
    }
    public MutableLiveData<RecentlyPlayed> getRecentlyPlayed() {


        homeService = retrofit.create(HomeService.class);
        Observable<RecentlyPlayed>observable = homeService.getRecentlyPlayed("Bearer " + ACCESS_TOKEN);
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<RecentlyPlayed>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("SUBGOT", "onSubscribe: Recents");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(RecentlyPlayed recentlyPlayed) {
                        Log.d("GOTCHA", "onNextRecentlys: " + recentlyPlayed.getMitems().size());
                            recentlyPlayedMutableLiveData.setValue(recentlyPlayed);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "on" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return recentlyPlayedMutableLiveData;
    }

    @SuppressLint("CheckResult")
    public MutableLiveData<Recommendations> getRecommendations() {
        final MutableLiveData<Recommendations> recommendationsMutableLiveData = new MutableLiveData<>();
        getRecommendationsObservable()
                .subscribeOn(Schedulers.io())
                .flatMap((Function<Recommendations.Tracks, ObservableSource<Recommendations.Tracks>>) tracks -> getModifiedObservable(tracks)).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Recommendations.Tracks>() {
            @Override
            public void onSubscribe(Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(Recommendations.Tracks tracks) {
                recommendationsMutableLiveData.setValue(recommendationsHelper);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
            }
        });


        return recommendationsMutableLiveData;
    }
    private Observable<Recommendations.Tracks> getRecommendationsObservable() {
        return homeService.getRecommendations("Bearer " + ACCESS_TOKEN, "pop")
              .subscribeOn(Schedulers.io())
                .flatMap((Function<Recommendations, ObservableSource<Recommendations.Tracks>>) recommendations -> {
                    recommendationsHelper = recommendations;
                    return Observable.fromIterable(recommendations.getMtracks());
                });

    }
    private Observable<Recommendations.Tracks> getModifiedObservable(final Recommendations.Tracks tracks) {
        return homeService.getArtist("Bearer " + ACCESS_TOKEN, tracks.getMartists().get(0).getId())
                .map(artist -> {
                    tracks.setArtist(artist);
                    recommendationsHelper.getMtracks().set(recommendationsHelper.getMtracks().indexOf(tracks), tracks);
                    return tracks;
                });
    }

    public MutableLiveData<UserTopTracks> getUserTopTracks(int limit) {
        Observable<UserTopTracks> observable = homeService.getUserTopTracks("Bearer " + ACCESS_TOKEN, "tracks", limit);
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<UserTopTracks>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(UserTopTracks userTopTracks) {
                        Log.d(TAG4, "onNext: " + userTopTracks.getItems().size());
                            userTopTracksMutableLiveData.setValue(userTopTracks);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG4, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return userTopTracksMutableLiveData;
    }

    public CompositeDisposable getDisposables() {
        return compositeDisposable;
    }
}
