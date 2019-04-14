package com.loftschool.loftcoin.data.db;

import com.loftschool.loftcoin.data.db.model.CoinEntity;
import com.loftschool.loftcoin.data.db.model.Transaction;
import com.loftschool.loftcoin.data.db.model.Wallet;

import java.util.List;

import io.reactivex.Flowable;

public interface DataBase {

    void open();

    void close();


    void saveCoins(List<CoinEntity> coins);

    Flowable<List<CoinEntity>> getCoins();

    CoinEntity getCoin(String symbol);

    void saveWallet(Wallet wallet);

    Flowable<List<Wallet>> getWallets();

    void saveTransaction(List<Transaction> transactions);

    Flowable<List<Transaction>> getTransactions(String walletId);

}
