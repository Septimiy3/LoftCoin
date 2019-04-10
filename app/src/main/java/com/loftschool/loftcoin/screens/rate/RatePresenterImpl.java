package com.loftschool.loftcoin.screens.rate;

import com.loftschool.loftcoin.data.api.Api;
import com.loftschool.loftcoin.data.api.model.Coin;
import com.loftschool.loftcoin.data.db.DataBase;
import com.loftschool.loftcoin.data.db.model.CoinEntity;
import com.loftschool.loftcoin.data.db.model.CoinEntityMapper;
import com.loftschool.loftcoin.data.prefs.Prefs;
import com.loftschool.loftcoin.utils.Fiat;

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
    private DataBase dataBase;
    private CoinEntityMapper coinEntityMapper;


    @Nullable
    private RateView view;

    private CompositeDisposable disposables = new CompositeDisposable();

    public RatePresenterImpl(Prefs prefs, Api api, DataBase dataBase, CoinEntityMapper coinEntityMapper) {
        this.prefs = prefs;
        this.api = api;
        this.dataBase = dataBase;
        this.coinEntityMapper = coinEntityMapper;
    }

    @Override
    public void attachView(RateView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        disposables.dispose();
        view = null;
    }

    @Override
    public void getRate() {
        Disposable disposable = dataBase.getCoins()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(coinEntities -> {
                    if (view != null) {
                        view.setCoins(coinEntities);
                    }
                }, throwable -> {

                });
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
                .doOnNext(coinEntities -> dataBase.saveCoins(coinEntities))
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
}
