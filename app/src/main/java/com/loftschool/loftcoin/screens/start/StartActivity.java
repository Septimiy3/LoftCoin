package com.loftschool.loftcoin.screens.start;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.loftschool.loftcoin.App;
import com.loftschool.loftcoin.R;
import com.loftschool.loftcoin.data.api.Api;
import com.loftschool.loftcoin.data.db.DataBase;
import com.loftschool.loftcoin.data.db.model.CoinEntityMapper;
import com.loftschool.loftcoin.data.db.model.CoinEntityMapperImpl;
import com.loftschool.loftcoin.data.prefs.Prefs;
import com.loftschool.loftcoin.screens.main.MainActivity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StartActivity extends AppCompatActivity implements StartView {


    public static void start(Context context) {
        Intent starter = new Intent(context, StartActivity.class);
        context.startActivity(starter);
    }

    public static void startInNewTask(Context context) {
        Intent starter = new Intent(context, StartActivity.class);
        starter.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    @BindView(R.id.start_top_corner)
    ImageView topCorner;

    @BindView(R.id.start_bottom_corner)
    ImageView bottomCorner;

    private StartPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);


        Prefs prefs = ((App) getApplication()).getPrefs();
        Api api = ((App) getApplication()).getApi();
        DataBase dataBase = ((App) getApplication()).getDataBase();
        CoinEntityMapper mapper = new CoinEntityMapperImpl();

        presenter = new StartPresenterImpl(prefs, api, dataBase, mapper);

        presenter.attachView(this);

        presenter.loadRates();

    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();

        startAnimations();
    }

    @Override
    public void navigateToMainScreen() {
        MainActivity.start(this);
        finish();
    }

    private void startAnimations() {
        ObjectAnimator innerAnimator = ObjectAnimator.ofFloat(topCorner, "rotation", 0, 360);
        innerAnimator.setDuration(30000);
        innerAnimator.setRepeatMode(ValueAnimator.RESTART);
        innerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        innerAnimator.setInterpolator(new LinearInterpolator());

        ObjectAnimator outerAnimator = ObjectAnimator.ofFloat(bottomCorner, "rotation", 0, -360);
        outerAnimator.setDuration(60000);
        outerAnimator.setRepeatMode(ValueAnimator.RESTART);
        outerAnimator.setRepeatCount(ValueAnimator.INFINITE);
        outerAnimator.setInterpolator(new LinearInterpolator());

        AnimatorSet set = new AnimatorSet();
        set.play(innerAnimator).with(outerAnimator);
        set.start();
    }
}
