package com.loftschool.loftcoin.screens.rate;

import com.loftschool.loftcoin.data.api.Api;
import com.loftschool.loftcoin.data.api.model.Coin;
import com.loftschool.loftcoin.data.db.DataBase;
import com.loftschool.loftcoin.data.db.model.CoinEntity;
import com.loftschool.loftcoin.data.db.model.CoinEntityMapper;
import com.loftschool.loftcoin.data.prefs.Prefs;
import com.loftschool.loftcoin.utils.Fiat;
import com.loftschool.loftcoin.work.WorkHelper;

import java.util.List;

import androidx.annotation.Nullable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class RatePresenterImpl implements RatePresenter {

    private Prefs prefs;
    private Api api;
    private DataBase mainDatabase;
    private DataBase workerDatabase;
    private CoinEntityMapper coinEntityMapper;
    private WorkHelper workHelper;


    @Nullable
    private RateView view;

    private CompositeDisposable disposables = new CompositeDisposable();

    public RatePresenterImpl(Prefs prefs,
                             Api api,
                             DataBase mainDatabase,
                             DataBase workerDatabase,
                             CoinEntityMapper coinEntityMapper,
                             WorkHelper workHelper) {
        this.prefs = prefs;
        this.api = api;
        this.mainDatabase = mainDatabase;
        this.workerDatabase = workerDatabase;
        this.coinEntityMapper = coinEntityMapper;
        this.workHelper = workHelper;

    }

    @Override
    public void attachView(RateView view) {
        this.view = view;
        mainDatabase.open();
    }

    @Override
    public void detachView() {
        disposables.dispose();
        view = null;
        mainDatabase.close();
    }

    @Override
    public void getRate() {
        Disposable disposable = mainDatabase.getCoins()
                .subscribe(coinEntities -> {
                    if (view != null) {
                        view.setCoins(coinEntities);
                    }
                }, Timber::e);

        disposables.add(disposable);
    }

    private void loadRate() {

        Disposable disposable = api.rates(Api.CONVERT)
                .subscribeOn(Schedulers.io())
                .map(rateResponse -> {
                    List<Coin> coins = rateResponse.data;
                    List<CoinEntity> coinEntities = coinEntityMapper.map(coins);
                    return coinEntities;
                })
                .doOnNext(coinEntities -> {
                    workerDatabase.open();
                    workerDatabase.saveCoins(coinEntities);
                    workerDatabase.close();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        coinEntities -> {
                            if (view != null) {
                                view.setRefreshing(false);
                            }
                        }, throwable -> {
                            Timber.e(throwable);

                            if (view != null) {
                                view.setRefreshing(false);
                            }
                        }
                );


        disposables.add(disposable);

    }

    @Override
    public void onRefresh() {
        loadRate();
    }

    @Override
    public void onMenuItemCurrencyClick() {
        if (view != null) {
            view.showCurrencyDialog();
        }
    }

    @Override
    public void onFiatCurrencySelected(Fiat currency) {
        prefs.setFiatCurrency(currency);

        if (view != null) {
            view.invalidateRates();
        }

    }

    @Override
    public void onRateLongClick(String symbol) {
        Timber.d("onRateLongClick: symbol = %s", symbol);
        workHelper.startSyncRateWorker(symbol);
    }
}
