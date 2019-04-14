package com.loftschool.loftcoin.data.db.model;

import com.loftschool.loftcoin.utils.Fiat;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class CoinEntity extends RealmObject {

    @PrimaryKey
    public int id;

    public String name;

    public String symbol;

    public String slug;

    public String lastUpdated;

    public QouteEntity usd;

    public QouteEntity eur;

    public QouteEntity rub;


    public QouteEntity getQuote(Fiat fiat) {
        switch (fiat) {
            case USD:
                return usd;
            case EUR:
                return eur;
            case RUB:
                return rub;
        }

        return usd;
    }
}
