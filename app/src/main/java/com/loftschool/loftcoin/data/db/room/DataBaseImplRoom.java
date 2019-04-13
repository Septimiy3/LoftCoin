package com.loftschool.loftcoin.data.db.room;


import com.loftschool.loftcoin.data.db.DataBase;
import com.loftschool.loftcoin.data.db.model.CoinEntity;
import com.loftschool.loftcoin.data.db.model.Wallet;
import com.loftschool.loftcoin.data.db.model.WalletModel;

import java.util.List;

import io.reactivex.Flowable;

public class DataBaseImplRoom implements DataBase {

    private AppDataBase appDataBase;

    public DataBaseImplRoom(AppDataBase appDataBase) {
        this.appDataBase = appDataBase;
    }

    @Override
    public void saveCoins(List<CoinEntity> coins) {
        appDataBase.coinDao().saveCoins(coins);
    }

    @Override
    public Flowable<List<CoinEntity>> getCoins() {
        return appDataBase.coinDao().getCoins();
    }

    @Override
    public CoinEntity getCoin(String symbol) {
        return appDataBase.coinDao().getCoin(symbol);
    }

    @Override
    public void saveWallet(Wallet wallet) {
        appDataBase.walletDao().saveWallet(wallet);
    }

    @Override
    public Flowable<List<WalletModel>> getWallets() {
        return appDataBase.walletDao().grtWallets();
    }
}
