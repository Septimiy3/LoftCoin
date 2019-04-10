package com.loftschool.loftcoin.screens.currencies;

import com.loftschool.loftcoin.data.db.model.CoinEntity;

public interface CurrenciesAdapterListener {
    void onCurrencyClick(CoinEntity coin);
}