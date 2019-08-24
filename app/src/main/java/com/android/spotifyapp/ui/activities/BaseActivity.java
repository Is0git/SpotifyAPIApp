package com.android.spotifyapp.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.spotifyapp.R;
import com.android.spotifyapp.ui.fragment.HomeFragment;
import com.android.spotifyapp.ui.fragment.PlaylistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_activity);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment current = null;
                switch(menuItem.getItemId()) {
                    case R.id.home:
                        current = new HomeFragment();
                        break;
                    case R.id.my_playlists:
                        current = new PlaylistFragment();

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, current).commit();
                return true;
            }
        });
    }
}
