package com.loftschool.loftcoin.data.db;

import com.loftschool.loftcoin.data.db.model.CoinEntity;

import java.util.List;

public interface DataBase {

    void saveCoins(List<CoinEntity> coins);

    List<CoinEntity> getCoins();
}
