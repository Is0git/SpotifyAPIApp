package com.android.spotifyapp.data.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.spotifyapp.data.network.model.User;
import com.android.spotifyapp.data.network.services.UserService;
import com.android.spotifyapp.di.components.AppComponent;
import com.android.spotifyapp.di.components.BaseComponent;
import com.android.spotifyapp.di.components.DaggerAppComponent;
import com.android.spotifyapp.di.components.DaggerBaseComponent;
import com.android.spotifyapp.di.modules.ActivityViewModelModule;
import com.android.spotifyapp.di.qualifiers.RetrofitQualifier;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.android.spotifyapp.utils.Contracts.SpotifyAuthContract.ACCESS_TOKEN;

public class BaseRepository {
    @Inject
    @RetrofitQualifier
    Retrofit retrofit;
    private UserService userService;
    private MutableLiveData<User> mutableLiveData;
    private CompositeDisposable compositeDisposable;
    private static BaseRepository baseRepository_instance;
    private BaseRepository() {
        mutableLiveData = new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();
    }

    public static BaseRepository getInstance() {
        if(baseRepository_instance == null) {
            baseRepository_instance = new BaseRepository();
        }
        return baseRepository_instance;
    }

    public MutableLiveData<User> getUser() {
        BaseComponent component = DaggerBaseComponent.builder().appComponent(new AppComponent() {
            @Override
            public Retrofit getRetrofit() {
                AppComponent appComponent = DaggerAppComponent.create();
                return appComponent.getRetrofit();
            }
        }).activityViewModelModule(new ActivityViewModelModule(null)).build();
        component.inject(this);

        userService = retrofit.create(UserService.class);
        Observable<User>observable = userService.getUser("Bearer " + ACCESS_TOKEN);
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("SUBSCRIBED", "start" );
                    }

                    @Override
                    public void onNext(User user) {

                        mutableLiveData.setValue(user);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("SUBSCRIBED", "onSubscribess: " + e.getMessage() );
                    }

                    @Override
                    public void onComplete() {
                        Log.d("SUBSCRIBED", "onSasda");
                    }
                });
        return this.mutableLiveData;
    }
    public CompositeDisposable getDisposables() {
        return compositeDisposable;
    }
}
