package com.loftschool.loftcoin.data.db.room;


import com.loftschool.loftcoin.data.db.DataBase;
import com.loftschool.loftcoin.data.db.model.CoinEntity;

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
}
