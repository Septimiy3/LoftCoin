package com.loftschool.loftcoin.screens.start;

import com.loftschool.loftcoin.data.api.Api;
import com.loftschool.loftcoin.data.api.model.Coin;
import com.loftschool.loftcoin.data.db.DataBase;
import com.loftschool.loftcoin.data.db.model.CoinEntity;
import com.loftschool.loftcoin.data.db.model.CoinEntityMapper;
import com.loftschool.loftcoin.data.prefs.Prefs;

import java.util.List;

import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class StartPresenterImpl implements StartPresenter {

    private Prefs prefs;
    private Api api;
    private DataBase dataBase;
    private CoinEntityMapper coinEntityMapper;

    @Nullable
    private StartView view;

    private CompositeDisposable disposables = new CompositeDisposable();

    public StartPresenterImpl(Prefs prefs, Api api, DataBase dataBase, CoinEntityMapper coinEntityMapper) {
        this.prefs = prefs;
        this.api = api;
        this.dataBase = dataBase;
        this.coinEntityMapper = coinEntityMapper;
    }

    @Override
    public void attachView(StartView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        disposables.dispose();
        this.view = null;
    }

    @Override
    public void loadRates() {


        Disposable disposable = api.rates(Api.CONVERT)
                .subscribeOn(Schedulers.io())
                .map(rateResponse -> {
                    List<Coin> coins = rateResponse.data;
                    List<CoinEntity> coinEntities = coinEntityMapper.map(coins);
                    return coinEntities;
                })
                .doOnNext(coinEntities -> dataBase.saveCoins(coinEntities))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(coinEntities -> {
                    if (view != null) {
                        view.navigateToMainScreen();
                    }
                }, throwable -> Timber.e(throwable));


        disposables.add(disposable);


    }
}
