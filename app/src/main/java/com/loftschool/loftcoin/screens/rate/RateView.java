package com.loftschool.loftcoin.screens.rate;

import com.loftschool.loftcoin.data.api.model.Coin;

import java.util.List;

public interface RateView {

    void setCoins(List<Coin> coins);

    void setRefreshing(Boolean refreshing);

    void showCurrencyDialog();
}
