package com.loftschool.loftcoin.screens.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.loftschool.loftcoin.App;
import com.loftschool.loftcoin.R;
import com.loftschool.loftcoin.data.prefs.Prefs;
import com.loftschool.loftcoin.screens.start.StartActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomActivity extends AppCompatActivity {


    public static void start(Context context) {
        Intent starter = new Intent(context, WelcomActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    public static void startInNewTask(Context context) {
        Intent starter = new Intent(context, WelcomActivity.class);
        context.startActivity(starter);
    }

    @BindView(R.id.pager)
    ViewPager pager;

    @BindView(R.id.start_btn)
    Button startButton;

    @BindView(R.id.tab_pager)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        ButterKnife.bind(this);

        final Prefs prefs = ((App) getApplication()).getPrefs();

        pager.setAdapter(new WelcomePagerAdapter(getSupportFragmentManager()));

        tabLayout.setupWithViewPager(pager, true);

        startButton.setOnClickListener(v -> {
            prefs.setFirstLaunch(false);
            StartActivity.startInNewTask(WelcomActivity.this);
        });
    }
}
