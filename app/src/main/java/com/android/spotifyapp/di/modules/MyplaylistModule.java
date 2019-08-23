package com.android.spotifyapp.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.android.spotifyapp.utils.SpotifyMyPlaylistAuthContract.BASE_URL;

@Module(includes = {AppModule.class})
public class MyplaylistModule {



}
