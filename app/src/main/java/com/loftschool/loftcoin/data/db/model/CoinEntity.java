package com.loftschool.loftcoin.data.db.model;

import com.loftschool.loftcoin.utils.Fiat;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "Coin")
public class CoinEntity {

    @PrimaryKey
    public int id;

    public String name;

    public String symbol;

    public String slug;

    public String lastUpdated;

    @Embedded(prefix = "usd")
    public QouteEntity usd;

    @Embedded(prefix = "eur")
    public QouteEntity eur;

    @Embedded(prefix = "rub")
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
