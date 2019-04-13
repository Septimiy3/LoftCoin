package com.loftschool.loftcoin.screens.wallets;

import android.app.Application;

import com.loftschool.loftcoin.App;
import com.loftschool.loftcoin.data.db.DataBase;
import com.loftschool.loftcoin.data.db.model.CoinEntity;
import com.loftschool.loftcoin.data.db.model.Wallet;
import com.loftschool.loftcoin.data.db.model.WalletModel;
import com.loftschool.loftcoin.utils.SingleLiveData;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class WalletsViewModelImpl extends WalletsViewModel {

    private DataBase dataBase;
    private CompositeDisposable disposables = new CompositeDisposable();

    public WalletsViewModelImpl(@NonNull Application application) {
        super(application);
        Timber.d("ViewModel constructor");

        dataBase = ((App) getApplication()).getDataBase();
    }

    private SingleLiveData<Object> selectCurrency = new SingleLiveData<>();
    private MutableLiveData<Boolean> walletsVisible = new MutableLiveData<>();
    private MutableLiveData<Boolean> newWalletsVisible = new MutableLiveData<>();
    private MutableLiveData<List<WalletModel>> wallets = new MutableLiveData<>();

    @Override
    public LiveData<Object> selectCurrency() {
        return selectCurrency;
    }

    @Override
    public LiveData<Boolean> walletsVisible() {
        return walletsVisible;
    }

    @Override
    public LiveData<Boolean> newWalletsVisible() {
        return newWalletsVisible;
    }

    @Override
    public LiveData<List<WalletModel>> wallets() {
        return wallets;
    }

    @Override
    void getWallets() {
        Disposable disposable = dataBase.getWallets()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        walletModels -> {
                            if (walletModels.size() == 0) {
                                newWalletsVisible.setValue(true);
                                walletsVisible.setValue(false);
                            } else {
                                newWalletsVisible.setValue(false);
                                walletsVisible.setValue(true);

                                wallets.setValue(walletModels);
                            }
                        },
                        Timber::e
                );
        disposables.add(disposable);
    }

    @Override
    void onNewWalletClick() {
        selectCurrency.postValue(new Object());
    }

    @Override
    void onCurrencySelected(CoinEntity coin) {
        Wallet wallet = randomWallet(coin);

        Disposable disposable = Observable.fromCallable(() -> {
            dataBase.saveWallet(wallet);
            return new Object();
        })
                .observeOn(Schedulers.io())
                .subscribe(o -> {

                }, Timber::e);
        disposables.add(disposable);
    }

    private Wallet randomWallet(CoinEntity coin) {
        Random random = new Random();
        return new Wallet(UUID.randomUUID().toString(), coin.id, 10 * random.nextDouble());
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
        super.onCleared();
    }
}
