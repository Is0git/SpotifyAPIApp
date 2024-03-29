package com.android.spotifyapp.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.spotifyapp.R;
import com.android.spotifyapp.data.ViewModels.AuthViewModel;
import com.android.spotifyapp.di.components.AuthComponent;
import com.android.spotifyapp.di.components.DaggerAuthComponent;
import com.android.spotifyapp.di.modules.ContextModule;
import com.android.spotifyapp.di.modules.ViewModelsModule;
import com.android.spotifyapp.utils.SharedPreferencesUtil;
import com.jakewharton.rxbinding3.view.RxView;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;
import static com.android.spotifyapp.utils.Contracts.SharedPreferencesContract.access_token;
import static com.android.spotifyapp.utils.Contracts.SharedPreferencesContract.shared_preferences_auth;
import static com.android.spotifyapp.utils.Contracts.SpotifyAuthContract.REDIRECT_URL;
import static com.android.spotifyapp.utils.Contracts.SpotifyAuthContract.URI;
import static com.android.spotifyapp.utils.ProgressBar.progressBarUnvisible;
import static com.android.spotifyapp.utils.ProgressBar.progressBarVisible;

public class AuthActivity extends AppCompatActivity {
    @BindView(R.id.redirectbtn) Button login;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @Inject AuthViewModel authViewModel;

    private CompositeDisposable disposables = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        ButterKnife.bind(this);

        AuthComponent authComponent = DaggerAuthComponent.builder()
                .viewModelsModule(new ViewModelsModule(null))
                .contextModule(new ContextModule(this)).build();
        authComponent.inject(this);

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
                        progressBarVisible(progressBar);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URI + REDIRECT_URL));
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AuthActivity.this, "Oops! Something went wrong...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

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
            authViewModel.getTokenData(code).observe(this, accessToken -> {
                if(!accessToken.getAccess_token().isEmpty()) {
                    SharedPreferences.Editor editor = SharedPreferencesUtil.getPreferences(shared_preferences_auth, getApplicationContext()).edit();
                    editor.putString(access_token, accessToken.getAccess_token()).apply();
                    progressBarVisible(progressBar);
                    Intent intent = new Intent(getBaseContext(), BaseActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
