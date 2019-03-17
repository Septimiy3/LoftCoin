package com.loftschool.loftcoin.screens.launch;

import android.os.Bundle;

import com.loftschool.loftcoin.App;
import com.loftschool.loftcoin.data.prefs.Prefs;
import com.loftschool.loftcoin.screens.start.StartActivity;
import com.loftschool.loftcoin.screens.welcome.WelcomActivity;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Prefs prefs = ((App) getApplication()).getPrefs();

        if (prefs.isFirstLaunch()) {
            WelcomActivity.start(this);
        } else {
            StartActivity.start(this);
        }
        finish();
    }
}
