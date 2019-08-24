package com.android.spotifyapp.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.android.spotifyapp.R;
import com.android.spotifyapp.data.ViewModels.AuthViewModel;
import com.android.spotifyapp.data.ViewModels.MyPlaylistViewModel;
import com.android.spotifyapp.data.network.model.AccessToken;
import com.android.spotifyapp.data.network.model.MyPlaylist;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

import static com.android.spotifyapp.utils.ProgressBar.progressBarUnvisible;
import static com.android.spotifyapp.utils.ProgressBar.progressBarVisible;
import static com.android.spotifyapp.utils.SpotifyAuthContract.ACCESS_TOKEN;
import static com.android.spotifyapp.utils.SpotifyAuthContract.REDIRECT_URL;
import static com.android.spotifyapp.utils.SpotifyAuthContract.URI;

public class AuthActivity extends AppCompatActivity {
    Button login;
    Button button;
    ProgressBar progressBar;
    AuthViewModel authViewModel;
    MyPlaylistViewModel myPlaylistViewModel;
    AccessToken maccessToken;
    public final String TAG = "MIN";
    private CompositeDisposable disposables = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        login = findViewById(R.id.redirectbtn);
        button = findViewById(R.id.button);

        progressBar = findViewById(R.id.progressBar);

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        myPlaylistViewModel = ViewModelProviders.of(this).get(MyPlaylistViewModel.class);


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                myPlaylistViewModel.getMyPlaylistLiveData(maccessToken.getToken_type() + " " + maccessToken.getAccess_token()).observe(AuthActivity.this, new androidx.lifecycle.Observer<MyPlaylist>() {
                    @Override
                    public void onChanged(MyPlaylist myPlaylist) {
                        Toast.makeText(AuthActivity.this, "RES " + myPlaylist.getTotal(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        RxView.clicks(login)
                .throttleFirst(10000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Unit>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(Unit unit) {
                        Log.d(TAG, "onNext: " + "CLICKED");
                        progressBarVisible(progressBar);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URI + REDIRECT_URL));
                        startActivity(intent);

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AuthActivity.this, "Oops! Something went wrong...", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: " + "completed");
                    }
                });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear(); // Dispose observable
    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBarUnvisible(progressBar);
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(REDIRECT_URL)) {
            String code = uri.getQueryParameter("code");
            authViewModel.getTokenData(code).observe(this, new androidx.lifecycle.Observer<AccessToken>() {
                @Override
                public void onChanged(AccessToken accessToken) {
                    Log.d(TAG, "onChanged: " );
                    if(!accessToken.getAccess_token().isEmpty()) {
                        maccessToken = accessToken;
                        //set global access token
                        ACCESS_TOKEN = accessToken.getAccess_token();
                        progressBarVisible(progressBar);
                        Intent intent = new Intent(getBaseContext(), BaseActivity.class);
                        startActivity(intent);
                    }
                }
            });

        }

    }
}