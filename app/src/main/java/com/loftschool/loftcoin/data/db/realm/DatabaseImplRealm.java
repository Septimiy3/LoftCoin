package com.loftschool.loftcoin.data.db.realm;

import com.loftschool.loftcoin.data.db.DataBase;
import com.loftschool.loftcoin.data.db.model.CoinEntity;
import com.loftschool.loftcoin.data.db.model.Transaction;
import com.loftschool.loftcoin.data.db.model.Wallet;

import java.util.List;

import io.reactivex.Flowable;
import io.realm.Realm;
import io.realm.RealmResults;

public class DatabaseImplRealm implements DataBase {

    private Realm realm;

    @Override
    public void open() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void close() {
        realm.close();
    }

    @Override
    public void saveCoins(List<CoinEntity> coins) {
        realm.executeTransaction(realm -> realm.copyToRealmOrUpdate(coins));
    }

    @Override
    public Flowable<List<CoinEntity>> getCoins() {
        Flowable<RealmResults<CoinEntity>> flowable = realm.where(CoinEntity.class)
                .findAll()
                .asFlowable()
                .filter(RealmResults::isLoaded);

        //noinspection unchecked
        return (Flowable) flowable;
    }

    @Override
    public CoinEntity getCoin(String symbol) {
        return realm.where(CoinEntity.class)
                .equalTo("symbol", symbol)
                .findFirst();
    }

    @Override
    public void saveWallet(Wallet wallet) {
        realm.executeTransaction(realm -> realm.copyToRealm(wallet));
    }

    @Override
    public Flowable<List<Wallet>> getWallets() {
        Flowable<RealmResults<Wallet>> flowable = realm.where(Wallet.class)
                .findAll()
                .asFlowable()
                .filter(RealmResults::isLoaded);

        //noinspection unchecked
        return (Flowable) flowable;
    }

    @Override
    public void saveTransaction(List<Transaction> transactions) {
        realm.executeTransaction(realm -> realm.copyToRealm(transactions));
    }

    @Override
    public Flowable<List<Transaction>> getTransactions(String walletId) {
        Flowable<RealmResults<Transaction>> flowable = realm.where(Transaction.class)
                .equalTo("walletId", walletId)
                .findAll()
                .asFlowable()
                .filter(RealmResults::isLoaded);

        //noinspection unchecked
        return (Flowable) flowable;
    }
}
