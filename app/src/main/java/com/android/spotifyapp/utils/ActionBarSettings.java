package com.android.spotifyapp.utils;

import android.view.LayoutInflater;
import android.view.View;

import com.android.spotifyapp.R;
import com.android.spotifyapp.ui.activities.BaseActivity;

public class ActionBarSettings {
    public static void SetActionBar(BaseActivity baseActivity, View view) {
        baseActivity.getSupportActionBar().setDisplayShowCustomEnabled(true);
        baseActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        baseActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        baseActivity.getSupportActionBar().setCustomView(view);
    }
}
